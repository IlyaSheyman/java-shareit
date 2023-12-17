package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, int userId);

    List<ItemRequestDtoWithItems> getRequests(int userId);

    List<ItemRequestDtoWithItems> getAllRequests(int userId, int from, int size);

    ItemRequestDtoWithItems getOneRequest(int userId, int requestId);
}
