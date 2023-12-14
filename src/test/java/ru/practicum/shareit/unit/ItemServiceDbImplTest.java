package ru.practicum.shareit.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessToItemDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.storage.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceDbImpl;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceDbImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository requestRepository;

    @InjectMocks
    private ItemServiceDbImpl itemService;

    @Test
    void testAddItemSuccess() {
        int userId = 1;

        ItemDto itemDto = new ItemDto(1, "Test item", "Test description", Optional.of(true), 0);

        User mockUser = User.builder().id(userId).name("Test user").email("testemail@gmail.com").build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.getById(userId)).thenReturn(mockUser);

        ItemDto result = itemService.addItem(itemDto, userId);

        assertNotNull(result);
        assertEquals("Test item", result.getName());
        assertEquals("Test description", result.getDescription());
        assertTrue(result.getAvailable().isPresent());
        assertTrue(result.getAvailable().get());
    }

    @Test
    void testAddItemValidationExceptionEmptyName() {

        int userId = 1;
        ItemDto itemDto = new ItemDto(1, "", "Updated Description", Optional.of(true), 0);


        assertThrows(ValidationException.class, () -> itemService.addItem(itemDto, userId));
    }

    @Test
    void testAddItemValidationExceptionNullDescription() {
        int userId = 1;
        ItemDto itemDto = new ItemDto(1, "Test Item", null, Optional.of(true), 0);

        assertThrows(ValidationException.class, () -> itemService.addItem(itemDto, userId));
    }

    @Test
    void testAddItemValidationExceptionEmptyAvailability() {
        int userId = 1;
        ItemDto itemDto = new ItemDto(1, "Test Item", null, Optional.empty(), 0);

        assertThrows(ValidationException.class, () -> itemService.addItem(itemDto, userId));
    }

    @Test
    void testUpdateItemSuccess() {
        int itemId = 1;
        int userId = 1;
        ItemDto itemDto = new ItemDto(1, "Updated Item", "Updated Description", Optional.of(true), 0);

        User owner = new User();
        owner.setId(userId);

        Item existingItem = new Item();
        existingItem.setId(itemId);
        existingItem.setName("Original Item");
        existingItem.setDescription("Original Description");
        existingItem.setAvailable(false);
        existingItem.setOwner(owner);

        when(itemRepository.getById(itemId)).thenReturn(existingItem);

        ItemDto result = itemService.updateItem(itemId, itemDto, userId);

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertTrue(result.getAvailable().isPresent());
        assertTrue(result.getAvailable().get());
    }

    @Test
    void testUpdateItemAccessDeniedException() {
        int itemId = 1;
        int userId = 2;
        ItemDto itemDto = new ItemDto(1, "Updated Item", "Updated Description", Optional.of(true), 0);

        User owner = new User();
        owner.setId(1);

        Item existingItem = new Item();
        existingItem.setId(itemId);
        existingItem.setName("Original Item");
        existingItem.setDescription("Original Description");
        existingItem.setAvailable(false);
        existingItem.setOwner(owner);

        when(itemRepository.getById(itemId)).thenReturn(existingItem);

        assertThrows(AccessToItemDeniedException.class, () -> itemService.updateItem(itemId, itemDto, userId));
    }
}