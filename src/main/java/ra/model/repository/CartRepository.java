package ra.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.Carts;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Carts,Integer> {
    Page<Carts> findByCartNameContaining(String name, Pageable pageable);
    Page<Carts> findByCartStatusNotIn(List<Integer> cartStatus, Pageable pageable);
    List<Carts> findByCreatDateBetween(Date startDate, Date endDate);
    List<Carts> findByUsers_UserIdAndCartStatus(Integer userId, Integer cartStatus);
}
