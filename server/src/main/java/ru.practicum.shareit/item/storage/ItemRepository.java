package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByOwner_Id(int ownerId);

    List<Item> findByRequest_Id(int requestId);

    @Query("select i from Item i order by i.id")
    Page<Item> findAllWithOrderBy(Pageable pageable);
}