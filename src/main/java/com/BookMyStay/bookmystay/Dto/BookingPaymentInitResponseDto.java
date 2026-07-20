package com.BookMyStay.bookmystay.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingPaymentInitResponseDto {
    private String sessionUrl;
}
