package ru.practicum.shareit.exception;

public class AccessToItemDeniedException extends RuntimeException  {

    public AccessToItemDeniedException(String message) {
        super(message);
    }
}
