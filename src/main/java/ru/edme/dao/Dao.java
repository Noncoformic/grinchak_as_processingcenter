package ru.edme.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    void createTable() ;
    void dropTable();
    void clearTable() ;
    void insert(T entity);
    void delete(Long id);
    List<T> getAll();
    Optional<T> getById(Long id);
    void update(T entity);
}
