package ra.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class UserUpdate {
    private Set<String> listRoles;
    public UserUpdate (Set<String> listRoles){
        this.listRoles =listRoles;
    }
}
