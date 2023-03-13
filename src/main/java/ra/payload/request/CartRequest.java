package ra.payload.request;

import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class CartRequest {
    private String firstName;
    private String lastName;
    private boolean cartStatus;
    private int quantity;
    private int totalCart;
    private Date createDate;
    private String email;
    private String address;
    private String city;
    private String zipCode;
    private int userId;
    private Set<Integer> listCart = new HashSet<>();
}
