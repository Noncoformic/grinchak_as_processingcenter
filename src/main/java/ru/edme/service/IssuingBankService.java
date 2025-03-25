package ru.edme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.model.IssuingBank;
import ru.edme.repository.IssuingBankRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IssuingBankService {

    private final IssuingBankRepository repository;

    public IssuingBank save(IssuingBank issuingBank) {
        validateNotNull(issuingBank, "IssuingBank must not be null");
        return repository.save(issuingBank);
    }

    public IssuingBank update(IssuingBank issuingBank) {
        validateNotNull(issuingBank, "IssuingBank must not be null");
        return repository.save(issuingBank);
    }

    @Transactional
    public void delete(Long id) {
        validateNotNull(id, "ID must not be null");
        repository.deleteById(id);
    }

    public Optional<IssuingBank> findById(Long id) {
        validateNotNull(id, "ID must not be null");
        return repository.findById(id);
    }

    public List<IssuingBank> findAll() {
        return repository.findAll();
    }

    private void validateNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

}