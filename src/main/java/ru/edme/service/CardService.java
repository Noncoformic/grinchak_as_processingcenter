package ru.edme.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.model.Card;
import ru.edme.repository.CardRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

  private final CardRepository cardRepository;

  public Card saveCard(Card card) {
    validateNotNull(card, "Card must not be null");
    return cardRepository.save(card);
  }

  public Optional<Card> getCardById(Long id) {
    validateNotNull(id, "ID must not be null");
    return cardRepository.findById(id);
  }

  public List<Card> getAllCards() {
    return cardRepository.findAll();
  }

  @Transactional
  public void deleteCard(Long id) {
    validateNotNull(id, "ID must not be null");
    cardRepository.deleteById(id);
  }

  private void validateNotNull(Object obj, String message) {
    if (obj == null) {
      throw new IllegalArgumentException(message);
    }
  }

}
