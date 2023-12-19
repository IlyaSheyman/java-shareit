package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByBooker_Id(int bookerId, Pageable pageable);

    List<Booking> findByBooker_IdAndStartIsBeforeAndEndIsAfter(int bookerId,
                                                               LocalDateTime start,
                                                               LocalDateTime end,
                                                               Pageable pageable);

    List<Booking> findByBooker_IdAndEndIsBefore(int bookerId, LocalDateTime end, Pageable pageable);

    List<Booking> findByBooker_IdAndStartIsAfter(int bookerId, LocalDateTime end, Pageable pageable);

    List<Booking> findByBooker_IdAndStatusIs(int bookerId, Status status, Pageable pageable);

    List<Booking> findByItem_Id(int itemId, Pageable pageable);

    List<Booking> findByItem_Id(int itemId);

    List<Booking> findByItem_IdAndStartIsBeforeAndEndIsAfter(int bookerId,
                                                             LocalDateTime start,
                                                             LocalDateTime end,
                                                             Pageable pageable);

    List<Booking> findByItem_IdAndEndIsBefore(int bookerId,
                                              LocalDateTime end,
                                              Pageable pageable);

    List<Booking> findByItem_IdAndStartIsAfter(int bookerId, LocalDateTime end, Pageable pageable);

    List<Booking> findByItem_IdAndStatusIs(int bookerId, Status status, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND (b.end <= CURRENT_TIMESTAMP OR (b.start < CURRENT_TIMESTAMP AND b.end > CURRENT_TIMESTAMP)) ORDER BY b.end DESC")
    List<Booking> findLastBookingByItemId(@Param("itemId") int itemId);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.start >= CURRENT_TIMESTAMP ORDER BY b.start ASC")
    List<Booking> findNextBookingByItemId(@Param("itemId") int itemId);
}