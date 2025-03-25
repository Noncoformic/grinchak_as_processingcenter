package ru.edme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edme.model.PaymentSystem;
import ru.edme.repository.PaymentSystemRepository;

import java.util.List;

@RestController
@RequestMapping("/api/payment-systems")
@RequiredArgsConstructor
@Tag(name = "Платежные системы", description = "API для управления платежными системами")
public class PaymentSystemController {

    private final PaymentSystemRepository repository;

    @Operation(summary = "Создать платежную систему", description = "Добавляет новую платежную систему")
    @PostMapping
    public ResponseEntity<PaymentSystem> create(@RequestBody PaymentSystem paymentSystem) {
        if (paymentSystem == null || paymentSystem.getPaymentSystemName() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repository.save(paymentSystem));
    }

    @Operation(summary = "Получить платежную систему по ID", description = "Возвращает данные по ID платежной системы")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentSystem> getById(@PathVariable("id") Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить список всех платежных систем", description = "Возвращает список всех платежных систем")
    @GetMapping
    public ResponseEntity<List<PaymentSystem>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Обновить платежную систему", description = "Обновляет данные платежной системы по ID")
    @PutMapping("/{id}")
    public ResponseEntity<PaymentSystem> update(@PathVariable("id") Long id, @RequestBody PaymentSystem updated) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        updated.setId(id);
        return ResponseEntity.ok(repository.save(updated));
    }

    @Operation(summary = "Удалить платежную систему", description = "Удаляет платежную систему по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
