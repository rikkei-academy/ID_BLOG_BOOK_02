package ra.payload.request;


import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class RegisterRequest {
    private String userName;
    private String phone;
    private String email;
    private String address;
    private Date created;
    private Set<String> listRoles;

}
