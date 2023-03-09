package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ra.jwt.JwtTokenProvider;
import ra.model.entity.ERole;
import ra.model.entity.Roles;
import ra.model.entity.Users;
import ra.model.service.RoleService;
import ra.model.service.UserService;
import ra.payload.request.ChangePass;
import ra.payload.request.LoginRequest;
import ra.payload.request.SignupRequest;
import ra.payload.response.JwtResponse;
import ra.payload.response.MessageResponse;
import ra.security.CustomUserDetails;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder encoder;
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        if (userService.existsByUserName(signupRequest.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Usermame is already"));
        }
        if (userService.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already"));
        }
        Users user = new Users();
        user.setUserName(signupRequest.getUserName());
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        user.setEmail(signupRequest.getEmail());
        user.setPhone(signupRequest.getPhone());
        user.setAddress(signupRequest.getAddress());
        user.setUserStatus(true);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateNow = new Date();
        String strNow = sdf.format(dateNow);
        try {
            user.setCreated(sdf.parse(strNow));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        Set<String> strRoles = signupRequest.getListRoles();
        Set<Roles> listRoles = new HashSet<>();
        if (strRoles==null){
            //User quyen mac dinh
            Roles userRole = roleService.findByRoleName(ERole.ROLE_USER).orElseThrow(()->new RuntimeException("Error: Role is not found"));
            listRoles.add(userRole);
        }else {
            strRoles.forEach(role->{
                switch (role){
                    case "admin":
                        Roles adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                        listRoles.add(adminRole);
                    case "moderator":
                        Roles modRole = roleService.findByRoleName(ERole.ROLE_MODERATOR)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                        listRoles.add(modRole);
                    case "user":
                        Roles userRole = roleService.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                        listRoles.add(userRole);
                }
            });
        }
        user.setListRoles(listRoles);
        userService.saveOrUpdate(user);
        return ResponseEntity.ok(new MessageResponse("Đã đăng ký thành công"));
    }
    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetail = (CustomUserDetails) authentication.getPrincipal();
        //Sinh JWT tra ve client
        String jwt = tokenProvider.generateToken(customUserDetail);
        //Lay cac quyen cua user
        List<String> listRoles = customUserDetail.getAuthorities().stream()
                .map(item->item.getAuthority()).collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,customUserDetail.getUsername(),customUserDetail.getEmail(),
                customUserDetail.getPhone(),listRoles));

    }
    @GetMapping("/logOut")
    public ResponseEntity<?> logOut(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        // Clear the authentication from server-side (in this case, Spring Security)
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("You have been logged out.");
    }
    //////////////////sdsddssdsdsdsd////////////////////////

    @PutMapping("/changePass")
    public ResponseEntity<?> changePassword(@RequestBody ChangePass changePass) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userService.findByID(userDetails.getUserId());
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        boolean passChecker = bc.matches(changePass.getOldPassword(), users.getPassword());
        if (passChecker) {
            boolean checkDuplicate = bc.matches(changePass.getPassword(), users.getPassword());
            if (checkDuplicate) {
                return ResponseEntity.ok(new MessageResponse("Mật khẩu mới phải khác mật khẩu cũ!"));
            } else {
                users.setPassword(encoder.encode(changePass.getPassword()));
                userService.saveOrUpdate(users);
                return ResponseEntity.ok(new MessageResponse("Đổi mật khẩu thành công !"));
            }
        } else {
            return ResponseEntity.ok(new MessageResponse("Mật khẩu không hợp lệ ! Đổi mật khẩu thất bại"));
        }
    }

    @GetMapping()
    public List<Users> readUser(){
        List<Users> userList = userService.findAll();
        return userList;
    }

    @GetMapping("/searchUser")
    public List<Users> listSearch(@RequestParam("userName") String userName){
        List<Users> listSearch = userService.searchByName(userName);
        return listSearch ;
    }

    @GetMapping("/getPagging")
    public ResponseEntity<Map<String, Object>> getPagging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Users> pageBook = userService.getPagination(pageable);
        Map<String, Object> data = new HashMap<>();
        data.put("user", pageBook.getContent());
        data.put("total", pageBook.getSize());
        data.put("totalItems", pageBook.getTotalElements());
        data.put("totalPages", pageBook.getTotalPages());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }



    @PostMapping("/block/{userID}")
    public ResponseEntity<?> blockUser(@PathVariable("userID") int userID) {
        Users userBlock = userService.findByID(userID);
        userBlock.setUserStatus(false);
        userService.saveOrUpdate(userBlock);
        return ResponseEntity.ok("Tài Khoản bị khóa !");
    }

    @GetMapping("/filter/{option}")
    public List<Users> listFilter(@PathVariable("option") Integer option){
        return userService.listFilter(option);
    }

    @GetMapping("/sort")
    public List<Users> sortUser(@RequestParam("userName") String userName){
        List<Users> listSort = userService.sortByName(userName);
        return  listSort;
    }
}

