package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private Optional<Boolean> available;
    private int requestId;

    public ItemDto(int id, String name, String description, Optional<Boolean> available, int requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }
}