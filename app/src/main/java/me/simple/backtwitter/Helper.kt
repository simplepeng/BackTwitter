package me.simple.backtwitter

import android.app.AppOpsManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat

//https://blog.csdn.net/sziitjin/article/details/105724275
object Helper {

    private const val twitterPackageName = "com.twitter.android"
    private const val START_ACTIVITY = "com.twitter.android.StartActivity"
    private const val MAIN_ACTIVITY = "com.twitter.app.main.MainActivity"

    fun getTwitterIntent(): Intent {
        val intent = getLaunchTwitterIntent()
        if (intent != null) return intent
        return getStaticTwitterIntent()
    }

    private fun getLaunchTwitterIntent() = App.context.packageManager.getLaunchIntentForPackage(twitterPackageName)

    private fun getStaticTwitterIntent() = Intent().apply {
        addCategory("android.intent.category.LAUNCHER")
        action = "android.intent.action.MAIN"
        component = ComponentName("com.twitter.android", "com.twitter.android.StartActivity")
    }

    private fun getProxyIntent() = Intent(App.context, ProxyActivity::class.java)

    fun createIcon(
        context: Context = App.context,
        name: String = "Twitter",
        iconRes: Int = R.mipmap.ic_launcher_twitter,
        shortcutId: String,
        unSupport: (() -> Unit)? = null,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        if (!hasShortcutPermissionOnMIUI(context)) {
            unSupport?.invoke()
            return
        }
        if (!ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            unSupport?.invoke()
            return
        }

        val launchIntent = getProxyIntent()

        launchIntent.setAction(Intent.ACTION_VIEW)
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val shortcutInfo = ShortcutInfoCompat.Builder(App.context, shortcutId)
            .setShortLabel(name)
            .setIcon(IconCompat.createWithResource(App.context, iconRes))
            .setIntent(launchIntent)
            .setLongLived(true)
            .build()

//        ShortcutManagerCompat.pushDynamicShortcut(App.context, shortcutInfo)

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

//    private val twitterAliasComponent = ComponentName(App.context, App.context.packageName + ".ProxyActivity1")
//    private val twitterRoundAliasComponent = ComponentName(App.context, App.context.packageName + ".ProxyActivity2")
//
//    fun enableIcon() {
//        App.context.packageManager.setComponentEnabledSetting(
//            twitterAliasComponent,
//            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//            PackageManager.DONT_KILL_APP
//        )
//    }

    //0 有权限，1 没权限，5 询问
    private fun checkShortcutPermissionOnMIUI(context: Context): Int {
        return try {
            val mAppOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val pkg = context.applicationContext.packageName
            val uid = context.applicationInfo.uid
            val appOpsClass = Class.forName(AppOpsManager::class.java.name)
            val checkOpNoThrowMethod = appOpsClass.getDeclaredMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String::class.java)
            val invoke = checkOpNoThrowMethod.invoke(mAppOps, 10017, uid, pkg)
            if (invoke == null) {
                Log.e("ShortcutPermission", "MIUI check permission checkOpNoThrowMethod(AppOpsManager) invoke result is null")
                return -1
            }
            invoke as Int
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    private fun isMIUI() = Build.MANUFACTURER.equals("Xiaomi", ignoreCase = true) || Build.DISPLAY.contains("MIUI")

    private fun hasShortcutPermissionOnMIUI(context: Context) = isMIUI() && checkShortcutPermissionOnMIUI(context) != 1

    fun openSettingPage(context: Context) {
        val intent = Intent().apply {
            action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.parse("package:" + context.packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}