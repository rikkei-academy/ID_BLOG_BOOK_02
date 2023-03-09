package ra.model.service;

import ra.model.entity.Users;

public interface UserService {
    Users findByUserName(String userName);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Users findByID(int userID);
    Users saveOrUpdate(Users user);
}
