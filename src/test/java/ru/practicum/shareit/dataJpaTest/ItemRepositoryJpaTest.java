package ru.practicum.shareit.dataJpaTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Тесты для itemRepository")
public class ItemRepositoryJpaTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository requestRepository;


    @Test
    @DisplayName("Тестирует сохранение и поиск всех вещей в БД")
    public void testFindAll() {
        User owner = User.builder().name("User 1").email("user1@yandex.ru").build();
        userRepository.save(owner);

        Item item1 = Item.builder()
                .name("Item 1")
                .description("Description 1")
                .available(true)
                .owner(owner)
                .request(null)
                .build();

        Item item2 = Item.builder()
                .name("Item 2")
                .description("Description 2")
                .available(false)
                .owner(owner)
                .request(null)
                .build();

        itemRepository.saveAll(List.of(item1, item2));

        List<Item> items = itemRepository.findAll();

        assertEquals(2, items.size());
        assertEquals(item1.getName(), items.get(0).getName());
        assertEquals(item1.getDescription(), items.get(0).getDescription());
        assertEquals(item1.getId(), items.get(0).getId());
        assertEquals(item2.getName(), items.get(1).getName());
        assertEquals(item2.getDescription(), items.get(1).getDescription());
        assertEquals(item2.getId(), items.get(1).getId());
    }

    @Test
    public void testFindByRequest_Id() {
        User requestor = User.builder().name("User 1").email("user1@yandex.ru").build();
        userRepository.save(requestor);

        ItemRequest request = ItemRequest.builder()
                .created(LocalDateTime.now())
                .description("Need item 1")
                .requestor(requestor).build();

        requestRepository.save(request);

        Item item1 = Item.builder()
                .name("Item 1")
                .description("Description 1")
                .available(true)
                .request(request).build();

        itemRepository.save(item1);

        List<Item> itemsByRequestId = itemRepository.findByRequest_Id(1);

        assertEquals(1, itemsByRequestId.size());
        assertEquals(item1.getName(), itemsByRequestId.get(0).getName());
        assertEquals(item1.getDescription(), itemsByRequestId.get(0).getDescription());
        assertEquals(item1.getId(), itemsByRequestId.get(0).getId());
    }
}