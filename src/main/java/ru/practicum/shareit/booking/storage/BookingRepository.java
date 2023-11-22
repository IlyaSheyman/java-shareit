package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByBooker_Id(int bookerId);

    List<Booking> findByBooker_IdAndStartIsBeforeAndEndIsAfter(int bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByBooker_IdAndEndIsBefore(int bookerId, LocalDateTime end);

    List<Booking> findByBooker_IdAndStartIsAfter(int bookerId, LocalDateTime end);

    List<Booking> findByBooker_IdAndStatusIs(int bookerId, Status status);
}
