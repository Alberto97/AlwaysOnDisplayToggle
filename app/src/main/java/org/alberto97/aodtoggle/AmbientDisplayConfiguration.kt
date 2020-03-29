package org.alberto97.aodtoggle

import android.content.res.Resources
import android.text.TextUtils
import android.util.Log

// In a real world where every manufacturer modify the Android framework its own way
// this is probably not enough to make sure AOD is available.
// I have a Pixel though.
class AmbientDisplayConfiguration {

    fun isAvailable(): Boolean {
        return isAodAvailable() && isDozeAvailable()
    }

    private fun isAodAvailable(): Boolean {
        val available = try {
            val res = getAndroidIdentifier("config_dozeAlwaysOnDisplayAvailable", "bool")
            Resources.getSystem().getBoolean(res)
        } catch (e: Exception) {
            false
        }
        Log.d("package", "AOD available: $available")
        return available
    }

    private fun isDozeAvailable(): Boolean {
        val component = try {
            val res = getAndroidIdentifier("config_dozeComponent", "string")
            Resources.getSystem().getString(res)
        } catch (e: Exception) {
            null
        }
        val available = !TextUtils.isEmpty(component)
        Log.d("package", "Ambient Display component: $component - available: $available")
        return available
    }

    private fun getAndroidIdentifier(name: String, defType: String): Int {
        return Resources.getSystem().getIdentifier(name, defType, "android")
    }
}