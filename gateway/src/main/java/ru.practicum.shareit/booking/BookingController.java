package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private BookingClient bookingClient;

    public BookingController(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> addBooking(@Valid @RequestBody BookItemRequestDto bookingDto,
                                             @RequestHeader("X-Sharer-User-Id") @PositiveOrZero int userId) {
        log.info("Получен запрос на создание нового бронирования.");

        return bookingClient.addBooking(userId, bookingDto);
    }

    @ResponseBody
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader(value = "X-Sharer-User-Id") @PositiveOrZero int userId,
                                             @PathVariable @PositiveOrZero int bookingId,
                                             @RequestParam boolean approved) {
        log.info("Получен запрос на обновление статуса бронирования");
        return bookingClient.approveBooking(bookingId, userId, approved);
    }

    @ResponseBody
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(value = "X-Sharer-User-Id") @PositiveOrZero int userId,
                                         @PathVariable @PositiveOrZero int bookingId) {
        log.info("Получен запрос на получение информации о бронировании");

        return bookingClient.getBooking(userId, bookingId);
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<Object> getBookingByUserId(@RequestHeader(value = "X-Sharer-User-Id") @PositiveOrZero int userId,
                                                       @RequestParam(defaultValue = "ALL") String state,
                                                       @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                                       @RequestParam(value = "size", defaultValue = "20") @Min(0) int size) {
        log.info("Получен запрос на получение списка всех бронирований текущего пользователя");

        return bookingClient.getBookingsByUserId(userId, state, from, size);
    }

    @ResponseBody
    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsForAllItems(@RequestHeader(value = "X-Sharer-User-Id") @PositiveOrZero int userId,
                                                           @RequestParam(defaultValue = "ALL") String state,
                                                           @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                                           @RequestParam(value = "size", defaultValue = "20") @Min(0) int size) {
        log.info("Получен запрос на получение списка бронирований для всех вещей текущего пользователя");

        return bookingClient.getBookingsForAllItems(userId, state, from, size);
    }
}