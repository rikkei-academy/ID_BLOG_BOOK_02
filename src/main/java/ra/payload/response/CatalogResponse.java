package ra.payload.response;

import lombok.Data;
import ra.model.entity.Product;

import java.util.ArrayList;
import java.util.List;

@Data
public class CatalogResponse {
    private int catalogId;
    private String catalogName;
    private Boolean catalogStatus;
    private List<Product> productList = new ArrayList<>();

}
