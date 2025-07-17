package ru.edme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.edme.dto.CurrencyDto;
import ru.edme.service.CurrencyService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
@Tag(name = "Валюты", description = "API для управления валютами")
public class CurrencyController {

    private final CurrencyService service;

    @Operation(summary = "Создать валюту", description = "Добавляет новую валюту в систему")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CurrencyDto create(@RequestBody @Valid CurrencyDto dto) {
        return service.create(dto);
    }

    @Operation(summary = "Обновить валюту", description = "Обновляет информацию о валюте по ID")
    @PutMapping("/{id}")
    public CurrencyDto update(
            @PathVariable Long id,
            @RequestBody @Valid CurrencyDto dto
    ) {
        return service.update(id, dto);
    }

    @Operation(summary = "Получить список всех валют", description = "Возвращает список всех доступных валют")
    @GetMapping
    public List<CurrencyDto> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Получить валюту по ID", description = "Возвращает валюту по её идентификатору")
    @GetMapping("/{id}")
    public CurrencyDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @Operation(summary = "Удалить валюту", description = "Удаляет валюту по её идентификатору")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
