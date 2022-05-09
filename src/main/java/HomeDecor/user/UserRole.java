package HomeDecor.user;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static HomeDecor.user.UserPermission.*;
public enum UserRole {
    USER(Sets.newHashSet(USER_SAVECART, USER_GETPRODUCT, USER_RATEPRODUCT, USER_READCART)),
    ADMIN(Sets.newHashSet(ADMIN_ADDPRODUCT, ADMIN_EDITPRODUCT, ADMIN_REMOVEPRODUCT, ADMIN_GETPRODUCT)),
    SUPERADMIN(Sets.newHashSet(USER_GETPRODUCT, USER_READCART, ADMIN_EDITPRODUCT,
            ADMIN_REMOVEPRODUCT, SUPERADMIN_REMOVEUSER, SUPERADMIN_BANUSER,
            SUPERADMIN_EDITSTATUS, SUPERADMIN_GETPRODUCTBYSTATUS));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions()
                .stream()
                .map(userPermission -> new SimpleGrantedAuthority(userPermission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
