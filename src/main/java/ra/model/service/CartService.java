package ra.model.service;

import ra.model.entity.Cart;

import java.util.List;

public interface CartService {
    Cart insert(Cart cart);

    void delete(int cartId);

    Cart findById(int cartId);

    List<Cart> findAllUserId(int userId);
}
