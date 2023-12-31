package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDtoTextOnly;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingsComments;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("ItemServiceInMemoryImpl")
public class ItemServiceInMemoryImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;
    private final UserStorage userStorage;

    @Autowired
    public ItemServiceInMemoryImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.itemMapper = new ItemMapper();
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, int userId) {
        Item item = new Item();
        item.setOwner(userStorage.get(userId)); // вынесено в сервис, так как в ItemStorage
        // нельзя импортировать другой репозиторий (UserStorage)
        return itemMapper.toItemDto(itemStorage.add(itemDto, item, userId));
    }

    @Override
    public ItemDto updateItem(int id, ItemDto item, int userID) {
        return itemMapper.toItemDto(itemStorage.update(id, item, userID));
    }

    @Override
    public ItemDtoBookingsComments getItem(int id, int userId) {
        return itemMapper.toItemDtoWithBookings(itemStorage.get(id, userId));
    }

    @Override
    public List<ItemDtoBookingsComments> getItems(int userId, int from, int size) {
        List<ItemDtoBookingsComments> itemsDto = new ArrayList<>();
        for (Item i : itemStorage.getAll(userId)) {
            itemsDto.add(itemMapper.toItemDtoWithBookings(i));
        }
        return itemsDto;
    }

    @Override
    public List<ItemDto> search(int userId, String text, int from, int size) {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item i : itemStorage.search(text)) {
            itemsDto.add(itemMapper.toItemDto(i));
        }
        return itemsDto;
    }

    @Override
    public ItemDto deleteItem(int id, int userId) {
        return itemMapper.toItemDto(itemStorage.delete(id, userId));
    }

    @Override
    public CommentDto addComment(int userId, int itemId, CommentDtoTextOnly comment) {
        return null;
    }
}