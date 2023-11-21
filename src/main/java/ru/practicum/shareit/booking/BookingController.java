package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;

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
    public BookingDto addBooking(@Valid @RequestBody BookingDto bookingDto,
                           @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Получен запрос на создание нового бронирования.");

        return bookingService.addBooking(bookingDto, userId);
    }
}


//
//    @ResponseBody
//    @PostMapping
//    public ItemDto addItem(@Valid @RequestBody ItemDto item,
//                           @RequestHeader("X-Sharer-User-Id") int userId) {
//        log.info("Получен запрос на добавление вещи.");
//        return itemService.addItem(item, userId);
//    }
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
