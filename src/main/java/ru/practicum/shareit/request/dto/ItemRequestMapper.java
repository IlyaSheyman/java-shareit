package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ItemRequestMapper {

    public ItemRequest fromRequestDto(ItemRequestDto requestDto) {
        return new ItemRequest(
                requestDto.getId(),
                requestDto.getDescription(),
                new User()
        );
    }

    public ItemRequestDto toRequestDto(ItemRequest request) {
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                LocalDateTime.now()
        );
    }

    public ItemRequestDtoWithItems toRequestDtoWithItems(ItemRequest request) {
        return new ItemRequestDtoWithItems(
                request.getId(),
                request.getDescription(),
                LocalDateTime.now(),
                new ArrayList<>()
        );
    }
}
