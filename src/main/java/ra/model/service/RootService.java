package ra.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RootService<T,V>{
    Page<T> getAllList(Pageable pageable);
    T saveOrUpdate(T t);
    T findById(V id);
    Page<T> findByName(String name, Pageable pageable);

    List<T> findAll();
}
