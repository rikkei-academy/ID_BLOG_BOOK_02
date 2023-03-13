package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;
import ra.jwt.JwtTokenProvider;
import ra.model.entity.*;
import ra.model.sendEmail.ProvideSendEmail;
import ra.model.service.PassResetService;
import ra.model.service.ProductService;
import ra.model.service.RoleService;
import ra.model.service.UserService;
import ra.payload.request.*;
import ra.payload.response.JwtResponse;
import ra.payload.response.MessageResponse;
import ra.security.CustomUserDetails;
import ra.security.CustomUserDetailsService;

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
    private ProductService productService;
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Set<String> strRoles = signupRequest.getListRoles();
        Set<Roles> listRoles = new HashSet<>();
        if (strRoles == null) {
            //User quyen mac dinh
            Roles userRole = roleService.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            listRoles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Roles adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(adminRole);
                    case "moderator":
                        Roles modRole = roleService.findByRoleName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(modRole);
                    case "user":
                        Roles userRole = roleService.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(userRole);
                }
            });
        }
        user.setListRoles(listRoles);
        userService.saveOrUpdate(user);
        return ResponseEntity.ok(new MessageResponse("Đã đăng ký thành công"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        Users users = userService.findByUserName(loginRequest.getUserName());
        if (users.isUserStatus()) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails customUserDetail = (CustomUserDetails) authentication.getPrincipal();
            //Sinh JWT tra ve client
            String jwt = tokenProvider.generateToken(customUserDetail);
            //Lay cac quyen cua user
            List<String> listRoles = customUserDetail.getAuthorities().stream()
                    .map(item -> item.getAuthority()).collect(Collectors.toList());
            return ResponseEntity.ok(new JwtResponse(jwt, customUserDetail.getUsername(), customUserDetail.getEmail(),
                    customUserDetail.getPhone(), listRoles));

        } else {
            return ResponseEntity.ok("Tài Khoản đã bị khóa ! Không Đăng Nhập Được ");
        }

    }

    @GetMapping("/logOut")
    public ResponseEntity<?> logOut(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        // Clear the authentication from server-side (in this case, Spring Security)
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("You have been logged out.");
    }
    //////////////////sdsddssdsdsdsd////////////////////////

    @PutMapping("/changePass")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public List<Users> readUser() {
        List<Users> userList = userService.findAll();
        return userList;
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public Users getUserById(@PathVariable("userId") int userId) {
        return userService.findByID(userId);
    }

    @GetMapping("/searchUser")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Users> listSearch(@RequestParam("userName") String userName) {
        List<Users> listSearch = userService.searchByName(userName);
        return listSearch;
    }

    @PostMapping("/updateUser/{userID}")
    @PreAuthorize("hasRole('ADMIN')")
    public Users updateUser(@PathVariable("userID") int userID, @RequestBody UserUpdate userUpdate) {
        Users user = userService.findByID(userID);
        Set<String> strRoles = userUpdate.getListRoles();
        Set<Roles> listRoles = new HashSet<>();
        if (strRoles == null) {
            Roles userRole = roleService.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            listRoles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Roles adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(adminRole);
                    case "moderator":
                        Roles modRole = roleService.findByRoleName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(modRole);
                    case "user":
                        Roles userRole = roleService.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(userRole);
                }
            });
        }
        user.setListRoles(listRoles);
        return userService.saveOrUpdate(user);
    }

    @PutMapping("updateUserForUser/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateUserForUser(@PathVariable("userId") int userId, @RequestBody RegisterRequest registerRequest) {
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already"));
        }
        Users userUpdateUser = userService.findByID(userId);
        userUpdateUser.setUserName(registerRequest.getUserName());
        userUpdateUser.setPhone(registerRequest.getPhone());
        userUpdateUser.setAddress(registerRequest.getAddress());
        userUpdateUser.setEmail(registerRequest.getEmail());
        userUpdateUser.setCreated(registerRequest.getCreated());
        userService.saveOrUpdate(userUpdateUser);
        return ResponseEntity.ok(new MessageResponse("Update successfully!"));
    }

    @GetMapping("/getPagging")

    public ResponseEntity<Map<String, Object>> getPagging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Users> pageBook = userService.getPagination(pageable);
        Map<String, Object> data = new HashMap<>();
        data.put("user", pageBook.getContent());
        data.put("total", pageBook.getSize());
        data.put("totalItems", pageBook.getTotalElements());
        data.put("totalPages", pageBook.getTotalPages());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @PostMapping("/block/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> blockUser(@PathVariable("userId") int userID) {
        Users userBlock = userService.findByID(userID);
        userBlock.setUserStatus(false);
        userService.saveOrUpdate(userBlock);
        return ResponseEntity.ok("Tài Khoản bị khóa !");
    }

    @PatchMapping("/unblock/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> unblockUser(@PathVariable("userId") int userId) {
        Users usersUnBlock = userService.findByID(userId);
        usersUnBlock.setUserStatus(true);
        userService.saveOrUpdate(usersUnBlock);
        return ResponseEntity.ok("Tài Khoản đã được mở  !");

    }

    @GetMapping("/filter/{option}")
    public List<Users> listFilter(@PathVariable("option") Integer option) {
        return userService.listFilter(option);
    }

    @GetMapping("/sort")
    public List<Users> sortUser(@RequestParam("userName") String userName) {
        List<Users> listSort = userService.sortByName(userName);
        return listSort;
    }

    @PutMapping("addWishList/{productId}")
    public ResponseEntity<?> addToWishList(@PathVariable("productId") int productId){
        Product product = productService.findById(productId);
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = userService.findByID(customUserDetails.getUserId());
        user.getListProduct().add(product);
        try{
            user = userService.saveOrUpdate(user);
            return ResponseEntity.ok("Sản phảm đã được thêm vào danh sách yêu thích ");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok("Chưa thêm được vào danh sách yêu thích");
        }
    }

    @DeleteMapping ("removeWishlist/{productId}")
    public ResponseEntity<?> removeWishlist(@PathVariable("productId")int productId){
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = userService.findByID(customUserDetails.getUserId());
        for (Product product:user.getListProduct()){
            if (product.getProductId()==productId){
                user.getListProduct().remove(productService.findById(productId));
                break;
            }
        }
        try {
            user = userService.saveOrUpdate(user);
            return  ResponseEntity.ok("Đã loại bỏ khỏi danh sách yêu thích ");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok("Đang bị lỗi trong quá trình xủ lý thử lại sau");
        }
    }


}

