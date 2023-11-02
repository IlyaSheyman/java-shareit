package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Slf4j
public class InMemoryItemStorage implements ItemStorage {
    private HashMap<Integer, Item> items;
    private int seq = 0;
    private UserStorage userStorage;

    @Autowired
    public InMemoryItemStorage(InMemoryUserStorage userStorage) {
        this.items = new HashMap<>();
        this.userStorage = userStorage;
    }

    @Override
    public Item add(ItemDto itemDto, int userId) {
        if (itemDto.getName().isEmpty() || itemDto.getDescription() == null || !itemDto.isAvailable()) {
            throw new ValidationException("Ошибка валидации вещи");
        } else {
            Item item = new Item();

            seq++;
            item.setId(seq);
            item.setDescription(itemDto.getDescription());
            item.setName(itemDto.getName());
            item.setAvailable(itemDto.isAvailable());
            item.setOwner(userStorage.get(userId).getId());
            item.setRequest(new ItemRequest());

            items.put(seq, item);

            return item;
        }
    }

    @Override
    public Item update(int id, Item item) {
        if (items.containsKey(id)) {
            Item previous = items.remove(id);

            if (item.getName() != null && item.getName() != previous.getName()) {
                previous.setName(item.getName());
            }

            if (previous.getDescription() != null && item.getDescription() != previous.getDescription()) {
                previous.setDescription(item.getDescription());
            }

            items.put(id, previous);

            return previous;
        } else {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
    }

    @Override
    public Item delete(int id) {
        if (items.containsKey(id)) {
            return items.remove(id);
        } else {
            throw new NotFoundException("Вещь с id " + id + " не найдена");
        }
    }

    @Override
    public Item get(int id) {
        if (items.containsKey(id)) {
            return items.get(id);
        } else {
            throw new NotFoundException("Вещь с id " + id + " не найдена");
        }
    }

    @Override
    public List<Item> getAll() {
        List<Item> itemValues = new ArrayList<>();
        for (Item i: items.values()) {
            itemValues.add(i);
        }
        return itemValues;
    }
}