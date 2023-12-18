package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDtoForRequests;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ItemRequestDtoWithItems {
    private int id;
    private String description;
    private LocalDateTime created;
    private List<ItemDtoForRequests> items;

    public ItemRequestDtoWithItems(int id, String description, LocalDateTime created, List<ItemDtoForRequests> items) {
        this.id = id;
        this.description = description;
        this.created = created;
        this.items = items;
    }
}