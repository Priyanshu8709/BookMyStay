package com.BookMyStay.bookmystay.Dto;

import com.BookMyStay.bookmystay.Entity.Enum.Gender;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class GuestDto {
    private Long id;
    private String name;
    private Gender gender;
    private LocalDate dateOfBirth;
}
