package com.BookMyStay.bookmystay.Dto;

import com.BookMyStay.bookmystay.Entity.HotelContactInfo;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HotelPriceResponseDto {
    private Long id;
    private String name;
    private String city;
    private String[] photos;
    private String[] amenities;
    private HotelContactInfo contactInfo;
    private Double price;
}
