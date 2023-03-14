package ra.model.service;


import ra.model.entity.Carts;
import ra.payload.request.CartConfirm;

import java.util.Date;
import java.util.List;

public interface CartService extends RootService<Carts,Integer> {
    Carts mapCartConfirmToCart(Carts carts, CartConfirm confirm);

    List<Carts> findByCreatDateBetween(Date startDate, Date endDate);
    List<Carts> findByUsers_UserIdAndCartStatus(Integer userId, Integer cartStatus);
}
