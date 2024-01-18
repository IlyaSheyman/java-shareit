package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private Optional<Boolean> available;
    private Integer requestId;

    public ItemDto(int id, String name, String description, Optional<Boolean> available, Integer requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }
}
