package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public class ItemMapper {

    public Item fromItemDto(ItemDto itemDto, User user) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable().get(),
                user,
                new ItemRequest()
        );
    }
/** В этом методе надо будет вернуть строку item.getRequest() != null ? item.getRequest().getId() : null,
 * когда буду работать в ветке add-requests **/

    public ItemDto toItemDto(Item item) {
       return new ItemDto(
               item.getId(),
               item.getName(),
               item.getDescription(),
               Optional.of(item.isAvailable()),
                0
       );
   }
}