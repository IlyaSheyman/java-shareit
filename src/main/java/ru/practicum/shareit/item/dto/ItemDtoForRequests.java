package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemDtoForRequests {
    private int id;
    private String description;
    private boolean isAvailable;

    public ItemDtoForRequests(int id, String description, boolean isAvailable) {
        this.id = id;
        this.description = description;
        this.isAvailable = isAvailable;
    }
}
