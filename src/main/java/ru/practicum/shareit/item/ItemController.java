package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDtoTextOnly;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingsComments;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(@Qualifier("ItemServiceDbImpl") ItemService itemService) {
        this.itemService = itemService;
    }

    @ResponseBody
    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto item,
                           @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Получен запрос на добавление вещи.");
        return itemService.addItem(item, userId);
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public ItemDto updateItem(@Valid @RequestBody ItemDto item,
                              @PathVariable int id,
                              @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Получен запрос на обновление вещи.");
        return itemService.updateItem(id, item, userId);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ItemDtoBookingsComments getItemById(@PathVariable int id,
                                               @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Получен запрос на получение вещи с ID - {}.", id);
        return itemService.getItem(id, userId);
    }

    @ResponseBody
    @GetMapping
    public List<ItemDtoBookingsComments> getItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getItems(userId);
    }

    @ResponseBody
    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(value = "X-Sharer-User-Id", required = false) int userId,
                                @RequestParam String text) {
        return itemService.search(text);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                 @PathVariable int itemId,
                                 @RequestBody CommentDtoTextOnly comment) {
        return itemService.addComment(userId, itemId, comment);
    }
}