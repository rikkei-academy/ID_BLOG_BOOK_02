package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.Catalog;
import ra.model.entity.Product;
import ra.model.entity.ResponseObject;
import ra.model.service.CatalogService;
import ra.model.service.ProductService;
import ra.model.serviceImp.ProductServiceImp;
import ra.payload.request.ProductRequest;
import ra.payload.response.ProductResponse;

import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductServiceImp productServiceImp;

    @Autowired
    private CatalogService catalogService;


    @GetMapping
    public List<Product> getAllProduct() {

        return productService.findAll();
    }

    @PostMapping("/create")
//        @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {
        try{
            Product pro = new Product();
            pro.setProductName(productRequest.getProductName());
            pro.setProductPrice(productRequest.getProductPrice());
            pro.setProductQuantity(productRequest.getProductQuantity());
            pro.setProductDescription(productRequest.getProductDescription());
            pro.setProductImg(productRequest.getProductImg());
            pro.setProductCompany(productRequest.getProductCompany());
            pro.setProductAuthor(productRequest.getProductAuthor());
            pro.setProductLanguage(productRequest.getProductLanguage());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateNow = new Date();
            String strNow = sdf.format(dateNow);
            try {
                pro.setProductBirthOfDate(sdf.parse(strNow));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            pro.setProductStatus(productRequest.isProductStatus());
            Catalog catalog = catalogService.findById(productRequest.getCatalogId());
            pro.setCatalog(catalog);
    //         productService.saveOfUpdate(pro);
    //        for (String str : productRequest.getListImage()) {
    //            Image image = new Image();
    //            image.setProduct(pro);
    //            image.setImageLink(str);
    //            image = imageSevice.saveOrUpdate(image);
    //            pro.getListImage().add(image);
    //        }
                return ResponseEntity.ok(productService.saveOfUpdate(pro));
            } catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.ok("Có lỗi trong quá trình xử lý vui lòng thử lại!");
            }
        }



    @PutMapping("update/{productId}")
//        @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") int productId, @RequestBody ProductRequest productRequest) {
        try {
            Product productUpdate = productService.findById(productId);
            productUpdate.setProductName(productRequest.getProductName());
            productUpdate.setProductPrice(productRequest.getProductPrice());
            productUpdate.setProductQuantity(productRequest.getProductQuantity());
            productUpdate.setProductStatus(productRequest.isProductStatus());
            productUpdate.setProductDescription(productRequest.getProductDescription());
            productUpdate.setProductImg(productRequest.getProductImg());
            productUpdate.setProductLanguage(productRequest.getProductLanguage());
            productUpdate.setProductCompany(productRequest.getProductCompany());
            productUpdate.setProductAuthor(productRequest.getProductAuthor());
            Catalog catalog = catalogService.findById(productRequest.getCatalogId());
            productUpdate.setCatalog(catalog);
//            for (Image image : productUpdate.getListImage()) {
//                imageSevice.deleteById(image.getImageId());
//            }
//            for (String str : productRequest.getListImage()) {
//                Image image = new Image();
//                image.setProduct(productUpdate);
//                image.setImageLink(str);
//                image = imageSevice.saveOrUpdate(image);
//                productUpdate.getListImage().add(image);
//            }

            Product editProduct= productService.saveOfUpdate(productUpdate);
            return ResponseEntity.ok(editProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Chưa Cập Nhập Được ok");
        }
    }



    @DeleteMapping("/delete/{producId}")
//        @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteproduct(@PathVariable("producId") int productId) {
        try{
            Product product  = productService.findById(productId);
//            for (Image image:product.getListImage()){
//                imageSevice.deleteById(image.getImageId());
//            }
            product.setProductStatus(false);
            productService.saveOfUpdate(product);
            return ResponseEntity.ok("Đã xóa thành công ");
        }catch (Exception e){
            return ResponseEntity.ok("Chưa xóa được kiểm tra lại ");
        }
    }

//    @GetMapping("/getByProductId")
//    public ResponseEntity<?> getByProductId(int productId) {
//        Product pr = productService.finById(productId);
//        return ResponseEntity.ok(pr);
//    }
    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable("productId") int productId) {
        return productService.findById(productId);
    }

    @GetMapping("/search")
    public List<Product> searchName(@RequestParam("productName") String productName) {
        return productService.searchName(productName);
    }
    @GetMapping("/sortByName")
    public ResponseEntity<List<Product>> sortBookByName(@RequestParam("direction") String direction) {
        List<Product> listBook = productService.sortProductByProductName(direction);
        return new ResponseEntity<>(listBook, HttpStatus.OK);
    }
    @GetMapping("/getPagging")
//        @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getPagging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> pageProduct = productService.getPagging(pageable);
        Map<String, Object> data = new HashMap<>();
        data.put("products", pageProduct.getContent());
        data.put("total", pageProduct.getSize());
        data.put("totalItems", pageProduct.getTotalElements());
        data.put("totalPages", pageProduct.getTotalPages());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


}
