package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AccessToItemDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Slf4j
public class InMemoryItemStorage implements ItemStorage {
    private HashMap<Integer, Item> items;
    private int seq = 0;

    @Autowired
    public InMemoryItemStorage(InMemoryUserStorage userStorage) {
        this.items = new HashMap<>();
    }

    @Override
    public Item add(ItemDto itemDto, Item item, int userId) {
        if (itemDto.getName().isEmpty() || itemDto.getDescription() == null || !itemDto.getAvailable().isPresent()) {
            throw new ValidationException("Ошибка валидации вещи");
        } else {
            seq++;
            item.setId(seq);
            item.setDescription(itemDto.getDescription());
            item.setName(itemDto.getName());
            item.setAvailable(itemDto.getAvailable().get());
            item.setRequest(new ItemRequest());

            items.put(seq, item);

            return item;
        }
    }

    @Override
    public Item update(int id, ItemDto item, int userId) {
        if (items.get(id).getOwner().getId() == userId) {
            if (items.containsKey(id)) {
                Item previous = items.remove(id);

                if (item.getName() != null && item.getName() != previous.getName()) {
                    previous.setName(item.getName());
                }

                if (item.getDescription() != null && item.getDescription() != previous.getDescription()) {
                    previous.setDescription(item.getDescription());
                }

                if (item.getAvailable().isPresent() && item.getAvailable().get() != previous.isAvailable()) {
                    previous.setAvailable(item.getAvailable().get());
                }

                items.put(id, previous);

                return previous;
            } else {
                throw new NotFoundException("Пользователь с id " + id + " не найден");
            }
        } else {
            throw new AccessToItemDeniedException("Обновить вещь может только владелец");
        }
    }

    @Override
    public Item delete(int id, int userId) {
        if (userId == items.get(id).getOwner().getId()) {
            if (items.containsKey(id)) {
                return items.remove(id);
            } else {
                throw new NotFoundException("Вещь с id " + id + " не найдена");
            }
        } else {
            throw new RuntimeException("Удалить вещь может только её владелец");
        }
    }

    @Override
    public Item get(int id, int userId) {
        if (userId >= 0) {
            if (items.containsKey(id)) {
                return items.get(id);
            } else {
                throw new NotFoundException("Вещь с id " + id + " не найдена");
            }
        } else {
            throw new NotFoundException("Пользователя с id " + userId + " не может существовать");
        }
    }

    @Override
    public List<Item> getAll(int userId) {
        List<Item> itemValues = new ArrayList<>();

        for (Item i: items.values()) {
            if (i.getOwner().getId() == userId) {
                itemValues.add(i);
            }
        }
        return itemValues;
    }

    @Override
    public List<Item> search(String name) {
        List<Item> result = new ArrayList<>();

        if (!name.isEmpty()) {
            for (Item i : items.values()) {
                if ((i.getName().toLowerCase().contains(name.toLowerCase())
                        || i.getDescription().toLowerCase().contains(name.toLowerCase()))
                        && i.isAvailable()) {
                    result.add(i);
                }
            }
        }
        return result;
    }
}