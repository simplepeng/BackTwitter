package me.simple.backtwitter

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N_MR1)
object Helper {

    private val shortcutManager: ShortcutManager by lazy {
        App.context.getSystemService(
            ShortcutManager::class.java
        )
    }

    fun createIcon(
        name: String = "Twitter",
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (shortcutManager.isRequestPinShortcutSupported) {
                val intent: Intent = Intent(App.context, MainActivity::class.java)
                intent.setAction(Intent.ACTION_VIEW)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                val pinShortcutInfo = ShortcutInfo.Builder(App.context, "twitter_shortcut_id")
                    .setShortLabel(name)
                    .setLongLabel(name)
                    .setIcon(Icon.createWithResource(App.context, R.mipmap.ic_launcher))
                    .setIntent(intent)
                    .build()

                val pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(pinShortcutInfo)
                val successCallback = PendingIntent.getBroadcast(
                    App.context,
                    0,
                    pinnedShortcutCallbackIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                )

                val success = shortcutManager.requestPinShortcut(pinShortcutInfo, successCallback.intentSender)
                if (success) onSuccess.invoke() else onFail.invoke()
            } else {
                onFail.invoke()
            }
        } else {
            onFail.invoke()
        }
    }

}