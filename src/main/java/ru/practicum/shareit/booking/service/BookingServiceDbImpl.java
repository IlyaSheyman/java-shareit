package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoExtended;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessToItemDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public BookingDtoExtended addBooking(BookingDto bookingDto, int userId) {
        int itemId = bookingDto.getItemId();
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException("Вещь с id " + itemId + " не найдена");
        } else if (bookingDto.getStart() == null || bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Некорректная дата начала бронирования");
        } else if (bookingDto.getEnd() == null || bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Некорректная дата конца бронирования");
        } else if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new ValidationException("Даты начала и конца бронирования не могут быть одинаковыми");
        } else if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ValidationException("Дата начала бронирования не может быть после даты конца");
        } else if (!itemRepository.getById(itemId).isAvailable()) {
            throw new ValidationException("Вещь с id " + itemId + " недоступна");
        } else {
            Booking booking = new Booking();
            booking.setBooker(userRepository.getById(userId));
            booking.setStart(bookingDto.getStart());
            booking.setEnd(bookingDto.getEnd());
            booking.setItem(itemRepository.getById(bookingDto.getItemId()));
            booking.setStatus(Status.WAITING);

            bookingRepository.save(booking);

            return bookingMapper.toBookingDtoExt(booking);
        }
    }

    @Override
    public BookingDtoExtended approveBooking(int bookingId, int userId, boolean isApproved) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else if (!bookingRepository.existsById(bookingId)) {
            throw new NotFoundException("Бронирование с id " + bookingId + " не найдено");
        } else {
            Booking booking = bookingRepository.getById(bookingId);
            if (booking.getItem().getOwner().getId() == userId) {
                if (isApproved == true) {
                    booking.setStatus(Status.APPROVED);
                } else {
                    booking.setStatus(Status.REJECTED);
                }

                bookingRepository.save(booking);
                return bookingMapper.toBookingDtoExt(booking);
            } else {
                throw new AccessToItemDeniedException("Доступ к изменению статуса бронирования " +
                        "имеет только владелец");
            }
        }
    }

    @Override
    public BookingDto updateBooking(int userId, BookingDto bookingDto, int id) {
        return null;
    }

    @Override
    public BookingDtoExtended getBooking(int userId, int bookingId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else if (!bookingRepository.existsById(bookingId)) {
            throw new NotFoundException("Бронирование с id " + bookingId + " не найдено");
        } else {
            Booking booking = bookingRepository.getById(bookingId);
            Item item = itemRepository.getById(bookingRepository.getById(bookingId).getItem().getId());

            if (booking.getBooker().getId() == userId
                    || item.getOwner().getId() == userId) {
                return bookingMapper.toBookingDtoExt(bookingRepository.getById(bookingId));
            } else {
                throw new AccessToItemDeniedException("Доступ к вещи имеют только владелец и автор бронирования");
            }
        }
    }

    @Override
    public List<BookingDtoExtended> getBookingsByUserId(int userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else {
            List<Booking> bookingsByUserId = new ArrayList<>();
            switch (state) {
                case ("ALL"):
                    bookingsByUserId = bookingRepository.findByBooker_Id(userId);
                case ("CURRENT"):
                    bookingsByUserId = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(userId,
                            LocalDateTime.now(),
                            LocalDateTime.now());
                case ("**PAST**"):
                    bookingsByUserId = bookingRepository.findByBooker_IdAndEndIsBefore(userId,
                            LocalDateTime.now());
                case ("FUTURE"):
                    bookingsByUserId = bookingRepository.findByBooker_IdAndStartIsAfter(userId,
                            LocalDateTime.now());
                case ("WAITING"):
                    bookingsByUserId = bookingRepository.findByBooker_IdAndStatusIs(userId,
                            Status.WAITING);
                case ("REJECTED"):
                    bookingsByUserId = bookingRepository.findByBooker_IdAndStatusIs(userId,
                            Status.REJECTED);
            }

            List<BookingDtoExtended> bookingsDtoByUserId = new ArrayList<>();

            return bookingsByUserId.stream()
                    .map(bookingMapper::toBookingDtoExt)
                    .sorted(Comparator.comparing(BookingDtoExtended::getStart))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public BookingDto getBookingsByItem() {
        return null;
    }
}
