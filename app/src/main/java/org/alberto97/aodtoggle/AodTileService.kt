package org.alberto97.aodtoggle

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import org.alberto97.aodtoggle.TileServiceExtensions.collapseQSPanel
import org.alberto97.aodtoggle.config.AmbientDisplayConfiguration
import org.alberto97.aodtoggle.config.Settings
import org.alberto97.aodtoggle.permissions.GrantWriteSecureSettingsUseCase
import org.alberto97.aodtoggle.permissions.ShizukuStatus
import org.alberto97.aodtoggle.permissions.ShizukuUtils
import rikka.shizuku.Shizuku

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
            handleMissingPermission()
        }
    }

    private fun handleMissingPermission() {
        when (ShizukuUtils.hasPermission()) {
            ShizukuStatus.PERM_GRANTED -> handleShizukuPermissionGranted()
            ShizukuStatus.PERM_NOT_GRANTED -> requestShizukuPermission()
            // TODO: Inform the user to install shizuku or use ADB
            ShizukuStatus.SERVICE_STOPPED -> showPermissionDialog()
        }
    }

    private fun requestShizukuPermission() {
        collapseQSPanel()
        Shizuku.requestPermission(0)
        Shizuku.addRequestPermissionResultListener { _, grantResult ->
            val hasPermission = grantResult == PackageManager.PERMISSION_GRANTED
            if (hasPermission) {
                handleShizukuPermissionGranted()
            } else {
                // TODO: Inform the user to grant shizuku perm
                showPermissionDialog()
            }
        }
    }

    private fun handleShizukuPermissionGranted() {
        val grantWriteSecureSettingsUseCase = GrantWriteSecureSettingsUseCase()
        val granted = grantWriteSecureSettingsUseCase.execute(baseContext)
        if (granted) {
            toggleAod()
        } else {
            showPermissionDialog()
        }
    }

    private fun showPermissionDialog() {
        val msg1 = getString(R.string.grant_msg1)
        val msg2 = getString(R.string.grant_msg2, this.packageName, Manifest.permission.WRITE_SECURE_SETTINGS)
        val dialog = AlertDialog.Builder(this)
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
            android.provider.Settings.Secure.getInt(contentResolver, Settings.DOZE_ALWAYS_ON) == 1
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