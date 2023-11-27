package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoForGetItem;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Optional;

@Data
public class ItemDtoWithBookings {
    private int id;
    private String name;
    private String description;
    private Optional<Boolean> available;
    private int requestId;
    private BookingDtoForGetItem lastBooking;
    private BookingDtoForGetItem nextBooking;

    public ItemDtoWithBookings(int id,
                               String name,
                               String description,
                               Optional<Boolean> available,
                               int requestId,
                               BookingDtoForGetItem lastBooking,
                               BookingDtoForGetItem nextBooking) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
    }
}