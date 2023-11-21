package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 *TODO Sprint add-bookings.
 */

@Data
public class BookingDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private int itemId;


    public BookingDto(LocalDateTime start, LocalDateTime end, int id) {

    }
}