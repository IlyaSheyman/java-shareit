package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoExtended;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
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
        } else if (itemRepository.getById(itemId).getOwner().getId() == userId) {
            throw new NotFoundException("Владелец вещи не может забронировать её");
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
                    if (booking.getStatus().equals(Status.APPROVED)) {
                        throw new ValidationException("Бронирование уже одобрено");
                    }
                    booking.setStatus(Status.APPROVED);
                } else {
                    booking.setStatus(Status.REJECTED);
                }

                bookingRepository.save(booking);
                return bookingMapper.toBookingDtoExt(booking);
            } else {
                throw new NotFoundException("Доступ к изменению статуса бронирования " +
                        "имеет только владелец");
            }
        }
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
                throw new NotFoundException("Доступ к вещи имеют только владелец и автор бронирования");
            }
        }
    }

    @Override
    public List<BookingDtoExtended> getBookingsByUserId(int userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else {
            List<Booking> bookingsByUserId = getBookingsByStateAndId(state, userId);

            List<BookingDtoExtended> bookingsDtoByUserId = new ArrayList<>();

            return bookingsByUserId.stream()
                    .map(bookingMapper::toBookingDtoExt)
                    .sorted(Comparator.comparing(BookingDtoExtended::getStart).reversed())
                    .collect(Collectors.toList());
        }
    }

    public List<Booking> getBookingsByStateAndId(String state, int userId) {
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ("ALL"):
                bookings = bookingRepository.findByBooker_Id(userId);
                break;
            case ("CURRENT"):
                bookings = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(userId,
                        LocalDateTime.now(),
                        LocalDateTime.now());
                break;
            case ("**PAST**"):
                bookings = bookingRepository.findByBooker_IdAndEndIsBefore(userId,
                        LocalDateTime.now());
                break;
            case ("FUTURE"):
                bookings = bookingRepository.findByBooker_IdAndStartIsAfter(userId,
                        LocalDateTime.now());
                break;
            case ("WAITING"):
                bookings = bookingRepository.findByBooker_IdAndStatusIs(userId,
                        Status.WAITING);
                break;
            case ("REJECTED"):
                bookings = bookingRepository.findByBooker_IdAndStatusIs(userId,
                        Status.REJECTED);
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
        return bookings;
    }

    @Override
    public List<BookingDtoExtended> getBookingsByItemOwner(int userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else {
            List<Booking> bookingsByItemOwner = getBookingsByOwner(state, userId);

            List<BookingDtoExtended> bookingsDtoByUserId = new ArrayList<>();

            return bookingsByItemOwner.stream()
                    .map(bookingMapper::toBookingDtoExt)
                    .sorted(Comparator.comparing(BookingDtoExtended::getStart).reversed())
                    .collect(Collectors.toList());
        }
    }

    public List<Booking> getBookingsByOwner(String state, int userId) {
        List<Item> items = itemRepository.findByOwner_Id(userId);
        List<Integer> itemIds = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        List<Booking> bookings = new ArrayList<>();

        switch (state) {
            case ("ALL"):
                for (Integer i : itemIds) {
                    bookings.addAll(bookingRepository.findByItem_Id(i));
                }
                break;
            case ("CURRENT"):
                for (Integer i : itemIds) {
                    bookings.addAll(bookingRepository.findByItem_IdAndStartIsBeforeAndEndIsAfter(i,
                            LocalDateTime.now(),
                            LocalDateTime.now()));
                }
                break;
            case ("**PAST**"):
                for (Integer i : itemIds) {
                    bookings.addAll(bookingRepository.findByItem_IdAndEndIsBefore(i,
                            LocalDateTime.now()));
                }
                break;
            case ("FUTURE"):
                for (Integer i : itemIds) {
                    bookings.addAll(bookingRepository.findByItem_IdAndStartIsAfter(i,
                            LocalDateTime.now()));
                }
                break;
            case ("WAITING"):
                for (Integer i : itemIds) {
                    bookings.addAll(bookingRepository.findByItem_IdAndStatusIs(i,
                            Status.WAITING));
                }
                break;
            case ("REJECTED"):
                for (Integer i : itemIds) {
                    bookings.addAll(bookingRepository.findByItem_IdAndStatusIs(i,
                            Status.REJECTED));
                }
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
        return bookings;
    }
}