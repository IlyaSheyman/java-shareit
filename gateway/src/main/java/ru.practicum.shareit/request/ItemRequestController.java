package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.RequestRequestDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Slf4j
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    ItemRequestClient requestClient;

    public ItemRequestController(ItemRequestClient requestClient) {
        this.requestClient = requestClient;
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestBody RequestRequestDto request,
                                                 @RequestHeader("X-Sharer-User-Id") @PositiveOrZero int userId) {
        log.info("Получен запрос на добавление запроса на вещь.");
        return requestClient.addItemRequest(request, userId);
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<Object> getRequestsByRequestor(@RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return requestClient.getRequests(userId);
    }

    @ResponseBody
    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                                 @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                                 @RequestParam(value = "size", defaultValue = "20") @Min(1) @Positive int size) {
        return requestClient.getAllRequests(userId, from, size);
    }

    @ResponseBody
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getOneRequest(@RequestHeader(value = "X-Sharer-User-Id") @PositiveOrZero int userId,
                                                @PathVariable @PositiveOrZero int requestId) {
        return requestClient.getOneRequest(userId, requestId);
    }
}