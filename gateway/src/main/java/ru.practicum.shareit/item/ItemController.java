package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import ru.practicum.shareit.item.comment.dto.CommentDtoTextOnly;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping("/items")
@Validated
public class ItemController {

    private final ItemClient itemClient;

    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> addItem(@Valid @RequestBody ItemRequestDto item,
                                          @RequestHeader("X-Sharer-User-Id") @PositiveOrZero int userId) {
        log.info("Получен запрос на добавление вещи.");
        return itemClient.addItem(item, userId);
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@Valid @RequestBody ItemRequestDto item,
                              @PathVariable @PositiveOrZero int id,
                              @RequestHeader("X-Sharer-User-Id") @PositiveOrZero int userId) {
        log.info("Получен запрос на обновление вещи.");
        return itemClient.updateItem(id, item, userId);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@PathVariable @PositiveOrZero int id,
                                               @RequestHeader("X-Sharer-User-Id") @PositiveOrZero int userId) {
        log.info("Получен запрос на получение вещи с ID - {}.", id);
        return itemClient.getItem(id, userId);
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") @PositiveOrZero int userId,
                                                  @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                                  @RequestParam(value = "size", defaultValue = "20") @Min(0) Integer size) {
        return itemClient.getItems(userId, from, size);
    }

    @ResponseBody
    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(value = "X-Sharer-User-Id", required = false) @PositiveOrZero int userId,
                                @RequestParam String text,
                                @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                @RequestParam(value = "size", defaultValue = "20") @Min(0) int size) {
        return itemClient.search(userId, text, from, size);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(value = "X-Sharer-User-Id") @PositiveOrZero int userId,
                                 @PathVariable @PositiveOrZero int itemId,
                                 @RequestBody CommentRequestDto comment) {
        return itemClient.addComment(userId, itemId, comment);
    }
}