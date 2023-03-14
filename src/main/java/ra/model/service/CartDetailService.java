package ra.model.service;


import ra.model.entity.CartDetail;
import ra.model.entity.Carts;

import java.util.List;

public interface CartDetailService extends RootService<CartDetail,Integer> {
    CartDetail findByProduct_ProductIdAndCarts_CartId(Integer bookId, Integer cartId);
    boolean deleteByCartDetailId(Integer id);
    List<CartDetail> findByProduct_ProductId(int bookId);
    List<CartDetail> findByCartsIn(List<Carts> listCart);
}
