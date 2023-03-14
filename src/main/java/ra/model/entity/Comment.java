package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "comment")


public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "CommentId")
    private int commentId;

    @Column(name="Content")
    private String content;

    @JoinColumn(name="ProductId")
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @JoinColumn(name ="UserId")
    @ManyToOne(fetch =  FetchType.LAZY)
    private  Users users;
}
