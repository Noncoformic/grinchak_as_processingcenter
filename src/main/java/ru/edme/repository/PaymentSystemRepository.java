package ru.edme.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.edme.model.PaymentSystem;

@Repository
public interface PaymentSystemRepository extends ListCrudRepository<PaymentSystem,Long> {
}
