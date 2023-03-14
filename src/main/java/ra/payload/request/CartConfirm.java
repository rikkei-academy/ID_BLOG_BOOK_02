package ra.payload.request;

import lombok.Data;

@Data
public class CartConfirm {
    private int cartId;
    private String address;
    private String city;
    private int discount;
    private String state;
    private String phone;
    private String firstName;
    private String lastName;
    private String email;
    private String postCode;
    private float total;
    private String note;
}
