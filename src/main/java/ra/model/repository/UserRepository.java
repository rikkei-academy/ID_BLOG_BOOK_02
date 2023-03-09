package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.Users;
import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {
    List<Users> findUserByUserNameContaining(String userName);
    Users findByUserName(String userName);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);

    Users findByEmail(String email);
}
