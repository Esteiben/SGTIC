package com.uteq.sgtic.services;

import java.util.List;

public interface ICrud<T, ID> {
    T save(T t) throws Exception;
    T update(ID id, T t) throws Exception;
    T delete(ID id) throws Exception;
    List<T> findAll() throws Exception;
    T findById(ID id) throws Exception;
}
