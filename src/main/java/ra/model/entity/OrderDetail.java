package ra.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="orderDetail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oderDetailId")
    private int oderDetailId;
    @Column(name="quantity")
    private int quantity;
    @Column(name="price")
    private float price;
    @Column(name="totalPrice")
    private float totalPrice;
    @Column(name="orderStatus")
    private int orderStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "cartId")
    private Cart cart;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "productId")
    private Product product;



}
