package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item add(ItemDto item, int userId);

    Item update(int id, ItemDto item, int userId);

    Item delete(int id);

    Item get(int id, int userId);

    List<Item> getAll(int userId);

    List<Item> search(String name);
}
