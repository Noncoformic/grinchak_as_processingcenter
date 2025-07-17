package ru.edme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.edme.dto.CardStatusDto;
import ru.edme.service.CardStatusService;

@RestController
@RequestMapping("/api/card-statuses")
@RequiredArgsConstructor
@Tag(name = "Статусы карт", description = "API для управления статусами карт")
public class CardStatusController {

    private final CardStatusService service;

    @Operation(summary = "Создать статус карты", description = "Добавляет новый статус карты в систему")
    @PostMapping
    public CardStatusDto create(@Valid @RequestBody CardStatusDto dto) {
        return service.create(dto);
    }


    @Operation(summary = "Получить все статусы карт", description = "Возвращает список всех статусов карт")
    @GetMapping
    public List<CardStatusDto> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Получить статус карты по ID", description = "Возвращает статус карты по его идентификатору")
    @GetMapping("/{id}")
    public CardStatusDto get(@PathVariable Long id) {
        return service.getById(id);
    }


    @Operation(summary = "Обновить статус карты", description = "Обновляет существующий статус карты")
    @PutMapping("/{id}")
    public CardStatusDto update(@PathVariable Long id, @Valid @RequestBody CardStatusDto dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Удалить статус карты", description = "Удаляет статус карты по его идентификатору")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
