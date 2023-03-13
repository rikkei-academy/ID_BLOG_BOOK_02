package ra.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.Catalog;
import ra.model.service.CatalogService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/catalog")
public class CatalogController {
    @Autowired
    private CatalogService catalogService;

    @GetMapping
    public List<Catalog> getAllCatalog(){
        return catalogService.findAll();
    }

    @PostMapping("/create")
    //    @PreAuthorize("hasRole('ADMIN')")
    public Catalog createCatalog(@RequestBody Catalog catalog){
        catalog.setCatalogStatus(true);
        return catalogService.saveorUpdate(catalog);
    }

    @PutMapping("update/{catalogId}")
//    @PreAuthorize("hasRole('ADMIN')")
    public Catalog updateCatalog(@PathVariable("catalogId") int catalogId,@RequestBody Catalog catalog){
        Catalog catalogUpdate = catalogService.findById(catalogId);
        catalogUpdate.setCatalogName(catalog.getCatalogName());
        catalogUpdate.setCatalogStatus(catalog.getCatalogStatus());
        return catalogService.saveorUpdate(catalogUpdate);
    }

    @DeleteMapping("/delete/{catalogId}")
//    @PreAuthorize("hasRole('ADMIN')")
    public void DeleteCatalog(@PathVariable("catalogId") int catalogId){
        Catalog catalog = catalogService.findById(catalogId);
        catalog.setCatalogStatus(false);
        catalogService.saveorUpdate(catalog);
    }

    @GetMapping("/search")
    public List<Catalog> searchByName(@RequestParam("catalogName") String catalogName){
        return catalogService.searchByName(catalogName);
    }

}
