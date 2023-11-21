package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;

@Slf4j
@Service("BookingServiceDbImpl")
public class BookingServiceDbImpl implements BookingService {

    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingServiceDbImpl(BookingRepository bookingRepository,
                                UserRepository userRepository,
                                ItemRepository itemRepository) {
        this.bookingMapper = new BookingMapper();
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public BookingDto addBooking(BookingDto bookingDto, int userId) {
        int itemId = bookingDto.getItemId();
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException("Вещь с id " + itemId + " не найдена");
        } else if (bookingDto.getStart().isBefore(LocalDateTime.now()) || bookingDto.getStart() == null) {
            throw new ValidationException("Некорректная дата начала бронирования");
        } else if (bookingDto.getEnd().isBefore(LocalDateTime.now()) || bookingDto.getEnd() == null) {
            throw new ValidationException("Некорректная дата конца бронирования");
        } else if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new ValidationException("Даты начала и конца бронирования не могут быть одинаковыми");
        } else {
            Booking booking = new Booking();
            booking.setBooker(userRepository.getById(userId));
            booking.setStart(bookingDto.getStart());
            booking.setEnd(bookingDto.getEnd());
            booking.setItem(itemRepository.getById(bookingDto.getItemId()));
            booking.setStatus(Status.WAITING);

            return bookingMapper.toBookingDto(booking);
        }
    }

    @Override
    public BookingDto approveBooking(int userId, boolean isApproved) {
        return null;
    }

    @Override
    public BookingDto updateBooking(int userId, BookingDto bookingDto, int id) {
        return null;
    }

    @Override
    public BookingDto getBooking(int userId, int id) {
        return null;
    }

    @Override
    public BookingDto getAllBookings() {
        return null;
    }

    @Override
    public BookingDto getBookingsByItem() {
        return null;
    }
}
