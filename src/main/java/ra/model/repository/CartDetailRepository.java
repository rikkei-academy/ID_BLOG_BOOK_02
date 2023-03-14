package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.CartDetail;
import ra.model.entity.Carts;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail,Integer> {
    CartDetail findByProduct_ProductIdAndCarts_CartId(int bookId, int cartId);
    List<CartDetail> findByProduct_ProductId(int bookId);
    List<CartDetail> findByCartsIn(List<Carts> listCart);
}
