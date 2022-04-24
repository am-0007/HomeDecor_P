package HomeDecor.user;

public enum UserPermission {
    USER_GETPRODUCT("user:getProduct"),
    USER_RATEPRODUCT("user:rateProduct"),
    USER_CARTLIST("user:CartList"),
    ADMIN_PRODUCT("admin:product");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
