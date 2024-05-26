package android.permission;

interface IPermissionManager {
    void grantRuntimePermission(String packageName, String permissionName, int userId);
}
