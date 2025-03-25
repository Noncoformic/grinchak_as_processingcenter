package ru.edme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edme.model.CardStatus;
import ru.edme.repository.CardStatusRepository;

import java.util.List;

@RestController
@RequestMapping("/api/card-statuses")
@RequiredArgsConstructor
@Tag(name = "Статусы карт", description = "API для управления статусами карт")
public class CardStatusController {

    private final CardStatusRepository repository;

    @Operation(summary = "Создать статус карты", description = "Добавляет новый статус карты в систему")
    @PostMapping
    public ResponseEntity<CardStatus> create(@RequestBody CardStatus status) {
        return ResponseEntity.ok(repository.save(status));
    }

    @Operation(summary = "Получить все статусы карт", description = "Возвращает список всех статусов карт")
    @GetMapping
    public ResponseEntity<List<CardStatus>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Получить статус карты по ID", description = "Возвращает статус карты по его идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<CardStatus> getById(@PathVariable("id") Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Обновить статус карты", description = "Обновляет существующий статус карты")
    @PutMapping("/{id}")
    public ResponseEntity<CardStatus> update(@PathVariable("id") Long id, @RequestBody CardStatus updatedStatus) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        updatedStatus.setId(id);
        return ResponseEntity.ok(repository.save(updatedStatus));
    }

    @Operation(summary = "Удалить статус карты", description = "Удаляет статус карты по его идентификатору")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
