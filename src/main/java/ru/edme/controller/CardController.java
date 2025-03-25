package ru.edme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edme.model.Card;
import ru.edme.service.CardService;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Tag(name = "Карты", description = "API для управления банковскими картами") // Группировка API в Swagger
public class CardController {

    private final CardService cardService;

    @Operation(summary = "Добавить новую карту", description = "Создает новую банковскую карту в системе")
    @PostMapping
    public ResponseEntity<Card> addCard(@RequestBody Card card) {
        Card savedCard = cardService.saveCard(card);
        return ResponseEntity.ok(savedCard);
    }

    @Operation(summary = "Получить все карты", description = "Возвращает список всех карт в системе")
    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @Operation(summary = "Получить карту по ID", description = "Возвращает данные карты по ее идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<Card> getCard(@PathVariable("id") Long id) {
        return cardService.getCardById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Обновить карту", description = "Обновляет данные карты по ее ID")
    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable("id") Long id, @RequestBody Card updatedCard) {
        if (!cardService.getCardById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        updatedCard.setId(id);
        return ResponseEntity.ok(cardService.saveCard(updatedCard));
    }

    @Operation(summary = "Удалить карту", description = "Удаляет карту по ее идентификатору")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (!cardService.getCardById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}


