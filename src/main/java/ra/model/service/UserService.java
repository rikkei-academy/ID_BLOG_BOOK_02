package ra.model.service;

import ra.model.entity.Users;

import java.util.List;

public interface UserService {
    Users findByUserName(String userName);
    List<Users> findAll();
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Users findByID(int userID);
    Users saveOrUpdate(Users user);
}
