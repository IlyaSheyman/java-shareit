package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;

public class ItemRequestMapper {

    public ItemRequest fromRequestDto(ItemRequestDto requestDto) {
        return new ItemRequest(
                requestDto.getId(),
                requestDto.getDescription(),
                new User(),
                requestDto.getCreated()
        );
    }

    public ItemRequestDto toRequestDto(ItemRequest request) {
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                request.getCreated()
        );
    }

    public ItemRequestDtoWithItems toRequestDtoWithItems(ItemRequest request) {
        return new ItemRequestDtoWithItems(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                new ArrayList<>()
        );
    }
}
