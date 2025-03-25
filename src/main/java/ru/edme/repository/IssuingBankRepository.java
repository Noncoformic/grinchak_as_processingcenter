package ru.edme.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.edme.model.IssuingBank;

@Repository
public interface IssuingBankRepository extends ListCrudRepository<IssuingBank,Long> {
}
