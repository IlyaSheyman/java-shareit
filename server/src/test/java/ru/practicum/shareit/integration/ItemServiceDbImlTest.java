package ru.practicum.shareit.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDtoBookingsComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceDbImpl;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("Интеграционные тесты для ItemServiceDbImpl")
public class ItemServiceDbImlTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private ItemServiceDbImpl itemService;

    @Test
    void testGetItems() {

        MockitoAnnotations.initMocks(this);

        int userId = 1;
        int from = 0;
        int size = 5;

        List<Item> mockItems = new ArrayList<>();
        User mockUser = new User();
        mockUser.setId(userId);

        Item item1 = new Item();
        item1.setId(1);
        item1.setOwner(mockUser);

        Item item2 = new Item();
        item2.setId(2);
        item2.setOwner(mockUser);

        mockItems.add(item1);
        mockItems.add(item2);

        Page<Item> mockItemPage = new PageImpl<>(mockItems);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRepository.findAllWithOrderBy(PageRequest.of(from / size, size))).thenReturn(mockItemPage);
        when(bookingRepository.findLastBookingByItemId(1)).thenReturn(new ArrayList<>());
        when(bookingRepository.findNextBookingByItemId(1)).thenReturn(new ArrayList<>());
        when(bookingRepository.findLastBookingByItemId(2)).thenReturn(new ArrayList<>());
        when(bookingRepository.findNextBookingByItemId(2)).thenReturn(new ArrayList<>());


        List<ItemDtoBookingsComments> result = itemService.getItems(userId, from, size);

        assertEquals(2, result.size());
    }
}