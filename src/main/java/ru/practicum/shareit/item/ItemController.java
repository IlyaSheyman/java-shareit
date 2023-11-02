package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @ResponseBody
    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto item,
                           @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Получен запрос на добавление вещи.");
        return itemService.addItem(item, userId);
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public ItemDto updateItem(@Valid @RequestBody Item item, @PathVariable int id) {
        log.info("Получен запрос на обновление вещи.");
        return itemService.updateItem(id, item);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable int id) {
        log.info("Получен запрос на получение вещи с ID - {}.", id);
        return itemService.getItem(id);
    }

    @ResponseBody
    @GetMapping
    public List<ItemDto> getItems() {
        return itemService.getItems();
    }
}