package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemDtoForRequests {
    private int id;
    private String name;
    private int requestId;
    private String description;
    private boolean isAvailable;

    public ItemDtoForRequests(int id, String name, int requestId, String description, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.requestId = requestId;
        this.description = description;
        this.isAvailable = isAvailable;
    }
}
