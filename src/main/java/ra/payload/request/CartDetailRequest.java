package ra.payload.request;

import lombok.Data;

@Data
public class CartDetailRequest {
    private int productId;
    private int quantity;
    private int cartId;
    private float price;
}
