package ru.edme.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dto.CardDto;
import ru.edme.mapper.CardMapper;
import ru.edme.model.Card;
import ru.edme.repository.AccountRepository;
import ru.edme.repository.CardRepository;
import ru.edme.repository.CardStatusRepository;
import ru.edme.repository.PaymentSystemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService implements CrudService<CardDto, Long> {

  private final CardRepository repo;
  private final CardMapper mapper;
  private final CardStatusRepository statusRepo;
  private final PaymentSystemRepository psRepo;
  private final AccountRepository accountRepo;

  @Override
  public CardDto create(CardDto dto) {
    // 1) Сконвертировать в сущность и заполнить простые поля
    Card card = mapper.toEntity(dto);

    // 2) Подгрузить статус по id
    card.setCardStatus(
            statusRepo.findById(dto.getCardStatusId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "CardStatus " + dto.getCardStatusId() + " not found"))
    );

    // 3) Подгрузить paymentSystem
    card.setPaymentSystem(
            psRepo.findById(dto.getPaymentSystemId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "PaymentSystem " + dto.getPaymentSystemId() + " not found"))
    );

    // 4) Подгрузить account
    card.setAccount(
            accountRepo.findById(dto.getAccountId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Account " + dto.getAccountId() + " not found"))
    );

    // 5) Сохранить
    Card saved = repo.save(card);
    return mapper.toDto(saved);
  }

  @Override
  public CardDto update(Long id, CardDto dto) {
    if (!repo.existsById(id)) {
      throw new EntityNotFoundException("Card " + id + " not found");
    }
    Card card = mapper.toEntity(dto);
    card.setId(id);

    // та же логика подгрузки связанных сущностей
    card.setCardStatus(
            statusRepo.findById(dto.getCardStatusId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "CardStatus " + dto.getCardStatusId() + " not found"))
    );
    card.setPaymentSystem(
            psRepo.findById(dto.getPaymentSystemId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "PaymentSystem " + dto.getPaymentSystemId() + " not found"))
    );
    card.setAccount(
            accountRepo.findById(dto.getAccountId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Account " + dto.getAccountId() + " not found"))
    );

    Card updated = repo.save(card);
    return mapper.toDto(updated);
  }

  @Override
  @Transactional(readOnly = true)
  public CardDto get(Long id) {
    return repo.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException("Card " + id + " not found"));
  }

  @Override
  @Transactional(readOnly = true)
  public List<CardDto> getAll() {
    return repo.findAll()
            .stream()
            .map(mapper::toDto)
            .toList();
  }

  @Override
  public void delete(Long id) {
    if (!repo.existsById(id)) {
      throw new EntityNotFoundException("Card " + id + " not found");
    }
    repo.deleteById(id);
  }
}
