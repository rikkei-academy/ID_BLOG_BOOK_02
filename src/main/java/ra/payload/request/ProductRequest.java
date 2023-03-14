package ra.payload.request;

import lombok.Data;
import ra.model.entity.Tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProductRequest {

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
    private int catalogId;
    private List<Tag> tagList= new ArrayList<>();

}
