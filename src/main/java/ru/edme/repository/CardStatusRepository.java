package ru.edme.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.edme.model.CardStatus;

@Repository
public interface CardStatusRepository extends ListCrudRepository<CardStatus,Long > {

}
