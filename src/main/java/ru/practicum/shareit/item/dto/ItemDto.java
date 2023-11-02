package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private boolean available;
    private int requestId;

    public ItemDto(int id, String name, String description, boolean available, int requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }
}