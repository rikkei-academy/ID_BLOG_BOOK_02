package ra.payload.response;

import lombok.Data;
import ra.model.entity.Catalog;

import java.util.Date;

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
    private Catalog catalog;
}
