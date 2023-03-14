package ra.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "star")
public class Star {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "StarId")
    private int starId;
    @Column(name = "starPoint")
    private int starPoint;
    @JoinColumn(name = "ProductId")
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    @JoinColumn(name = "UserId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;


}
