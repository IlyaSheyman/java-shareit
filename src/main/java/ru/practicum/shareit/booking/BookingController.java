package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoExtended;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {

    BookingService bookingService;

    public BookingController(@Qualifier("BookingServiceDbImpl") BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @ResponseBody
    @PostMapping
    public BookingDtoExtended addBooking(@Valid @RequestBody BookingDto bookingDto,
                                         @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Получен запрос на создание нового бронирования.");

        return bookingService.addBooking(bookingDto, userId);
    }

    @ResponseBody
    @PatchMapping("/{bookingId}")
    public BookingDtoExtended approveBooking(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                             @PathVariable int bookingId,
                                             @RequestParam boolean approved) {
        log.info("Получен запрос на обновление статуса бронирования");
        return bookingService.approveBooking(bookingId, userId, approved);
    }

    @ResponseBody
    @GetMapping("/{bookingId}")
    public BookingDtoExtended getBooking(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                         @PathVariable int bookingId) {
        log.info("Получен запрос на получение информации о бронировании");

        return bookingService.getBooking(userId, bookingId);
    }

    @ResponseBody
    @GetMapping
    public List<BookingDtoExtended> getBookingByUserId(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                               @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получен запрос на получение списка всех бронирований текущего пользователя");

        return bookingService.getBookingsByUserId(userId, state);
    }

    @ResponseBody
    @GetMapping("/owner")
    public List<BookingDtoExtended> getBookingsForAllItems(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                                              @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получен запрос на получение списка бронирований для всех вещей текущего пользователя");

        return bookingService.getBookingsByItemOwner(userId, state);
    }
}


//
//    @ResponseBody
//    @PatchMapping("/{id}")
//    public ItemDto updateItem(@Valid @RequestBody ItemDto item,
//                              @PathVariable int id,
//                              @RequestHeader("X-Sharer-User-Id") int userId) {
//        log.info("Получен запрос на обновление вещи.");
//        return itemService.updateItem(id, item, userId);
//    }
//
//    @ResponseBody
//    @GetMapping("/{id}")
//    public ItemDto getItemById(@PathVariable int id,
//                               @RequestHeader("X-Sharer-User-Id") int userId) {
//        log.info("Получен запрос на получение вещи с ID - {}.", id);
//        return itemService.getItem(id, userId);
//    }
//
//    @ResponseBody
//    @GetMapping
//    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") int userId) {
//        return itemService.getItems(userId);
//    }
//
//    @ResponseBody
//    @GetMapping("/search")
//    public List<ItemDto> search(@RequestHeader(value = "X-Sharer-User-Id", required = false) int userId,
//                                @RequestParam String text) {
//        return itemService.search(text);
//    }
//}
