package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ra.model.entity.Users;
import ra.model.repository.UserRepository;
import ra.model.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Users findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Users findByID(int userID) {
        return userRepository.findById(userID).get();
    }

    @Override
    public Users saveOrUpdate(Users user) {
        return userRepository.save(user);
    }

    @Override
    public List<Users> searchByName(String userName) {
        return userRepository.findUserByUserNameContaining(userName);
    }
    public Page<Users> getPagination(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public List<Users> listFilter(Integer option) {
        List<Users> userList = userRepository.findAll();
        List<Users> listFilter = new ArrayList<>();
        for (Users users : userList ) {
            if (users.getListRoles().size()==option){
                listFilter.add(users);
            }
        }
        return listFilter;
    }

    @Override
    public List<Users> sortByName(String directionName) {
        if(directionName.equals("asc")){
            return userRepository.findAll(Sort.by("userName"));
        }else {
            return  userRepository.findAll(Sort.by("userName").descending());
        }
    }
}