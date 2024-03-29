package HomeDecor.user.ENUM;

public enum UserPermission {
    USER_GETPRODUCT("user:getProduct"),
    USER_RATEPRODUCT("user:rateProduct"),
    USER_READCART("user:readCart"),
    USER_SAVECART("user:saveCart"),
    ADMIN_GETPRODUCT("admin:getProduct"),
    ADMIN_COUNTPRODUCT("admin:countProduct"),
    ADMIN_ADDPRODUCT("admin:addProduct"),
    ADMIN_EDITPRODUCT("admin:editProduct"),
    ADMIN_REMOVEPRODUCT("admin:removeProduct"),
    ADMIN_GETTOTALPRODUCT("admin:getTotalProduct"),
    SUPERADMIN_EDITSTATUS("superAdmin:editStatus"),
    SUPERADMIN_GETPRODUCTBYSTATUS("superAdmin:getProduct"),
    SUPERADMIN_GETUSER("superAdmin:getUser"),
    SUPERADMIN_REMOVEUSER("superAdmin:removeUser"),
    SUPERADMIN_BANUSER("superAdmin:banUser")
    ;


    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
