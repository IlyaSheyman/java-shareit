package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Setter
public class ItemRequestDto {

    private int id;
    private String description;
    private LocalDateTime created;

    public ItemRequestDto(int id, String description, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.created = created;
    }
}