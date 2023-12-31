package ru.practicum.shareit.item.comment.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Comment findByTextAndItemId(String text, int itemId);

    List<Comment> findByItem_Id(int itemId);
}