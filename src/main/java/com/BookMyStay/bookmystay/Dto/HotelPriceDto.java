package com.BookMyStay.bookmystay.Dto;

import com.BookMyStay.bookmystay.Entity.Hotel;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelPriceDto {
    private Hotel hotel;
    private Double price;
}
