package com.oopclass.breadapp.generic;

import java.util.List;

/**
 * OOP Class 20-21
 * @author Gerald Villaran
 * @param <T>
 */

public interface GenericService<T extends Object> {

    T save(T entity);

    T update(T entity);

    void delete(T entity);

    void delete(Long id);

    void deleteInBatch(List<T> entities);

    T find(Long id);

    List<T> findAll();
}
