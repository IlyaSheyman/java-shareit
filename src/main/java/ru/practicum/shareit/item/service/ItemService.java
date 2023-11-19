package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, int userId);

    ItemDto updateItem(int id, ItemDto item, int userID);

    ItemDto getItem(int id, int userId);

    ItemDto deleteItem(int id, int userId);

    List<ItemDto> getItems(int userId);

    List<ItemDto> search(String text);
}