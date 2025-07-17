package ru.edme.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dto.CurrencyDto;
import ru.edme.mapper.CurrencyMapper;
import ru.edme.model.Currency;
import ru.edme.repository.CurrencyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CurrencyService {

    private final CurrencyRepository repository;
    private final CurrencyMapper mapper;

    /** Создать новую валюту */
    public CurrencyDto create(CurrencyDto dto) {
        Currency entity = mapper.toEntity(dto);
        Currency saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    /** Обновить существующую валюту по ID */
    public CurrencyDto update(Long id, CurrencyDto dto) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Currency with id=" + id + " not found");
        }
        Currency entity = mapper.toEntity(dto);
        entity.setId(id);
        Currency saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    /** Получить валюту по ID */
    @Transactional(readOnly = true)
    public CurrencyDto getById(Long id) {
        Currency entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Currency with id=" + id + " not found"));
        return mapper.toDto(entity);
    }

    /** Получить все валюты */
    @Transactional(readOnly = true)
    public List<CurrencyDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    /** Удалить валюту по ID */
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Currency with id=" + id + " not found");
        }
        repository.deleteById(id);
    }
}
