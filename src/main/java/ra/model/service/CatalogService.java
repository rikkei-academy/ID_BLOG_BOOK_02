package ra.model.service;

import ra.model.entity.Catalog;

import java.util.List;

public interface CatalogService {
    List<Catalog> findAll();
    Catalog findById(int catalogId);
    Catalog saveorUpdate(Catalog catalog);
    List<Catalog> searchByName(String catalogName);
}
