package ra.payload.response;

import lombok.Data;
import ra.model.entity.Catalog;
import ra.model.entity.Comment;
import ra.model.entity.Tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProductResponse {
    private int productId;
    private String productName;
    private float productPrice;
    private String productAuthor;
    private int productQuantity;
    private String productDescription;
    private Date productBirthOfDate;
    private boolean productStatus;
    private String productImg;
    private String productLanguage;
    private String productCompany;
    private List<Comment> listComment = new ArrayList<>();
    private List<Tag> tagList= new ArrayList<>();
    private Catalog catalog;
}
