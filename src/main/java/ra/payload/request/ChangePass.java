package ra.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePass {
    private String oldPassword;
    private String password;

    public ChangePass(String oldPassword, String password) {
        this.oldPassword = oldPassword;
        this.password = password;
    }
}
