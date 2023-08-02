package me.simple.backtwitter

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat

//https://blog.csdn.net/sziitjin/article/details/105724275
object Helper {

    private const val twitterPackageName = "com.twitter.android"

    fun createIcon(
        context: Context = App.context,
        name: String = "Twitter",
        iconRes: Int = R.mipmap.ic_launcher_twitter,
        shortcutId: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        val launchIntent = App.context.packageManager.getLaunchIntentForPackage(twitterPackageName)
        if (launchIntent == null) {
            onFail.invoke()
            return
        }

        launchIntent.setAction(Intent.ACTION_VIEW)
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val shortcutInfo = ShortcutInfoCompat.Builder(App.context, shortcutId)
            .setShortLabel(name)
            .setLongLabel(name)
            .setIcon(IconCompat.createWithResource(App.context, iconRes))
            .setIntent(launchIntent)
            .setLongLived(true)
            .build()

        ShortcutManagerCompat.pushDynamicShortcut(App.context, shortcutInfo)

        val resultIntent = ShortcutManagerCompat.createShortcutResultIntent(App.context, shortcutInfo)
        val successCallback = PendingIntent.getBroadcast(
            App.context,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        val succeeded = ShortcutManagerCompat.requestPinShortcut(context, shortcutInfo, successCallback.intentSender)
        if (succeeded) onSuccess.invoke() else onFail.invoke()
    }

}