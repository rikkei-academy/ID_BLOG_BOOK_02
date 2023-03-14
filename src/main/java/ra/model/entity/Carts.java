package ra.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "cart")
public class Carts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartId")
    private int cartId;
    @Column(name = "CartName")
    private String cartName;
    @Column (name = "Discount")
    private int discount;
    @Column (name = "Address")
    private String address;
    @Column(name = "State")
    private String state;
    @Column(name="City")
    private String city;
    @Column(name="PostCode")
    private String postCode;
    @Column(name="Phone")
    private String phone;
    @Column (name="FirstName")
    private String firstName;
    @Column (name= "LastName")
    private String lastName;
    @Column (name="Note")
    private String note;
    @Column(name="Email")
    private String email;
    @Column(name="CreatDate")
    private Date creatDate;
    @Column(name= "Status")
    private int cartStatus;
    @Column(name="total")
    private float total;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    @JsonIgnore
    private Users users;
    @OneToMany(mappedBy = "carts")
    private List<CartDetail> cartDetails = new ArrayList<>();

}
