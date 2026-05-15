package utilities.database;

import java.util.List;

public interface ContractDAO<T> {
    boolean save(T entity);
    boolean delete(int id);
    T findByID(int id);
    List<T> findAll();
}
