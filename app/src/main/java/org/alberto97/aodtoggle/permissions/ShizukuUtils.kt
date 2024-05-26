package org.alberto97.aodtoggle.permissions

import android.content.pm.PackageManager
import rikka.shizuku.Shizuku

enum class ShizukuStatus {
    PERM_GRANTED, PERM_NOT_GRANTED, SERVICE_STOPPED
}

object ShizukuUtils {

    fun hasPermission(): ShizukuStatus {
        return if (Shizuku.getBinder() != null) {
            if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                ShizukuStatus.PERM_GRANTED
            } else {
                ShizukuStatus.PERM_NOT_GRANTED
            }
        } else {
            ShizukuStatus.SERVICE_STOPPED
        }
    }
}
