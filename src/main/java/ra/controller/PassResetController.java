
package ra.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.PasswordResetToken;
import ra.model.entity.Users;
import ra.model.sendEmail.ProvideSendEmail;
import ra.model.service.PassResetService;
import ra.model.service.UserService;
import ra.payload.response.MessageResponse;
import ra.security.CustomUserDetails;
import ra.security.CustomUserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;


@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/v1/passReset")
public class PassResetController {
    private PasswordEncoder encoder;
    private ProvideSendEmail provideSendEmail;
    private PassResetService passResetService;
    private UserService userService;
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping("/forgotPassword")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String userEmail, HttpServletRequest request) {
        if (userService.existsByEmail(userEmail)) {
            Users users = (Users) userService.findByEmail(userEmail);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(users.getUserName());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = UUID.randomUUID().toString();
            PasswordResetToken myToken = new PasswordResetToken();
            myToken.setToken(token);
            String mess = "token is valid for 5 minutes.\n" + "Your token: " + token;
            myToken.setUsers(users);
            Date now = new Date();
            myToken.setStartDate(now);
            passResetService.saveOrUpdate(myToken);
            provideSendEmail.sendSimpleMessage(users.getEmail(),
                    "Reset your password", mess);
            return ResponseEntity.ok("Đã gửi rồi đấy  ! Vui lòng kiểm tra email của bạn");
        } else {
            return new ResponseEntity<>(new MessageResponse("Email có đúng không "), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/creatNewPass")
    public ResponseEntity<?> creatNewPass(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PasswordResetToken passwordResetToken = passResetService.getLastTokenByUserId(userDetails.getUserId());
        long date1 = passwordResetToken.getStartDate().getTime() + 1800000;
        long date2 = new Date().getTime();
        if (date2 > date1) {
            return new ResponseEntity<>(new MessageResponse("Expired Token "), HttpStatus.EXPECTATION_FAILED);
        } else {
            if (passwordResetToken.getToken().equals(token)) {
                Users users = (Users) userService.findByID(userDetails.getUserId());
                users.setPassword(encoder.encode(newPassword));
                userService.saveOrUpdate(users);
                return new ResponseEntity<>(new MessageResponse("Cập nhật mật khẩu thành công "), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse("token is fail "), HttpStatus.NO_CONTENT);
            }
        }
    }
}
