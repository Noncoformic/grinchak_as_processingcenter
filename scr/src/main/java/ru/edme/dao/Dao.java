package ru.edme.dao;

import java.util.List;

public interface Dao<T> {
    void createTable();
    void dropTable();
    void clearTable();
    void insert(T entity);
    void delete(Long id);
    List<T> getAll();
    T getById(Long id);
    void update(T entity);
}
