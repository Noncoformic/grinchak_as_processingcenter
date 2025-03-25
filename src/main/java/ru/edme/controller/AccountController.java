package ru.edme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edme.model.Account;
import ru.edme.repository.AccountRepository;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Счета", description = "API для управления счетами пользователей") // Добавляем описание для всей группы API
public class AccountController {

    private final AccountRepository repository;

    @Operation(summary = "Создание счета", description = "Добавляет новый счет в систему")
    @PostMapping
    public ResponseEntity<Account> create(@RequestBody Account account) {
        return ResponseEntity.ok(repository.save(account));
    }

    @Operation(summary = "Получение счета по ID", description = "Возвращает счет по идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable("id") Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получение всех счетов", description = "Возвращает список всех счетов")
    @GetMapping
    public List<Account> getAll() {
        return repository.findAll();
    }

    @Operation(summary = "Обновление счета", description = "Обновляет существующий счет по ID")
    @PutMapping("/{id}")
    public ResponseEntity<Account> update(@PathVariable("id") Long id, @RequestBody Account account) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        account.setId(id);
        return ResponseEntity.ok(repository.save(account));
    }

    @Operation(summary = "Удаление счета", description = "Удаляет счет по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
