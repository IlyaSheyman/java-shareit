package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ItemService {

    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemService(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
        this.itemMapper = new ItemMapper();
    }

    public ItemDto addItem(ItemDto item, int userId) {
        return itemMapper.toItemDto(itemStorage.add(item, userId));
    }

    public ItemDto updateItem(int id, Item item) {
        return itemMapper.toItemDto(itemStorage.update(id, item));
    }

    public ItemDto getItem(int id) {
        return itemMapper.toItemDto(itemStorage.get(id));
    }

    public List<ItemDto> getItems() {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item i : itemStorage.getAll()) {
            itemsDto.add(itemMapper.toItemDto(i));
        }
        return itemsDto;
    }
}