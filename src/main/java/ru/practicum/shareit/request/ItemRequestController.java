package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(path = "/requests")
public class ItemRequestController {

    ItemRequestService itemRequestService;

    public ItemRequestController(@Qualifier("ItemRequestServiceDbImpl") ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @ResponseBody
    @PostMapping
    public ItemRequestDto addItemRequest(@Valid @RequestBody ItemRequestDto request,
                                  @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Получен запрос на добавление вещи.");
        return null;
    }
}