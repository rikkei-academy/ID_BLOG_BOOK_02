package ra.payload.response;

import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class OrderResponse {
    private int orderId;
    private float subTotal;
    private float totalAmount;
    private String firstName;
    private String lastName;
    private boolean cartStatus;
    private int quantity;
    private int totalOrder;
    private Date createDate;
    private String email;
    private String address;
    private String city;
    private String zipCode;
    private int userId;
    private Set<Integer> listCart = new HashSet<>();
}
