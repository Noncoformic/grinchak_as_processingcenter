package ru.edme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.model.CardStatus;
import ru.edme.repository.CardStatusRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class CardStatusService {

  private final CardStatusRepository repository;

  public CardStatus save(CardStatus cardStatus) {
    validateNotNull(cardStatus, "CardStatus must not be null");
    return repository.save(cardStatus);
  }

  public CardStatus update(CardStatus cardStatus) {
    validateNotNull(cardStatus, "CardStatus must not be null");
    return repository.save(cardStatus);
  }

  @Transactional
  public void delete(Long id) {
    validateNotNull(id, "ID must not be null");
    repository.deleteById(id);
  }

  public Optional<CardStatus> findById(Long id) {
    validateNotNull(id, "ID must not be null");
    return repository.findById(id);
  }

  public List<CardStatus> findAll() {
    return repository.findAll();
  }

  private void validateNotNull(Object obj, String message) {
    if (obj == null) {
      throw new IllegalArgumentException(message);
    }
  }
}
