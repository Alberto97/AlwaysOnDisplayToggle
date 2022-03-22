package org.alberto97.aodtoggle

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AlertDialog

class AodTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()

        val config = AmbientDisplayConfiguration()
        if (!config.isAvailable()) {
            val tile = qsTile

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                tile.subtitle = getString(R.string.unsupported_device)

            tile.state = Tile.STATE_UNAVAILABLE
            tile.updateTile()
            return
        }

        val enabled = isEnabled()
        setActive(enabled)
    }

    override fun onClick() {
        super.onClick()

        if (hasPermission()) {
            toggleAod()
        } else {
            showPermissionDialog()
        }
    }

    private fun showPermissionDialog() {
        val ctx = ContextThemeWrapper(this, R.style.Theme_AppCompat_DayNight)
        val msg1 = getString(R.string.grant_msg1)
        val msg2 = getString(R.string.grant_msg2, this.packageName, Manifest.permission.WRITE_SECURE_SETTINGS)
        val dialog = AlertDialog.Builder(ctx)
            .setMessage("${msg1}\n${msg2}")
            .setNeutralButton(android.R.string.ok, null)
            .create()

        showDialog(dialog)
    }

    private fun hasPermission(): Boolean {
        val writeSecureSettings = checkSelfPermission(Manifest.permission.WRITE_SECURE_SETTINGS)
        val granted = writeSecureSettings == PackageManager.PERMISSION_GRANTED
        Log.d("package", "${Manifest.permission.WRITE_SECURE_SETTINGS} granted: $granted")

        return granted
    }

    private fun toggleAod() {
        val enabled = !isEnabled()
        setEnabled(enabled)
        setActive(enabled)
    }

    private fun setActive(active: Boolean) {
        val tile = qsTile
        tile.state = if (active) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.updateTile()
    }

    private fun isEnabled(): Boolean {
        val enabled = try {
            return android.provider.Settings.Secure.getInt(this.contentResolver,
                Settings.DOZE_ALWAYS_ON
            ) == 1
        } catch (e: Exception) {
            false
        }
        Log.d("package", "AOD enabled: $enabled")
        return enabled
    }

    private fun setEnabled(state: Boolean): Boolean {
        val intState = if (state) 1 else 0
        android.provider.Settings.Secure.putInt(this.contentResolver,
            Settings.DOZE_ALWAYS_ON, intState)
        return isEnabled()
    }
}