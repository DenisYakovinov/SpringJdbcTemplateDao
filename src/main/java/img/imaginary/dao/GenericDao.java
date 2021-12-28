package img.imaginary.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao <T, ID> {

    public Optional<ID> add(T entity);
    
    public List<T> findAll();

    public T findById(int id);

    public void update(T entity);

    public void delete(int id);

    public void delete(T entity);
}