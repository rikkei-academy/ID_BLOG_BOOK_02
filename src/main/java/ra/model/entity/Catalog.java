package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="catalog")
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="CatalogId")
    private int catalogId;

    @Column(name = "CatalogName")
    private String catalogName;

    @Column(name= "CatalogStatus")
    private Boolean catalogStatus;

    @OneToMany(mappedBy = "catalog")
    @JsonIgnore
    private List<Product> productList = new ArrayList<>();

}
