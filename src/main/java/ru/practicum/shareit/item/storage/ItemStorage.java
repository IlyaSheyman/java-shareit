package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item add(ItemDto item, int userId);

    Item update(int id, Item item);

    Item delete(int id);

    Item get(int id);

    List<Item> getAll();
}
