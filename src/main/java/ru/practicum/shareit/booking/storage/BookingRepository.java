package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    List<Booking> findByItem_Id(int itemId);

    List<Booking> findByItem_IdAndStartIsBeforeAndEndIsAfter(int bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByItem_IdAndEndIsBefore(int bookerId, LocalDateTime end);

    List<Booking> findByItem_IdAndStartIsAfter(int bookerId, LocalDateTime end);

    List<Booking> findByItem_IdAndStatusIs(int bookerId, Status status);

    @Query("SELECT b FROM Booking b WHERE b.item.id = ?1 AND b.end <= CURRENT_TIMESTAMP ORDER BY b.end DESC")
    List<Booking> findLastBookingByItemId(int itemId);

    @Query("SELECT b FROM Booking b WHERE b.item.id = ?1 AND b.start >= CURRENT_TIMESTAMP ORDER BY b.start  ASC")
    List<Booking> findNextBookingByItemId(int itemId);
}