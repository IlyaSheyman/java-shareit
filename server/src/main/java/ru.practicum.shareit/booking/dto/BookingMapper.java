package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoSmall;
import ru.practicum.shareit.user.dto.UserDtoSmall;

public class BookingMapper {
    public BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId()
        );
    }

    public BookingDtoExtended toBookingDtoExt(Booking booking) {
        return new BookingDtoExtended(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                new ItemDtoSmall(booking.getItem().getId(), booking.getItem().getName()),
                new UserDtoSmall(booking.getBooker().getId())
        );
    }

    public BookingDtoForGetItem toBookingDtoForGetItem(Booking booking) {
        return new BookingDtoForGetItem(
                booking.getId(),
                booking.getBooker().getId()
        );
    }
}
