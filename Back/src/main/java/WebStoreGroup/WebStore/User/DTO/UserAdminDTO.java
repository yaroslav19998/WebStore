package WebStoreGroup.WebStore.User.DTO;


import WebStoreGroup.WebStore.User.Role;

import java.util.List;

public interface UserAdminDTO extends UserInfoDTO{
    List<Role> getRoles();
}
