package com.BookMyStay.bookmystay.Dto;

import com.BookMyStay.bookmystay.Entity.Enum.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Gender gender;
    private LocalDate dob;
}
