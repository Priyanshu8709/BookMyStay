package com.BookMyStay.bookmystay.Service;

import com.BookMyStay.bookmystay.Dto.*;
import com.BookMyStay.bookmystay.Entity.Hotel;
import com.BookMyStay.bookmystay.Entity.Room;
import com.BookMyStay.bookmystay.Entity.User;
import com.BookMyStay.bookmystay.Repository.HotelRepository;
import com.BookMyStay.bookmystay.Repository.InventoryRepository;
import com.BookMyStay.bookmystay.Repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.BookMyStay.bookmystay.Util.AppUtil.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final HotelService hotelService;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("createNewHotel");
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);
        hotel.setActive(true);
        hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long hotelId) {
        log.info("getHotelById");
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new RuntimeException("hotel not found"));
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new RuntimeException("user not owned");
        }
        return modelMapper.map(hotel, HotelDto.class);

    }

    @Override
    public HotelDto updateHotelById(Long hotelId, HotelDto hotelDto) {
        log.info("updateHotelById");
        Hotel hotel=hotelRepository.findById(hotelId).orElseThrow(() -> new RuntimeException("hotel not found"));
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new RuntimeException("user not owned");
        }
        modelMapper.map(hotelDto, hotel);
        hotel.setId(hotelId);
        hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelDto.class);

    }

    @Override
    public void deleteHotelByID(Long hotelId) {
        log.info("deleteHotelById");
        Hotel hotel =hotelRepository.findById(hotelId).orElseThrow(() -> new RuntimeException("hotel not found"));
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new RuntimeException("user not owned");
        }
        for(Room room: hotel.getRooms()) {
            inventoryService.deleteAllInventories(room);
            roomRepository.deleteById(room.getId());
        }
        hotelRepository.delete(hotel);
    }

    @Override
    @Transactional
    public void activateHotelById(Long hotelId) {
        log.info("activateHotelById");
        Hotel hotel=hotelRepository.findById(hotelId).orElseThrow(() -> new RuntimeException("hotel not found"));
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new RuntimeException("user not owned");
        }
        hotel.setActive(true);
        for(Room room: hotel.getRooms()) {
            inventoryService.initializeRoomForAYear(room);
        }
    }

    @Override
    @Transactional
    public void deactivateHotelById(Long hotelId) {
        Hotel hotel=hotelRepository.findById(hotelId).orElseThrow(() -> new RuntimeException("hotel not found"));
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new RuntimeException("user not owned");
        }
        hotel.setActive(false);
        for(Room room: hotel.getRooms()) {
            inventoryService.deinitializeRoomForAYear(room);
        }

    }


    @Override
    public List<HotelDto> getAllHotel() {

        User user=getCurrentUser();
        log.info("getHotelInfoById");
        List<Hotel> hotels=hotelRepository.findByOwner(user);

        return hotels
                .stream()
                .map((element) -> modelMapper.map(element, HotelDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId, HotelInfoRequestDto hotelInfoRequestDto) {
        Hotel hotel=hotelRepository.findById(hotelId).orElseThrow(() ->
                new RuntimeException("hotel not found"));
        long daysCount = ChronoUnit.DAYS.between(hotelInfoRequestDto.getStartDate(),
                hotelInfoRequestDto.getEndDate())+1;
        List<RoomPriceDto> roomPriceDtoList = inventoryRepository.findRoomAveragePrice(hotelId,
                hotelInfoRequestDto.getStartDate(), hotelInfoRequestDto.getEndDate(),
                hotelInfoRequestDto.getRoomsCount(), daysCount);

        List<RoomPriceResponseDto> rooms = roomPriceDtoList.stream()
                .map(roomPriceDto -> {
                    RoomPriceResponseDto roomPriceResponseDto = modelMapper.map(roomPriceDto.getRoom(),
                            RoomPriceResponseDto.class);
                    roomPriceResponseDto.setPrice(roomPriceDto.getPrice());
                    return roomPriceResponseDto;
                })
                .collect(Collectors.toList());

        return new HotelInfoDto(modelMapper.map(hotel, HotelDto.class), rooms);

    }
}
