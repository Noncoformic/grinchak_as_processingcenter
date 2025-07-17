package ru.edme.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dto.CardStatusDto;
import ru.edme.mapper.CardStatusMapper;
import ru.edme.model.CardStatus;
import ru.edme.repository.CardStatusRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CardStatusService {

  private final CardStatusRepository repository;
  private final CardStatusMapper mapper;

  public CardStatusDto create(CardStatusDto dto) {
    CardStatus entity = mapper.toEntity(dto);
    CardStatus saved = repository.save(entity);
    return mapper.toDto(saved);
  }

  public CardStatusDto update(Long id, CardStatusDto dto) {
    if (!repository.existsById(id)) {
      throw new EntityNotFoundException("CardStatus with id=" + id + " not found");
    }
    CardStatus entity = mapper.toEntity(dto);
    entity.setId(id);
    CardStatus saved = repository.save(entity);
    return mapper.toDto(saved);
  }

  @Transactional(readOnly = true)
  public CardStatusDto getById(Long id) {
    CardStatus entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CardStatus with id=" + id + " not found"));
    return mapper.toDto(entity);
  }

  @Transactional(readOnly = true)
  public List<CardStatusDto> getAll() {
    return repository.findAll()
            .stream()
            .map(mapper::toDto)
            .toList();
  }

  public void delete(Long id) {
    if (!repository.existsById(id)) {
      throw new EntityNotFoundException("CardStatus with id=" + id + " not found");
    }
    repository.deleteById(id);
  }
}
