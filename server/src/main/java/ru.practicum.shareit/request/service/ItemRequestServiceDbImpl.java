package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("ItemRequestServiceDbImpl")
public class ItemRequestServiceDbImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper requestMapper;
    private final ItemMapper itemMapper;


    public ItemRequestServiceDbImpl(UserRepository userRepository,
                                    ItemRepository itemRepository,
                                    ItemRequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.requestRepository = requestRepository;
        this.requestMapper = new ItemRequestMapper();
        this.itemMapper = new ItemMapper();
    }

    @Transactional
    @Override
    public ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, int userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else if (itemRequestDto.getDescription() == null || itemRequestDto.getDescription().isEmpty()) {
            throw new ValidationException("Описание запроса не должно быть пустым");
        } else {
            ItemRequest request = requestMapper.fromRequestDto(itemRequestDto);
            request.setRequestor(userRepository.getById(userId));
            request.setCreated(LocalDateTime.now());

            requestRepository.save(request);

            ItemRequestDto requestDto = requestMapper.toRequestDto(
                    requestRepository.findByRequestor_IdAndDescription(
                            userId,
                            itemRequestDto.getDescription()
                    ));

            return requestDto;
        }
    }

    @Override
    public List<ItemRequestDtoWithItems> getRequests(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else {
            List<ItemRequestDtoWithItems> itemRequests = new ArrayList<>();

            for (ItemRequest req : requestRepository.findByRequestor_Id(userId)) {
                ItemRequestDtoWithItems requestWithItems = requestMapper.toRequestDtoWithItems(req);
                requestWithItems.setItems(
                        itemRepository.findByRequest_Id(req.getId())
                                .stream()
                                .map(itemMapper::toItemDtoForRequests)
                                .collect(Collectors.toList())
                );
                itemRequests.add(requestWithItems);
            }

            Collections.sort(itemRequests, Comparator.comparing(ItemRequestDtoWithItems::getCreated).reversed());
            return itemRequests;
        }
    }

    @Override
    public List<ItemRequestDtoWithItems> getAllRequests(int userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else {
            List<ItemRequestDtoWithItems> allRequests = new ArrayList<>();

            for (ItemRequest req : requestRepository.findByRequestor_IdNot(userId,
                    PageRequest.of(from / size,
                            size,
                            Sort.by(Sort.Direction.DESC, "created")))) {

                ItemRequestDtoWithItems requestDtoWithItems = requestMapper.toRequestDtoWithItems(req);
                requestDtoWithItems.setItems(
                        itemRepository.findByRequest_Id(req.getId())
                                .stream()
                                .map(itemMapper::toItemDtoForRequests)
                                .collect(Collectors.toList()));
                allRequests.add(requestDtoWithItems);
            }

            return allRequests;
        }
    }

    @Override
    public ItemRequestDtoWithItems getOneRequest(int userId, int requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else if (requestRepository.existsById(requestId)) {
            ItemRequestDtoWithItems request = requestMapper.toRequestDtoWithItems(requestRepository.getById(requestId));
            request.setItems(
                    itemRepository.findByRequest_Id(requestId)
                            .stream()
                            .map(itemMapper::toItemDtoForRequests)
                            .collect(Collectors.toList()));
            return request;
        } else {
            throw new NotFoundException("Запрос с id " + requestId + " не найден");
        }
    }
}