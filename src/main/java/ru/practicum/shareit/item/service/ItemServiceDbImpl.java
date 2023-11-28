package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessToItemDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDtoTextOnly;
import ru.practicum.shareit.item.comment.dto.CommentMapper;
import ru.practicum.shareit.item.comment.storage.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingsComments;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service("ItemServiceDbImpl")
public class ItemServiceDbImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public ItemServiceDbImpl(ItemRepository itemRepository,
                             UserRepository userRepository,
                             BookingRepository bookingRepository,
                             CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.itemMapper = new ItemMapper();
        this.bookingMapper = new BookingMapper();
        this.commentMapper = new CommentMapper();
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, int userId) {
        if (itemDto.getName().isEmpty()) {
            throw new ValidationException("Поле с названием вещи должно быть заполнено");
        } else if (itemDto.getDescription() == null) {
            throw new ValidationException("Поле с описанием вещи должно быть заполнено");
        } else if (!itemDto.getAvailable().isPresent()) {
            throw new ValidationException("Поле с доступностью вещи должно быть заполнено");
        } else if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else {
            User owner = userRepository.getById(userId);
            Item item = itemMapper.fromItemDto(itemDto, owner);
            itemRepository.save(item);

            return itemMapper.toItemDto(item);
        }
    }

    @Override
    public ItemDto updateItem(int id, ItemDto item, int userId) {
        if (itemRepository.getById(id).getOwner().getId() == userId) {
            Item previous = itemRepository.getById(id);

            if (item.getName() != null && item.getName() != previous.getName()) {
                previous.setName(item.getName());
            }

            if (item.getDescription() != null && item.getDescription() != previous.getDescription()) {
                previous.setDescription(item.getDescription());
            }

            if (item.getAvailable().isPresent() && item.getAvailable().get() != previous.isAvailable()) {
                previous.setAvailable(item.getAvailable().get());
            }
            itemRepository.save(previous);

            return itemMapper.toItemDto(previous);
        } else {
            throw new AccessToItemDeniedException("Изменять вещь может только владелец");
        }
    }


    @Override
    public ItemDtoBookingsComments getItem(int id, int userId) {
        if (userId >= 0) {
            if (itemRepository.existsById(id)) {
                Item item = itemRepository.getById(id);
                ItemDtoBookingsComments itemDto = itemMapper.toItemDtoWithBookings(itemRepository.getById(id));

                if (item.getOwner().getId() == userId) {
                    setLastAndNextBookings(id, itemDto);
                }

                itemDto.setComments(
                        Optional.ofNullable(commentRepository.findByItem_Id(id))
                                .map(list -> list.stream()
                                        .map(commentMapper::toCommentDto)
                                        .collect(Collectors.toList()))
                                .orElse(Collections.emptyList())
                );

                return itemDto;
            } else {
                throw new NotFoundException("Вещь с id " + id + " не найдена");
            }
        } else {
            throw new NotFoundException("Пользователя с id " + userId + " не может существовать");
        }
    }

    private void setLastAndNextBookings(int itemId, ItemDtoBookingsComments itemDto) {
        List<Booking> lastBookings = bookingRepository.findLastBookingByItemId(itemId);
        List<Booking> nextBookings = bookingRepository.findNextBookingByItemId(itemId);

        if (!lastBookings.isEmpty()) {
            Booking last = lastBookings.get(0);

            if (last.getStatus().equals(Status.APPROVED)) {
                itemDto.setLastBooking(bookingMapper.toBookingDtoForGetItem(last));
            }
        }

        if (!nextBookings.isEmpty()) {
            Booking next = nextBookings.get(0);

            if (next.getStatus().equals(Status.APPROVED)) {
                itemDto.setNextBooking(bookingMapper.toBookingDtoForGetItem(next));
            }
        }
    }

    @Override
    public List<ItemDtoBookingsComments> getItems(int userId) {
        List<ItemDtoBookingsComments> itemsDto = new ArrayList<>();
        for (Item i : itemRepository.findAll()) {
            if (i.getOwner().getId() == userId) {
                ItemDtoBookingsComments itemDto = itemMapper.toItemDtoWithBookings(i);
                setLastAndNextBookings(i.getId(), itemDto);
                itemsDto.add(itemDto);
            }
        }
        return itemsDto;
    }

    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> result = new ArrayList<>();

        if (!text.isEmpty()) {
            for (Item i : itemRepository.findAll()) {
                if ((i.getName().toLowerCase().contains(text.toLowerCase())
                        || i.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && i.isAvailable()) {
                    result.add(itemMapper.toItemDto(i));
                }
            }
        }
        return result;
    }

    @Override
    public ItemDto deleteItem(int id, int userId) {
        Item itemToDelete = itemRepository.getById(id);

        if (itemRepository.existsById(id) && userId == itemToDelete.getOwner().getId()) {
            itemRepository.deleteById(id);
        }

        return itemMapper.toItemDto(itemToDelete);
    }

    @Override
    public CommentDto addComment(int userId, int itemId, CommentDtoTextOnly comment) {
        Item item = itemRepository.getById(itemId);
        User user = userRepository.getById(userId);
        String text = comment.getText();

        List<Booking> bookings = bookingRepository.findByItem_Id(itemId);

        if (comment.getText().isEmpty()) {
            throw new ValidationException("Комментарий не может быть пустым");
        }
        if (userId == item.getOwner().getId()) {
            throw new ValidationException("Владелец вещи не может оставить комментарий");
        }

        boolean canAddComment = false;

        for (Booking booking : bookings) {
            if (booking.getItem().getId() == itemId && booking.getBooker().getId() == userId) {
                if (booking.getEnd().isBefore(LocalDateTime.now())) {
                    canAddComment = true;
                    break;
                } else {
                    throw new ValidationException("Бронирование должно быть завершено чтобы оставить комментарий");
                }
            } else {
                throw new ValidationException("Комментарий может оставлять только автор бронирования");
            }
        }

        if (canAddComment) {
            commentRepository.save(commentMapper.fromCommentDtoTextOnly(comment, item, user));
        }
        return commentMapper.toCommentDto(commentRepository.findByTextAndItemId(text, itemId));
    }
}