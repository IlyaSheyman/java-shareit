package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BookingDtoForGetItem {
    private int id;
    private int bookerId;

    public BookingDtoForGetItem(int id, int bookerId) {
        this.id = id;
        this.bookerId = bookerId;
    }
}