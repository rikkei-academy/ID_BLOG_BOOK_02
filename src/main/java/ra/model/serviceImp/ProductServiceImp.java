package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ra.model.entity.Product;
import ra.model.repository.ProductRepository;
import ra.model.service.ProductService;

import java.util.List;
@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(int productId) {
        return productRepository.findById(productId).get();
    }

    @Override
    public Product saveOfUpdate(Product pro) {
        return productRepository.save(pro);
    }

    @Override
    public void delete(int productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public List<Product> searchName(String productName) {
        return productRepository.findByProductNameContaining(productName);
    }

    @Override
    public List<Product> searchProductByCatalogId(int catalogId) {
        return productRepository.findByCatalog_CatalogId(catalogId);
    }

    @Override
    public List<Product> sortProductByProductName(String direction) {
        if (direction.equals("asc")){
            return productRepository.findAll(Sort.by("productName").ascending());
        }else {
            return  productRepository.findAll(Sort.by("productName").descending());
        }
    }
    @Override
    public Page<Product> getPagging(Pageable pageablee) {
        return productRepository.findAll(pageablee);
    }
    @Override
    public List<Product> listWishList(int userId) {
        return productRepository.findAllWishList(userId);
    }
}
