package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.entity.Product;
import ra.model.entity.Star;
import ra.model.repository.StarRepository;
import ra.model.service.ProductService;
import ra.model.service.StarService;
import ra.model.service.UserService;
import ra.payload.request.StarRequest;

import java.util.List;
@Service
public class StarSeviceImp implements StarService {
    @Autowired
    private StarRepository starRepository;
    @Autowired private UserService userService;
    @Autowired private ProductService productService;
    @Override
    public Page<Star> getAllList(Pageable pageable) {
        return starRepository.findAll(pageable) ;
    }

    @Override
    public Star saveOrUpdate(Star star) {
        return starRepository.save(star);
    }

    @Override
    public Star findById(Integer id) {
        return starRepository.findById(id).get();
    }

    @Override
    public Page<Star> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public List<Star> findAll() {
        return starRepository.findAll();
    }


    @Override
    public Star mapRequestToStar(StarRequest request) {
        Product product= productService.findById(request.getProductId());
        Star star = new Star();
        star.setStarPoint(request.getStarPoint());
        star.setProduct(product);
        return star;
    }
}
