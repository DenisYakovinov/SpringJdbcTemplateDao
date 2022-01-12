package img.imaginary.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao <T, ID> {

    Optional<ID> add(T entity);
    
    List<T> findAll();

    T findById(int id);

    void update(T entity);

    void delete(int id);

    void delete(T entity);
}