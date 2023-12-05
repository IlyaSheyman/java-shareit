package ru.practicum.shareit.request;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Service("ItemRequestServiceDbImpl")
public class ItemRequestServiceDbImpl implements ItemRequestService {

    @Override
    public ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto) {
        return null;
    }
}
