package com.BookMyStay.bookmystay.Dto;

import com.BookMyStay.bookmystay.Entity.Enum.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileUpdateRequestDto {
    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
}
