package ru.practicum.shareit.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoExtended;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingServiceDbImpl;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceDbImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceDbImpl bookingService;

    @Test
    @DisplayName("Юнит-тест успешного создания объекта Booking")
    public void testAddBookingSuccess() {
        int userId = 1;
        int bookerId = 2;
        int itemId = 2;

        BookingDto bookingDto = BookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(itemId)
                .build();

        User owner = User.builder()
                .email("testemail@test.ru")
                .id(userId)
                .name("Gustavo")
                .build();

        User booker = User.builder()
                .email("testemail2@test.ru")
                .id(bookerId)
                .name("Fring")
                .build();

        Item item = Item.builder()
                .id(itemId)
                .available(true)
                .owner(owner)
                .name("Test name")
                .description("Test description")
                .build();

        when(userRepository.existsById(bookerId)).thenReturn(true);
        when(itemRepository.existsById(itemId)).thenReturn(true);
        when(itemRepository.getById(itemId)).thenReturn(item);
        when(userRepository.getById(bookerId)).thenReturn(booker);

        BookingDtoExtended result = bookingService.addBooking(bookingDto, bookerId);

        assertNotNull(result);
        assertEquals(Status.WAITING, result.getStatus());
        assertNotNull(result.getBooker());
        assertEquals(booker.getId(), result.getBooker().getId());
    }

    @Test
    @DisplayName("Юнит-тест исключения при создании booking: пользователь не найден")
    public void testAddBookingUserNotFound() {
        int userId = 1;
        int itemId = 2;

        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingDto.setItemId(itemId);

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> bookingService.addBooking(bookingDto, userId));
    }

    @Test
    @DisplayName("Юнит-тест исключения при создании booking: вещь не найдена")
    public void testAddBookingItemNotFound() {
        int userId = 1;
        int itemId = 2;

        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingDto.setItemId(itemId);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRepository.existsById(itemId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> bookingService.addBooking(bookingDto, userId));
    }

    @Test
    @DisplayName("Юнит-тест исключения при создании booking: некорректные даты")
    public void testAddBookingInvalidDates() {
        int userId = 1;
        int itemId = 2;

        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().minusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingDto.setItemId(itemId);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRepository.existsById(itemId)).thenReturn(true);

        assertThrows(ValidationException.class, () -> bookingService.addBooking(bookingDto, userId));
    }

    @Test
    @DisplayName("Юнит-тест исключения при создании booking: некорректные даты")
    public void testAddBookingSameStartAndEndDate() {
        int userId = 1;
        int itemId = 2;

        BookingDto bookingDto = new BookingDto();
        LocalDateTime now = LocalDateTime.now();
        bookingDto.setStart(now);
        bookingDto.setEnd(now);
        bookingDto.setItemId(itemId);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRepository.existsById(itemId)).thenReturn(true);

        assertThrows(ValidationException.class, () -> bookingService.addBooking(bookingDto, userId));
    }

    @Test
    @DisplayName("Юнит-тест исключения при создании booking: некорректные даты")
    public void testAddBookingEndDateBeforeStartDate() {
        int userId = 1;
        int itemId = 2;

        BookingDto bookingDto = new BookingDto();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.minusDays(1);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);
        bookingDto.setItemId(itemId);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRepository.existsById(itemId)).thenReturn(true);

        assertThrows(ValidationException.class, () -> bookingService.addBooking(bookingDto, userId));
    }

    @Test
    @DisplayName("Юнит-тест исключения при создании booking: вещь недоступна")
    public void testAddBookingItemNotAvailable() {
        int userId = 1;
        int itemId = 2;

        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingDto.setItemId(itemId);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRepository.existsById(itemId)).thenReturn(true);
        when(itemRepository.getById(itemId)).thenReturn(Item.builder().available(false).build());

        assertThrows(ValidationException.class, () -> bookingService.addBooking(bookingDto, userId));
    }

    @Test
    @DisplayName("Юнит-тест исключения при создании booking: владелец вещи пытается создать бронирование")
    public void testAddBookingOwnerTryingToBookOwnItem() {
        int userId = 1;
        int itemId = 2;

        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingDto.setItemId(itemId);

        User owner = User.builder().id(userId).build();
        Item item = Item.builder().id(itemId).available(true).owner(owner).build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRepository.existsById(itemId)).thenReturn(true);
        when(itemRepository.getById(itemId)).thenReturn(item);

        assertThrows(NotFoundException.class, () -> bookingService.addBooking(bookingDto, userId));
    }

    @Test
    @DisplayName("Юнит-тест исключения при подтверждении booking: пользователь не найден")
    public void testApproveBookingUserNotFound() {
        int userId = 1;
        int bookingId = 2;
        boolean isApproved = true;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> bookingService.approveBooking(bookingId, userId, isApproved));
    }

    @Test
    @DisplayName("Юнит-тест исключения при подтверждении booking: бронирование не найдено")
    public void testApproveBookingBookingNotFound() {
        int userId = 1;
        int bookingId = 2;
        boolean isApproved = true;

        when(userRepository.existsById(userId)).thenReturn(true);
        when(bookingRepository.existsById(bookingId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> bookingService.approveBooking(bookingId, userId, isApproved));
    }

    @Test
    @DisplayName("Юнит-тест для успешного подтверждения бронирования")
    public void testApproveBookingOwnerApproving() {
        int userId = 1;
        int bookingId = 2;
        boolean isApproved = true;

        User user = User.builder().id(userId).build();

        Booking booking = Booking.builder()
                .id(bookingId)
                .booker(user)
                .status(Status.WAITING)
                .item(Item.builder().owner(user).build())
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(bookingRepository.existsById(bookingId)).thenReturn(true);
        when(bookingRepository.getById(bookingId)).thenReturn(booking);

        BookingDtoExtended result = bookingService.approveBooking(bookingId, userId, isApproved);

        assertEquals(Status.APPROVED, result.getStatus());
    }

    @Test
    @DisplayName("Юнит-тест для успешного отказа от бронирования")
    public void testApproveBookingOwnerRejecting() {
        int userId = 1;
        int bookingId = 2;
        boolean isApproved = false;

        User booker = User.builder().id(2).build();

        Booking booking = Booking.builder()
                .id(bookingId)
                .booker(booker)
                .status(Status.WAITING)
                .item(Item.builder().owner(User.builder().id(userId).build()).build())
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(bookingRepository.existsById(bookingId)).thenReturn(true);
        when(bookingRepository.getById(bookingId)).thenReturn(booking);

        BookingDtoExtended result = bookingService.approveBooking(bookingId, userId, isApproved);

        assertEquals(Status.REJECTED, result.getStatus());
    }

    @Test
    @DisplayName("Юнит-тест для исключения при бронировании Booking: другой владелец пытается подтвердить")
    public void testApproveBookingNonOwnerTryingToApprove() {
        int userId = 1;
        int bookingId = 2;
        boolean isApproved = true;

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.WAITING)
                .item(Item.builder().owner(User.builder().id(3).build()).build())
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(bookingRepository.existsById(bookingId)).thenReturn(true);
        when(bookingRepository.getById(bookingId)).thenReturn(booking);

        assertThrows(NotFoundException.class, () -> bookingService.approveBooking(bookingId, userId, isApproved));
    }

    @Test
    @DisplayName("Юнит-тест для исключения при бронировании Booking: другой владелец пытается подтвердить")
    public void testApproveBookingAlreadyApproved() {
        int userId = 1;
        int bookingId = 2;
        boolean isApproved = true;

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.APPROVED)
                .item(Item.builder().owner(User.builder().id(userId).build()).build())
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(bookingRepository.existsById(bookingId)).thenReturn(true);
        when(bookingRepository.getById(bookingId)).thenReturn(booking);

        assertThrows(ValidationException.class, () -> bookingService.approveBooking(bookingId, userId, isApproved));
    }

}