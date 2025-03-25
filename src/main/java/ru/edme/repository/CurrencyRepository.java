package ru.edme.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.edme.model.Currency;

@Repository
public interface CurrencyRepository extends ListCrudRepository<Currency,Long> {
}
