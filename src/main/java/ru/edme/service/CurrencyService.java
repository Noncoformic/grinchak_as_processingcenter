package ru.edme.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.model.Currency;
import ru.edme.repository.CurrencyRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository repository;

    public Currency save(Currency currency) {
        validateNotNull(currency, "Currency must not be null");
        return repository.save(currency);
    }

    public Currency update(Currency currency) {
        validateNotNull(currency, "Currency must not be null");
        return repository.save(currency);
    }

    @Transactional
    public void delete(Long id) {
        validateNotNull(id, "ID must not be null");
        repository.deleteById(id);
    }

    public Optional<Currency> findById(Long id) {
        validateNotNull(id, "ID must not be null");
        return repository.findById(id);
    }

    public List<Currency> findAll() {
        return repository.findAll();
    }

    private void validateNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
