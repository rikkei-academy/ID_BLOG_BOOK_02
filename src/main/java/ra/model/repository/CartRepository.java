package ra.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.Cart;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    List<Cart> findByUsers_UserId(int userId);
    Page<Cart> findAllByOrderStatus(int status, Pageable pageable);
    Page<Cart> findAllByUser_UserId(int id,Pageable pageable);

    Page<Cart> findAllByUser_UserIdAndOrderStatus(int userId, int status, Pageable pageable);
    Cart findByUser_UserIdAndOrderStatus(int id,int status);
}
