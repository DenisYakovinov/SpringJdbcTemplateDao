package img.imaginary.service;

import java.util.List;

public interface GenericService<T> {
    
    T add(T entity);
    
    List<T> findAll();

    T findById(int id);

    void update(T entity);

    void delete(int id);

    void delete(T entity);
}
