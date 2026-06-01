package com.BookMyStay.bookmystay.Security;

import com.BookMyStay.bookmystay.Dto.LoginDto;
import com.BookMyStay.bookmystay.Dto.SignUpRequestDto;
import com.BookMyStay.bookmystay.Dto.UserDto;
import com.BookMyStay.bookmystay.Entity.Enum.Role;
import com.BookMyStay.bookmystay.Entity.User;
import com.BookMyStay.bookmystay.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserDto signup(SignUpRequestDto signUpRequestDto){
        User user=userRepository.findByEmail(signUpRequestDto.getEmail()).orElse(null);
        if(user!=null){
            throw new RuntimeException("User already exists");
        }
        User newUser=modelMapper.map(signUpRequestDto,User.class);
        newUser.setRole(Role.USER);
        newUser.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        newUser = userRepository.save(newUser);
        return modelMapper.map(newUser,UserDto.class);
    }
    public String[] login(LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        User user = (User) authentication.getPrincipal();

        String[] arr =new String[2];
        arr[0]=jwtService.generateAccessToken(user);
        arr[1]=jwtService.generateRefreshToken(user);

        return arr;

    }

    public String refreshToken(String refreshToken) {
        Long id = jwtService.getUserIdFromToken(refreshToken);

        User user = userRepository.findById(id).orElseThrow(() ->
                new UsernameNotFoundException("User not found with id: "+id));
        return jwtService.generateAccessToken(user);
    }

}
