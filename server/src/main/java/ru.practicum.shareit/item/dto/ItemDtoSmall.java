package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDtoSmall {
    private int id;
    private String name;

    public ItemDtoSmall(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
