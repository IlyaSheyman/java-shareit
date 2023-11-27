package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDtoTextOnly;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, int userId);

    ItemDto updateItem(int id, ItemDto item, int userID);

    ItemDtoWithBookings getItem(int id, int userId);

    ItemDto deleteItem(int id, int userId);

    List<ItemDtoWithBookings> getItems(int userId);

    List<ItemDto> search(String text);

    CommentDto addComment(int userId, int itemId, CommentDtoTextOnly comment);
}