package ra.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="cartDetail")
public class CartDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartDetailId")
    private int cartDetailId;
    @Column (name = "Price")
    private float price;
    @Column(name = "Quantity")
    private int quantity;
    @Column (name = "Status")
    private boolean statusCartDetail;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "cartId")
    private Carts carts;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "productId")
    private Product product;

}
