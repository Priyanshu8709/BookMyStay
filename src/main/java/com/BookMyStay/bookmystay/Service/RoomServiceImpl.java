package com.BookMyStay.bookmystay.Service;

import com.BookMyStay.bookmystay.Dto.RoomDto;
import com.BookMyStay.bookmystay.Entity.Hotel;
import com.BookMyStay.bookmystay.Entity.Room;
import com.BookMyStay.bookmystay.Entity.User;
import com.BookMyStay.bookmystay.Exception.ResourceNotFoundException;
import com.BookMyStay.bookmystay.Repository.HotelRepository;
import com.BookMyStay.bookmystay.Repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.BookMyStay.bookmystay.Util.AppUtil.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public RoomDto createRoom(Long hotelId, RoomDto roomDto) {
        Hotel hotel = getOwnedHotel(hotelId);
        Room room = modelMapper.map(roomDto, Room.class);
        room.setId(null);
        room.setHotel(hotel);
        room = roomRepository.save(room);
        inventoryService.initializeRoomForAYear(room);
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public List<RoomDto> getRoomsByHotel(Long hotelId) {
        Hotel hotel = getOwnedHotel(hotelId);
        return hotel.getRooms().stream()
                .map(room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoomDto updateRoom(Long hotelId, Long roomId, RoomDto roomDto) {
        getOwnedHotel(hotelId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
        if (!Objects.equals(room.getHotel().getId(), hotelId)) {
            throw new AccessDeniedException("Room does not belong to hotel with id: " + hotelId);
        }

        room.setType(roomDto.getType());
        room.setBasePrice(roomDto.getBasePrice());
        room.setPhotos(roomDto.getPhotos());
        room.setAmenities(roomDto.getAmenities());
        room.setTotalCount(roomDto.getTotalCount());
        room.setCapacity(roomDto.getCapacity());
        return modelMapper.map(roomRepository.save(room), RoomDto.class);
    }

    @Override
    @Transactional
    public void deleteRoom(Long hotelId, Long roomId) {
        getOwnedHotel(hotelId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
        if (!Objects.equals(room.getHotel().getId(), hotelId)) {
            throw new AccessDeniedException("Room does not belong to hotel with id: " + hotelId);
        }
        inventoryService.deleteAllInventories(room);
        roomRepository.delete(room);
    }

    private Hotel getOwnedHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        User user = getCurrentUser();
        if (hotel.getOwner() == null || !Objects.equals(hotel.getOwner().getId(), user.getId())) {
            throw new AccessDeniedException("You are not the owner of hotel with id: " + hotelId);
        }
        return hotel;
    }
}
