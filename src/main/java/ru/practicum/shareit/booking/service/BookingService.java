package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {

    BookingDto addBooking(BookingDto bookingDto, int userId);
    BookingDto approveBooking(int userId, boolean isApproved);
    BookingDto updateBooking(int userId, BookingDto bookingDto, int id);
    BookingDto getBooking(int userId, int id);
    BookingDto getAllBookings();
    BookingDto getBookingsByItem();

}
