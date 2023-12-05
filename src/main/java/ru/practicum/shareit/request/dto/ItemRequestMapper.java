package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

public class ItemRequestMapper {

    public ItemRequest fromRequestDto(ItemRequestDto requestDto) {
        return new ItemRequest(
                requestDto.getId(),
                requestDto.getDescription(),
                new User()
        );
    }
//    public ItemDto toItemDto(Item item) {
//        return new ItemDto(
//                item.getId(),
//                item.getName(),
//                item.getDescription(),
//                Optional.of(item.isAvailable()),
//                item.getRequest() != null ? item.getRequest().getId() : null
//        );
//    }
}
