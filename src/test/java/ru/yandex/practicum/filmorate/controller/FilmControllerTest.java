package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FilmController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FilmService filmService;

    @Test
    void getAllFilms_thenResponseStatusOk_WithFilmsCollectionInBody() throws Exception {
        Film filmToCreate = new Film("TestMovie", "TestDescription",
                LocalDate.of(2020, 1, 1), 120);
        filmToCreate.setId(1);
        List<Film> expectedFilms = List.of(filmToCreate);
        when(filmService.getAllFilms()).thenReturn(expectedFilms);

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToCreate)));

        String response = mockMvc.perform(get("/films")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(filmService).getAllFilms();
        assertEquals(objectMapper.writeValueAsString(expectedFilms), response);
    }

    @Test
    void createFilm_whenInvokedWithValidFilm_thenResponseStatusOk_WithCreatedFilmInBody() throws Exception {
        Film filmToCreate = new Film("TestMovie", "TestDescription",
                LocalDate.of(2020, 1, 1), 120);
        filmToCreate.setId(1);
        when(filmService.createFilm(filmToCreate)).thenReturn(filmToCreate);

        String response = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(filmService).createFilm(filmToCreate);
        assertEquals(objectMapper.writeValueAsString(filmToCreate), response);
    }

    @Test
    void createFilm_whenRequestIsEmpty_thenReturnBadRequest() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType("application/json"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createFilm_whenNameIsBlank_thenReturnBadRequest() throws Exception {
        Film filmToCreate = new Film("TestMovie", "TestDescription",
                LocalDate.of(2020, 1, 1), 120);
        filmToCreate.setName("");
        when(filmService.createFilm(filmToCreate)).thenReturn(filmToCreate);

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToCreate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_whenMaximumDescriptionIs200Symbols_thenResponseStatusOk() throws Exception {
        String description = "s";
        Film filmToCreate = new Film("TestMovie", description.repeat(200),
                LocalDate.of(2020, 1, 1), 120);
        filmToCreate.setId(1);

        when(filmService.createFilm(filmToCreate)).thenReturn(filmToCreate);
        String response = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(filmService).createFilm(filmToCreate);
        assertEquals(objectMapper.writeValueAsString(filmToCreate), response);
    }

    @Test
    void createFilm_whenMaximumDescriptionIsMoreThan200Symbols_thenReturnBadRequest() throws Exception {
        String description = "s";
        Film filmToCreate = new Film("TestMovie", description.repeat(201),
                LocalDate.of(2020, 1, 1), 120);

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToCreate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_whenReleaseDateIs_28_12_1895_thenResponseStatusOk() throws Exception {
        Film filmToCreate = new Film("TestMovie", "Description",
                LocalDate.of(1985, 12, 28), 120);
        filmToCreate.setId(1);
        when(filmService.createFilm(filmToCreate)).thenReturn(filmToCreate);

        String response = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(filmService).createFilm(filmToCreate);

        assertEquals(objectMapper.writeValueAsString(filmToCreate), response);
    }

    @Test
    void createFilm_whenDurationIsNegative_thenReturnBadRequest() throws Exception {
        Film filmToCreate = new Film("TestMovie", "Description",
                LocalDate.of(1999, 12, 27), -1);

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToCreate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateFilm_whenIdIsUnknown_thenReturnBadRequest() throws Exception {
        Film filmToCreate = new Film("TestMovie", "TestDescription",
                LocalDate.of(2020, 1, 1), 120);
        when(filmService.createFilm(filmToCreate)).thenReturn(filmToCreate);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(filmToCreate)));
        filmToCreate.setId(1);

        Film filmToUpdate = new Film("TestMovie", "TestDescription",
                LocalDate.of(2020, 1, 1), 120);
        filmToUpdate.setId(2);
        when(filmService.updateFilm(filmToUpdate)).thenThrow(new EntityNotFoundException("Фильм с id 2 не найден"));

        mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToUpdate)))
                .andExpect(status().isNotFound());

        verify(filmService).updateFilm(filmToUpdate);
    }

    @Test
    void updateFilm_whenInvokedWithValidFilm_thenResponseStatusOk_WithCreatedFilmInBody() throws Exception {
        Film filmToCreate = new Film("TestMovie", "TestDescription",
                LocalDate.of(2020, 1, 1), 120);
        when(filmService.createFilm(filmToCreate)).thenReturn(filmToCreate);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(filmToCreate)));

        filmToCreate.setName("TestMovieUpdated");
        filmToCreate.setId(1);
        when(filmService.updateFilm(filmToCreate)).thenReturn(filmToCreate);

        String response = mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(filmToCreate), response);
    }

    @Test
    void updateFilm_whenRequestIsEmpty_thenReturnBadRequest() throws Exception {
        Film filmToCreate = new Film("TestMovie", "TestDescription",
                LocalDate.of(2020, 1, 1), 120);

        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(filmToCreate)));

        mockMvc.perform(put("/films")
                        .contentType("application/json"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateFilm_whenNameIsBlank_thenReturnBadRequest() throws Exception {
        Film filmToCreate = new Film("TestMovie", "TestDescription",
                LocalDate.of(2020, 1, 1), 120);

        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(filmToCreate)));

        filmToCreate.setName("");

        mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToCreate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateFilm_whenMaximumDescriptionIs200Symbols_thenResponseStatusOk() throws Exception {
        Film filmToCreate = new Film("TestMovie", "TestDescription",
                LocalDate.of(2020, 1, 1), 120);
        when(filmService.updateFilm(filmToCreate)).thenReturn(filmToCreate);

        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(filmToCreate)));

        String description = "s";
        filmToCreate.setDescription(description.repeat(200));
        filmToCreate.setId(1);

        String response = mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(filmService).updateFilm(filmToCreate);
        assertEquals(objectMapper.writeValueAsString(filmToCreate), response);
    }

    @Test
    void updateFilm_whenMaximumDescriptionIsMoreThan200Symbols_thenReturnBadRequest() throws Exception {
        Film filmToCreate = new Film("TestMovie", "TestDescription",
                LocalDate.of(2020, 1, 1), 120);

        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(filmToCreate)));

        String description = "s";
        filmToCreate.setDescription(description.repeat(201));
        filmToCreate.setId(1);

        mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToCreate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateFilm_whenReleaseDateIs_28_12_1895_thenResponseStatusOk() throws Exception {
        Film filmToCreate = new Film("TestMovie", "TestDescription",
                LocalDate.of(2020, 1, 1), 120);
        when(filmService.updateFilm(filmToCreate)).thenReturn(filmToCreate);

        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(filmToCreate)));

        filmToCreate.setReleaseDate(LocalDate.of(1985, 12, 28));
        filmToCreate.setId(1);

        String response = mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(filmService).updateFilm(filmToCreate);
        assertEquals(objectMapper.writeValueAsString(filmToCreate), response);
    }

    @Test
    void updateFilm_whenDurationIsNegative_thenReturnBadRequest() throws Exception {
        Film filmToCreate = new Film("TestMovie", "TestDescription",
                LocalDate.of(2020, 1, 1), 120);

        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(filmToCreate)));

        filmToCreate.setDuration(-1);
        filmToCreate.setId(1);

        mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmToCreate)))
                .andExpect(status().isBadRequest());
    }
}