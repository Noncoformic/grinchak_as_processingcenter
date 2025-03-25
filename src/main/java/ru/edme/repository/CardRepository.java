package ru.edme.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.edme.model.Card;

public interface CardRepository extends ListCrudRepository<Card, Long> {
    // можно добавить методы, если нужно (например, findByCardNumber)
}
