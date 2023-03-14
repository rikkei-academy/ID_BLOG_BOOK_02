package ra.controller;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.*;
import ra.model.service.CartDetailService;
import ra.model.service.ProductService;
import ra.model.service.StarService;
import ra.model.service.UserService;
import ra.payload.request.StarRequest;
import ra.security.CustomUserDetails;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/star")
@AllArgsConstructor
public class StarController {
    @Autowired
    private StarService starService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartDetailService cartDetailService;

    @PostMapping("/creatStar")
    public ResponseEntity<?> creatNewStar(@RequestBody StarRequest request) {
        try {
            Star star = starService.mapRequestToStar(request);
            CustomUserDetails usersChangePass = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Users users = userService.findByUserName(usersChangePass.getUsername());
            Product product = productService.findById(request.getProductId());
            List<Users> usersList = new ArrayList<>();
            List<CartDetail> listCartDetail = cartDetailService.findByProduct_ProductId(product.getProductId());
            for (CartDetail cd : listCartDetail) {
                usersList.add(cd.getCarts().getUsers());
            }
            boolean check =false;
            for (Users user : usersList) {
                if (user.getUserId()==users.getUserId()){
                    check =true;
                }
            }
            Star starNew = new Star();
            if (check){
                starNew.setStarPoint(request.getStarPoint());
                starNew.setUsers(users);
                starNew.setProduct(product);
                starService.saveOrUpdate(starNew);
            }
            Star result=  starService.saveOrUpdate(starNew);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
