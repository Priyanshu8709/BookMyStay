package com.BookMyStay.bookmystay.Security;

import com.BookMyStay.bookmystay.Dto.LoginDto;
import com.BookMyStay.bookmystay.Entity.Enum.Role;
import com.BookMyStay.bookmystay.Entity.User;
import com.BookMyStay.bookmystay.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private org.modelmapper.ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginShouldLoadUserFromRepositoryAfterAuthentication() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("asha@example.com");
        loginDto.setPassword("password123");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "unexpected-principal",
                null
        );
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        User user = new User();
        user.setId(1L);
        user.setEmail("asha@example.com");
        user.setPassword("encoded-password");
        user.setName("Asha");
        user.setRole(Role.USER);

        when(userRepository.findByEmail("asha@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(user)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user)).thenReturn("refresh-token");

        String[] tokens = authService.login(loginDto);

        assertArrayEquals(new String[]{"access-token", "refresh-token"}, tokens);
    }
}
