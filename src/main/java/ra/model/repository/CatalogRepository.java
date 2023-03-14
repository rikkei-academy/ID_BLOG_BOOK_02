package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.Catalog;

import java.util.List;

public interface CatalogRepository extends JpaRepository<Catalog,Integer> {
    List<Catalog> findByCatalogNameContaining(String catalogName);
}
