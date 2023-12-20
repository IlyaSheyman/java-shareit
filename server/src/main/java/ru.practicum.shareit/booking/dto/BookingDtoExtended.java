package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDtoSmall;
import ru.practicum.shareit.user.dto.UserDtoSmall;

import java.time.LocalDateTime;

@Data
public class BookingDtoExtended {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private ItemDtoSmall item;
    private UserDtoSmall booker;

    public BookingDtoExtended(int id,
                              LocalDateTime start,
                              LocalDateTime end,
                              Status status,
                              ItemDtoSmall item,
                              UserDtoSmall booker) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
        this.item = item;
        this.booker = booker;
    }
}

