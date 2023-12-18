package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/requests")
public class ItemRequestController {

    ItemRequestService requestService;

    public ItemRequestController(@Qualifier("ItemRequestServiceDbImpl") ItemRequestService itemRequestService) {
        this.requestService = itemRequestService;
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestBody ItemRequestDto request,
                                                 @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Получен запрос на добавление запроса на вещь.");
        return requestService.addItemRequest(request, userId);
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<Object> getRequestsByRequestor(@RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return requestService.getRequests(userId);
    }

    @ResponseBody
    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                                        @RequestParam(value = "from") int from,
                                                        @RequestParam(value = "size") int size) {
        return requestService.getAllRequests(userId, from, size);
    }

    @ResponseBody
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getOneRequest(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                                 @PathVariable int requestId) {
        return requestService.getOneRequest(userId, requestId);
    }
}