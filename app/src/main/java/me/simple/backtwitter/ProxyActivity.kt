package me.simple.backtwitter

import android.os.Bundle
import androidx.activity.ComponentActivity

class ProxyActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val intent = Helper.getTwitterIntent()
        startActivity(intent)

        finish()

        super.onCreate(savedInstanceState)
    }
}