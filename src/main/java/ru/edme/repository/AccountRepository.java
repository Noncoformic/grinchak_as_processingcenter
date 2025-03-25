package ru.edme.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.edme.model.Account;

@Repository
public interface AccountRepository extends ListCrudRepository<Account,Long> {

}
