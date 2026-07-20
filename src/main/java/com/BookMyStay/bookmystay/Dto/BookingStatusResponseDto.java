package com.BookMyStay.bookmystay.Dto;

import com.BookMyStay.bookmystay.Entity.Enum.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingStatusResponseDto {
    private BookingStatus bookingStatus;
}
