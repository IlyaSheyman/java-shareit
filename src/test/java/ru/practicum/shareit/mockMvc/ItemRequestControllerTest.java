package ru.practicum.shareit.mockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ItemRequestControllerTest {

    @Mock
    private ItemRequestService requestService;

    @InjectMocks
    private ItemRequestController requestController;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    public ItemRequestControllerTest() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(requestController).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void testAddItemRequest() throws Exception {
        int userId = 1;

        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setId(1);
        requestDto.setDescription("Please share this item.");

        when(requestService.addItemRequest(any(ItemRequestDto.class), any(int.class))).thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(requestDto.getId()))
                .andExpect(jsonPath("$.description").value(requestDto.getDescription()));
    }

    @Test
    void testGetRequestsByRequestor() throws Exception {
        int userId = 1;

        List<ItemRequestDtoWithItems> requests = Arrays.asList(
                new ItemRequestDtoWithItems(1, "Good",
                        LocalDateTime.now(), new ArrayList<>()),
                new ItemRequestDtoWithItems(2, "Very good",
                        LocalDateTime.now().minusDays(3), new ArrayList<>())
        );

        when(requestService.getRequests(userId)).thenReturn(requests);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].items").value(requests.get(0).getItems()))
                .andExpect(jsonPath("$[0].description").value(requests.get(0).getDescription()))
                .andExpect(jsonPath("$[1].description").value(requests.get(1).getDescription()))
                .andExpect(jsonPath("$[1].id").value(requests.get(1).getId()));
    }

    @Test
    void testGetAllRequests() throws Exception {
        int userId = 1;
        int from = 0;
        int size = 5;

        List<ItemRequestDtoWithItems> requests = Arrays.asList(
                new ItemRequestDtoWithItems(1, "Good",
                        LocalDateTime.now(), new ArrayList<>()),
                new ItemRequestDtoWithItems(2, "Very good",
                        LocalDateTime.now().minusDays(3), new ArrayList<>())
        );

        when(requestService.getAllRequests(userId, from, size)).thenReturn(requests);

        mockMvc.perform(get("/requests/all")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].items").value(requests.get(0).getItems()))
                .andExpect(jsonPath("$[0].description").value(requests.get(0).getDescription()))
                .andExpect(jsonPath("$[1].description").value(requests.get(1).getDescription()))
                .andExpect(jsonPath("$[1].id").value(requests.get(1).getId()));
    }

    @Test
    void testGetOneRequest() throws Exception {
        int userId = 1;
        int requestId = 1;

        ItemRequestDtoWithItems request = new ItemRequestDtoWithItems(1,
                "Good",
                LocalDateTime.now(),
                new ArrayList<>());

        when(requestService.getOneRequest(userId, requestId)).thenReturn(request);

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(request.getId()))
                .andExpect(jsonPath("$.description").value(request.getDescription()));
    }
}