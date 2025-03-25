package ru.edme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edme.model.IssuingBank;
import ru.edme.repository.IssuingBankRepository;

import java.util.List;

@RestController
@RequestMapping("/api/issuing-banks")
@RequiredArgsConstructor
@Tag(name = "Эмитенты карт", description = "API для управления банками-эмитентами")
public class IssuingBankController {

    private final IssuingBankRepository issuingBankRepository;

    @Operation(summary = "Создать банк-эмитент", description = "Добавляет новый банк-эмитент")
    @PostMapping
    public ResponseEntity<IssuingBank> create(@RequestBody IssuingBank issuingBank) {
        if (issuingBank == null || issuingBank.getAbbreviatedName() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(issuingBankRepository.save(issuingBank));
    }

    @Operation(summary = "Обновить банк-эмитент", description = "Обновляет информацию о банке по ID")
    @PutMapping("/{id}")
    public ResponseEntity<IssuingBank> update(@PathVariable("id") Long id, @RequestBody IssuingBank issuingBank) {
        if (!issuingBankRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        issuingBank.setId(id);
        return ResponseEntity.ok(issuingBankRepository.save(issuingBank));
    }

    @Operation(summary = "Получить список всех банков-эмитентов", description = "Возвращает список всех банков")
    @GetMapping
    public ResponseEntity<List<IssuingBank>> getAll() {
        return ResponseEntity.ok(issuingBankRepository.findAll());
    }

    @Operation(summary = "Получить банк-эмитент по ID", description = "Возвращает данные о банке по его идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<IssuingBank> getById(@PathVariable("id") Long id) {
        return issuingBankRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Удалить банк-эмитент", description = "Удаляет банк по его идентификатору")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (!issuingBankRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        issuingBankRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

