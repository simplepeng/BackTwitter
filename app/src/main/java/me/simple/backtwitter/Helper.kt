package me.simple.backtwitter

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi

//https://blog.csdn.net/sziitjin/article/details/105724275
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
                val twitterPackageName = "com.twitter.android"
                val launchIntent = App.context.packageManager.getLaunchIntentForPackage(twitterPackageName)
                if (launchIntent == null) {
                    onFail.invoke()
                    return
                }

                launchIntent.setAction(Intent.ACTION_VIEW)
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                val builder = ShortcutInfo.Builder(App.context, "twitter_shortcut_id")
                    .setShortLabel(name)
                    .setLongLabel(name)
                    .setIcon(Icon.createWithResource(App.context, R.mipmap.ic_launcher_twitter))
                    .setIntent(launchIntent)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    builder.setLongLived(true)
                }

                val pinShortcutInfo = builder.build()
                shortcutManager.setDynamicShortcuts(mutableListOf(pinShortcutInfo))

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