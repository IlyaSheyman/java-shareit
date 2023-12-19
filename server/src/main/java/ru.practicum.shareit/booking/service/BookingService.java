package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoExtended;

import java.util.List;

public interface BookingService {

    BookingDtoExtended addBooking(BookingDto bookingDto, int userId);

    BookingDtoExtended approveBooking(int bookingId, int userId, boolean isApproved);

    BookingDtoExtended getBooking(int userId, int bookingId);

    List<BookingDtoExtended> getBookingsByUserId(int userId, String state, int from, int size);

    List<BookingDtoExtended> getBookingsByItemOwner(int userId, String state, int from, int size);

}