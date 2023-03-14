package ra.model.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "cartId")
    private int cartId;
    @JoinColumn(name = "FirstName")
    private String firstName;
    @JoinColumn(name = "LastName")
    private String lastName;
    @Column(name = "cartStatus")
    private boolean cartStatus;
    @Column(name = "quantity")
    private int quantity;
    @JoinColumn(name = "totalCart")
    private int totalCart;
    @JoinColumn(name = "createDate")
    private Date createDate;
    @Column(name="Email")
    private String email;
    @Column (name = "Address")
    private String address;
    @Column(name="City")
    private String city;
    @JoinColumn(name = "Zipcode")
    private String zipCode;
    @ManyToOne(fetch = FetchType.EAGER)
    private Users users;
    @OneToMany(mappedBy = "cart")
    private List<OrderDetail> listOrderDetail = new ArrayList<>();

}
