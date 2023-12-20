package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CommentRequestDto {
    @NotEmpty
    private String text;
}
