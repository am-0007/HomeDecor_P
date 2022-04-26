package HomeDecor.user;

public enum UserPermission {
    USER_GETPRODUCT("user:getProduct"),
    USER_RATEPRODUCT("user:rateProduct"),
    USER_READCART("user:readCart"),
    USER_SAVECART("user:saveCart"),
    ADMIN_ADDPRODUCT("admin:addProduct"),
    ADMIN_EDITPRODUCT("admin:editProduct"),
    ADMIN_REMOVEPRODUCT("admin:removeProduct");


    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
