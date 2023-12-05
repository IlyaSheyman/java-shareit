package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-item-requests.
 */

@Data
//@Entity
//@Table(name = "requests")
public class ItemRequest {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @Column(name = "description")
    private String description;

//    @ManyToOne
    private User requestor;;
}

