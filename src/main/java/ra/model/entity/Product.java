package ra.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductId")
    private int productId;
    @Column(name = "ProductName")
    private String productName;
    @Column(name = "ProductPrice")
    private float productPrice;
    @Column(name="ProductAuthor")
    private String productAuthor;
    @Column(name = "ProductQuantity")
    private int productQuantity;
    @Column(name="ProductDescription")
    private String productDescription;
    @Column(name = "ProductDate")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date productBirthOfDate;
    @Column(name = "ProductStatus")
    private boolean productStatus;
    @Column(name="ProductImg")
    private String productImg;
    @Column(name="ProductLanguage")
    private String productLanguage;
    @Column(name="ProductCompany")
    private String productCompany;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "catalogId")
    private Catalog catalog;
    @OneToMany(mappedBy = "product")
    List <Star> starList = new ArrayList<>();
    @OneToMany(mappedBy = "product")
    List <Comment> commentList = new ArrayList<>();
    @OneToMany(mappedBy ="product")
    @JsonIgnore
    List<CartDetail> orderDetailList = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "tag_Product", joinColumns = @JoinColumn(name = "productId"), inverseJoinColumns = @JoinColumn(name = "tagId"))
    private List<Tag> tagList= new ArrayList<>();
//    @JsonIgnore



}
