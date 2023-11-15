package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

public class ItemMapper {

   public ItemDto toItemDto(Item item) {
       return new ItemDto(
               item.getId(),
               item.getName(),
               item.getDescription(),
               Optional.of(item.isAvailable()),
               item.getRequest() != null ? item.getRequest().getId() : null
       );
   }
}