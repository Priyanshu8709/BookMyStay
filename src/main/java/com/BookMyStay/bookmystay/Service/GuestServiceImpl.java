package com.BookMyStay.bookmystay.Service;

import com.BookMyStay.bookmystay.Dto.GuestDto;
import com.BookMyStay.bookmystay.Entity.Guest;
import com.BookMyStay.bookmystay.Entity.User;
import com.BookMyStay.bookmystay.Repository.GuestRepository;
import com.BookMyStay.bookmystay.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.BookMyStay.bookmystay.Util.AppUtil.getCurrentUser;

@Service
@Slf4j
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {
    private final GuestRepository guestRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<GuestDto> getAllGuests() {
        User user = getCurrentUser();
        log.info("Fetching all guests of user with id: {}", user.getId());
        List<Guest> guests = guestRepository.findByUser(user);
        return guests.stream()
                .map(guest -> modelMapper.map(guest, GuestDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updateGuest(Long guestId, GuestDto guestDto) {
        log.info("Updating guest with ID: {}", guestId);
        Guest guest = guestRepository.findById(guestId).orElseThrow(()->
                new UsernameNotFoundException("Guest with ID: " + guestId));
        User user =getCurrentUser();
        if (!Objects.equals(user.getId(), guest.getUser().getId())) {
            throw new UsernameNotFoundException("You are not the user of this guest" + guestId);
        }
        modelMapper.map(guestDto, guest);
        guest.setUser(user);
        guest.setId(guestId);
        guestRepository.save(guest);
    }

    @Override
    public void deleteGuest(Long guestId) {
        log.info("Deleting guest with ID: {}", guestId);
        Guest guest = guestRepository.findById(guestId).orElseThrow(()->
                new UsernameNotFoundException("guest not found with id:"+guestId));
        User user=getCurrentUser();
        if (!Objects.equals(user.getId(), guest.getUser().getId())) {
            throw new UsernameNotFoundException("You are not the user of this guest" + guestId);
        }
        guestRepository.delete(guest);

    }

    @Override
    public GuestDto addGuest(GuestDto guestDto) {
        log.info("Adding guest with ID: {}", guestDto.getId());
        Guest guest=modelMapper.map(guestDto,Guest.class);
        User user=getCurrentUser();
        guest.setUser(user);
        guestRepository.save(guest);
        return modelMapper.map(guest,GuestDto.class);
    }

}
