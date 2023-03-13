package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.Cart;
import ra.model.repository.CartRepository;
import ra.model.service.CartService;

import java.util.List;

@Service
public class CartServiceImp implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Override
    public Cart insert(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public void delete(int cartId) {
        cartRepository.deleteById(cartId);
    }

    @Override
    public Cart findById(int cart) {
        return cartRepository.findById(cart).get();
    }

    @Override
    public List<Cart> findAllUserId(int userId) {
        return cartRepository.findByUsers_UserId(userId);
    }
}
