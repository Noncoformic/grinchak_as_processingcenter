package ru.edme.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.edme.dto.CardDto;
import ru.edme.service.CardService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cards")
@Tag(name = "Карты")
public class CardController {

    private final CardService service;

    @PostMapping
    public CardDto create(@Valid @RequestBody CardDto dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public CardDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public List<CardDto> all() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public CardDto update(@PathVariable Long id,
                          @Valid @RequestBody CardDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}




