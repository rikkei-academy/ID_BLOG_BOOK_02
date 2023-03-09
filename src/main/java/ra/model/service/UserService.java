package ra.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.entity.Users;

import java.util.List;

public interface UserService {
    Users findByUserName(String userName);
    List<Users> findAll();
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Users findByID(int userID);
    Users saveOrUpdate(Users user);
    List<Users> searchByName(String userName);

    Page<Users> getPagination(Pageable pageable);
    List<Users> listFilter(Integer option);
    List<Users> sortByName(String directionName);
    Users findByEmail(String email);
}
