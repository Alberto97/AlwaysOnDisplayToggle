package org.alberto97.aodtoggle

import android.app.AlertDialog
import android.service.quicksettings.TileService

object TileServiceExtensions {

    fun TileService.collapseQSPanel() {
        // Show dialog and dismiss it immediately as workaround to close the QS panel
        val dialog = AlertDialog.Builder(this)
            .create()

        showDialog(dialog)
        dialog.dismiss()
    }
}