package ra.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.CartDetail;
import ra.model.entity.Carts;
import ra.model.entity.Product;
import ra.model.entity.Users;
import ra.model.service.CartDetailService;
import ra.model.service.CartService;
import ra.model.service.ProductService;
import ra.model.service.UserService;
import ra.payload.request.CartConfirm;
import ra.payload.request.CartDetailRequest;
import ra.security.CustomUserDetails;
import ra.model.sendEmail.ProvideSendEmail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/cart")
@AllArgsConstructor
public class CartController {

    private CartDetailService cartDetailService;
    private CartService cartService;
    private UserService userService;
    private ProductService productService;
    private ProvideSendEmail provideSendEmail;

    @PutMapping("/Addcart")
    public ResponseEntity<?> addToCart(@RequestBody CartDetailRequest cartDetailRequest, @RequestParam String action) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CartDetail cartDetail = null;
        List<Carts> cartsList=cartService.findByUsers_UserIdAndCartStatus(userDetails.getUserId(), 0);
        Carts cart = cartService.findById(cartDetailRequest.getCartId());
        boolean check = false;
        if (cartsList.get(0).getCartId()== cart.getCartId()&&cart.getCartStatus()==0){
            check= true;
        }
        if (check){
            try {
                cartDetail = cartDetailService.findByProduct_ProductIdAndCarts_CartId(cartDetailRequest.getProductId(), cart.getCartId());
                if (cartDetail != null) {
                    if (action.equals("Add more")) {
                        cartDetail.setQuantity(cartDetail.getQuantity() + cartDetailRequest.getQuantity());
                    } else if (action.equals("Edit")) {
                        cartDetail.setQuantity(cartDetailRequest.getQuantity());
                    }
                    cartDetailService.saveOrUpdate(cartDetail);
                    return new ResponseEntity<>(cartDetail, HttpStatus.OK);
                } else {
                    cartDetail = new CartDetail();
                    cartDetail.setCarts(cart);
                    cartDetail.setProduct(productService.findById(cartDetailRequest.getProductId()));
                    cartDetail.setQuantity(cartDetailRequest.getQuantity());
                    cartDetail.setPrice(cartDetailRequest.getPrice());
                    cartDetail.setStatusCartDetail(true);
                    CartDetail result = cartDetailService.saveOrUpdate(cartDetail);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                }
            } catch (Exception e) {
                return new ResponseEntity<>("Có lỗi trong quá trình xử lý",HttpStatus.BAD_REQUEST);
            }
        }else {
            return new ResponseEntity<>("Không đúng giỏ hàng",HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/deleteCart/{detailId}")
    public ResponseEntity<?> deleteCartDetail(@PathVariable int detailId) {
        try {
            cartDetailService.deleteByCartDetailId(detailId);
            return ResponseEntity.ok().body("Xóa thành công");
        }catch (Exception ex) {
            return ResponseEntity.ok().body("Chưa xóa được thành công ");
        }
    }

    @PutMapping("/check_out")
    public ResponseEntity<?> checkout(@RequestBody CartConfirm confirm) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Carts cart = cartService.findById(confirm.getCartId());
        try {
            Carts result = cartService.saveOrUpdate(cartService.mapCartConfirmToCart(cart, confirm));
            for (CartDetail detail : result.getCartDetails()) {
                Product product = detail.getProduct();
                product.setProductQuantity(product.getProductQuantity() - detail.getQuantity());
                productService.saveOfUpdate(product);
            }
            String subject = "Payment successfully: " + result.getCartName();
            String mess = "Cám ơn vì đã mua hàng. Đơn đặt hàng của bạn đang được xác nhận. Thời gian giao hàng sẽ được cập nhật sau khi xác nhận thành công. Vui lòng kiểm tra email của bạn để biết thông tin mới nhất.\n" +
                    "Detail oder:\n";
            String sDetail = "";
            float total = 0;
            for (CartDetail detail : result.getCartDetails()) {
                sDetail += detail.getProduct().getProductName() + " x" + detail.getQuantity() + " " + " x" + detail.getPrice() + "vnd" + "\n";
                total += detail.getQuantity() * detail.getPrice();
            }
            mess = mess + sDetail +
                    "-------------------------------------------------\n" +
                    "Total: " + total * result.getDiscount() + "vnd.\n" +
                    "Full name: " + result.getLastName() + " " + result.getFirstName() + ".\n" +
                    "Phone: " + result.getPhone() + ".\n" +
                    "Address: " + result.getCity() + " " + result.getState() + " " + result.getAddress()
            ;
            provideSendEmail.sendSimpleMessage(result.getEmail(),
                    subject, mess);
            Carts newCart = new Carts();
            newCart.setUsers((Users) userService.findByID(customUserDetails.getUserId()));
            Carts pendingCart = cartService.saveOrUpdate(newCart);
            return new ResponseEntity<>(pendingCart, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get_paging_and_sort")
    public ResponseEntity<Map<String, Object>> getPagingAndSort(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String direction,
            @RequestParam String sortBy) {
        Map<String, Object> data = new HashMap<>();
        try {
            Sort.Order order;
            if (direction.equals("asc")) {
                order = new Sort.Order(Sort.Direction.ASC, sortBy);
            } else {
                order = new Sort.Order(Sort.Direction.DESC, sortBy);
            }
            Pageable pageable = PageRequest.of(page, size, Sort.by(order));
            Page<Carts> carts = cartService.getAllList(pageable);
            data.put("carts", carts.getContent());
            data.put("total", carts.getSize());
            data.put("totalItems", carts.getTotalElements());
            data.put("totalPages", carts.getTotalPages());
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("/search_by_name")
    public ResponseEntity<?> searchByName(
            @RequestParam String searchName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Map<String, Object> data = new HashMap<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Carts> tags = cartService.findByName(searchName, pageable);
            data.put("tags", tags.getContent());
            data.put("total", tags.getSize());
            data.put("totalItems", tags.getTotalElements());
            data.put("totalPages", tags.getTotalPages());
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
    }
//    kkkk  c
}
