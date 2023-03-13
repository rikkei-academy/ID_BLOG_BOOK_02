package ra.model.entity;

import lombok.Data;

@Data
public class ResponseObject {
    private String status;
    private String message;
    private Object data;
}
