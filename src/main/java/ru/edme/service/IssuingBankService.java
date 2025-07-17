package ru.edme.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dto.IssuingBankDto;
import ru.edme.mapper.IssuingBankMapper;
import ru.edme.model.IssuingBank;
import ru.edme.repository.IssuingBankRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IssuingBankService {

    private final IssuingBankRepository repository;
    private final IssuingBankMapper mapper;

    /** Создать банк-эмитент */
    public IssuingBankDto create(IssuingBankDto dto) {
        IssuingBank entity = mapper.toEntity(dto);
        IssuingBank saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    /** Обновить банк-эмитент по ID */
    public IssuingBankDto update(Long id, IssuingBankDto dto) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("IssuingBank with id=" + id + " not found");
        }
        IssuingBank entity = mapper.toEntity(dto);
        entity.setId(id);
        IssuingBank saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    /** Получить банк-эмитент по ID */
    @Transactional(readOnly = true)
    public IssuingBankDto getById(Long id) {
        IssuingBank entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("IssuingBank with id=" + id + " not found"));
        return mapper.toDto(entity);
    }

    /** Получить все банки-эмитенты */
    @Transactional(readOnly = true)
    public List<IssuingBankDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    /** Удалить банк-эмитент по ID */
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("IssuingBank with id=" + id + " not found");
        }
        repository.deleteById(id);
    }
}
