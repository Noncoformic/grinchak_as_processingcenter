package ru.edme.service;

import java.util.List;

public interface CrudService<D, ID> {
    D create(D dto);
    D update(ID id, D dto);
    D get(ID id);
    List<D> getAll();
    void delete(ID id);
}
