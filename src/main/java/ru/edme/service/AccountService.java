package ru.edme.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dto.AccountDto;
import ru.edme.mapper.AccountMapper;
import ru.edme.model.Account;
import ru.edme.repository.AccountRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;

    public AccountDto create(AccountDto dto) {
        Account entity = mapper.toEntity(dto);
        Account saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    public AccountDto update(Long id, AccountDto dto) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Account with id=" + id + " not found");
        }
        Account entity = mapper.toEntity(dto);
        entity.setId(id);
        Account saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public AccountDto getById(Long id) {
        Account entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account with id=" + id + " not found"));
        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<AccountDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Account with id=" + id + " not found");
        }
        repository.deleteById(id);
    }
}
