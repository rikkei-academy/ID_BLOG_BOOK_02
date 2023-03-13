package ra.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name="tag")

public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TagId")
    private int tagId;
    @Column(name = "TagStatus")
    private boolean tagStatus;

        @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TagProduct",joinColumns = @JoinColumn(name = "UserId"),
            inverseJoinColumns = @JoinColumn(name = "ProductId"))
    private Set<Product> listProduct = new HashSet<>();
}
