package org.alberto97.aodtoggle.permissions

import android.annotation.TargetApi
import android.content.Context
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

    fun grantRuntimePermission(packageName: String, permissionName: String, userId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            return Api34Impl.grantRuntimePermission(packageName, permissionName, userId)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return Api30Impl.grantRuntimePermission(packageName, permissionName, userId)

        return LegacyImpl.grantRuntimePermission(packageName, permissionName, userId)
    }

    @TargetApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    object Api34Impl {
        private const val PERSISTENT_DEVICE_ID_DEFAULT = "default:" + Context.DEVICE_ID_DEFAULT

        private val permissionManager: IPermissionManager by lazy {
            HiddenApiBypass.addHiddenApiExemptions("Landroid/permission")
            IPermissionManager.Stub.asInterface(
                ShizukuBinderWrapper(SystemServiceHelper.getSystemService(PERMISSION_MANAGER_SERVICE))
            )
        }

        fun grantRuntimePermission(packageName: String, permissionName: String, userId: Int) {
            try {
                // Android 14 QPR3+
                permissionManager.grantRuntimePermission(
                    packageName,
                    permissionName,
                    PERSISTENT_DEVICE_ID_DEFAULT,
                    userId
                )
                return
            } catch (e: NoSuchMethodError) {
                // No-op
            }

            try {
                // Android 14 QPR2
                permissionManager.grantRuntimePermission(
                    packageName,
                    permissionName,
                    Context.DEVICE_ID_DEFAULT,
                    userId
                )
                return
            } catch (e2: NoSuchMethodError) {
                // No-op
            }

            // Android 14 initial release
            permissionManager.grantRuntimePermission(packageName, permissionName, userId)
        }
    }

    @TargetApi(Build.VERSION_CODES.R)
    object Api30Impl {
        private val permissionManager: IPermissionManager by lazy {
            HiddenApiBypass.addHiddenApiExemptions("Landroid/permission")
            IPermissionManager.Stub.asInterface(
                ShizukuBinderWrapper(SystemServiceHelper.getSystemService(PERMISSION_MANAGER_SERVICE))
            )
        }

        fun grantRuntimePermission(packageName: String, permissionName: String, userId: Int) {
            permissionManager.grantRuntimePermission(packageName, permissionName, userId)
        }
    }

    object LegacyImpl {
        private val packageManager = IPackageManager.Stub.asInterface(
            ShizukuBinderWrapper(SystemServiceHelper.getSystemService(PACKAGE_MANAGER_SERVICE))
        )

        fun grantRuntimePermission(packageName: String, permissionName: String, userId: Int) {
            packageManager.grantRuntimePermission(packageName, permissionName, userId)
        }
    }
}
