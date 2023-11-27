package ru.practicum.shareit.item.comment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private int id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
