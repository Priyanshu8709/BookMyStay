package com.BookMyStay.bookmystay.Service;

import com.BookMyStay.bookmystay.Dto.ProfileUpdateRequestDto;
import com.BookMyStay.bookmystay.Dto.UserDto;
import com.BookMyStay.bookmystay.Entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    User getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
