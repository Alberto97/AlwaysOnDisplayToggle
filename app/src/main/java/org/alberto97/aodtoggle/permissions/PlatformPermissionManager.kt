package org.alberto97.aodtoggle.permissions

import android.content.pm.IPackageManager
import android.os.Build
import android.permission.IPermissionManager
import org.lsposed.hiddenapibypass.HiddenApiBypass
import rikka.shizuku.ShizukuBinderWrapper
import rikka.shizuku.SystemServiceHelper

class PlatformPermissionManager {

    companion object {
        const val PACKAGE_MANAGER_SERVICE = "package"
        const val PERMISSION_MANAGER_SERVICE = "permissionmgr"
    }

    private val packageManager = IPackageManager.Stub.asInterface(
        ShizukuBinderWrapper(SystemServiceHelper.getSystemService(PACKAGE_MANAGER_SERVICE))
    )

    private val permissionManager: IPermissionManager by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("Landroid/permission")
        }
        IPermissionManager.Stub.asInterface(
            ShizukuBinderWrapper(SystemServiceHelper.getSystemService(PERMISSION_MANAGER_SERVICE))
        )
    }

    fun grantRuntimePermission(packageName: String?, permissionName: String?, userId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionManager.grantRuntimePermission(packageName, permissionName, userId)
        } else {
            packageManager.grantRuntimePermission(packageName, permissionName, userId)
        }
    }
}
