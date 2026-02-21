package com.uteq.sgtic.services.impl;

// import com.uteq.sgtic.services.ICrud;
// import org.springframework.data.jpa.repository.JpaRepository;

// import java.util.List;

// public abstract class CrudImpl<T, ID> implements ICrud<T, ID> {

//     protected abstract JpaRepository<T, ID> getRepository();

//     @Override
//     public T save(T t) throws Exception {
//         return getRepository().save(t);
//     }

//     @Override
//     public T update(ID id, T t) throws Exception {
//         return getRepository().save(t);
//     }

//     @Override
//     public T delete(ID id) throws Exception {
//         return getRepository().getReferenceById(id);
//     }

//     @Override
//     public List<T> findAll() throws Exception {
//         return getRepository().findAll();
//     }

//     @Override
//     public T findById(ID id) throws Exception {
//         return getRepository().findById(id).orElse(null);
//     }
// }
