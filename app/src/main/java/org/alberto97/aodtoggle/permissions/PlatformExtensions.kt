package org.alberto97.aodtoggle.permissions

import android.os.UserHandle

object PlatformExtensions {

    fun UserHandle.getIdentifier(): Int {
        val getIdentifierMethod = UserHandle::class.java.getMethod("getIdentifier")
        return getIdentifierMethod.invoke(this) as Int
    }

}
