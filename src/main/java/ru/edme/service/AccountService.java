package ru.edme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.model.Account;
import ru.edme.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class AccountService {
    private final AccountRepository repository;

    public Account save(Account account) {
        validateNotNull(account, "Account must not be null");
        return repository.save(account);
    }

    public Account update(Account account) {
        validateNotNull(account, "Account must not be null");
        return repository.save(account);
    }

    @Transactional
    public void delete(Long id) {
        validateNotNull(id, "ID must not be null");
        repository.deleteById(id);
    }

    public Optional<Account> findById(Long id) {
        validateNotNull(id, "ID must not be null");
        return repository.findById(id);
    }

    public List<Account> findAll() {
        return repository.findAll();
    }

    private void validateNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }
}