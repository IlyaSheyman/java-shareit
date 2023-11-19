package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

public class ItemServiceDbImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserStorage userStorage;

    public ItemServiceDbImpl(ItemRepository itemRepository, UserStorage userStorage) {
        this.itemRepository = itemRepository;
        this.itemMapper = new ItemMapper();
        this.userStorage = userStorage;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, int userId) {
        return null;
    }

    @Override
    public ItemDto updateItem(int id, ItemDto item, int userId) {
        return null;
    }

    @Override
    public ItemDto getItem(int id, int userId) {
        return null;
    }

    @Override
    public List<ItemDto> getItems(int userId) {
        return null;
    }

    @Override
    public List<ItemDto> search(String text) {
        return null;
    }

    @Override
    public ItemDto deleteItem(int id, int userId) {
        return null;
    }
}
