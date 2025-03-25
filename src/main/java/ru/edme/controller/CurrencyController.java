package ru.edme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edme.model.Currency;
import ru.edme.repository.CurrencyRepository;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
@Tag(name = "Валюты", description = "API для управления валютами")
public class CurrencyController {

    private final CurrencyRepository currencyRepository;

    @Operation(summary = "Создать валюту", description = "Добавляет новую валюту в систему")
    @PostMapping
    public ResponseEntity<Currency> create(@RequestBody Currency currency) {
        if (currency == null || currency.getCurrencyName() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(currencyRepository.save(currency));
    }

    @Operation(summary = "Обновить валюту", description = "Обновляет информацию о валюте по ID")
    @PutMapping("/{id}")
    public ResponseEntity<Currency> update(@PathVariable("id") Long id, @RequestBody Currency currency) {
        if (!currencyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        currency.setId(id);
        return ResponseEntity.ok(currencyRepository.save(currency));
    }

    @Operation(summary = "Получить список всех валют", description = "Возвращает список всех доступных валют")
    @GetMapping
    public ResponseEntity<List<Currency>> getAll() {
        return ResponseEntity.ok(currencyRepository.findAll());
    }

    @Operation(summary = "Получить валюту по ID", description = "Возвращает валюту по её идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<Currency> getById(@PathVariable("id") Long id) {
        return currencyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Удалить валюту", description = "Удаляет валюту по её идентификатору")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (!currencyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        currencyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
