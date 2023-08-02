package me.simple.backtwitter

import android.widget.Toolbar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage() {
    val titleName = LocalContext.current.getString(R.string.app_name)
    val failText = LocalContext.current.getString(R.string.set_fail)

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        var showSnackBar by remember {
            mutableStateOf(false)
        }
        var snackBarText by remember {
            mutableStateOf("")
        }

        TopAppBar(
            title = {
                Text(
                    text = titleName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }, modifier = Modifier.align(Alignment.TopCenter)
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var appName by remember {
                mutableStateOf("Twitter")
            }
            var isFirstChecked by remember { mutableStateOf(true) }

            Row {
                Box {
                    Image(
                        painter = painterResource(id = R.mipmap.ic_launcher_twitter),
                        contentDescription = null,
                        modifier = Modifier.padding(18.dp)
                    )
                    Checkbox(
                        checked = isFirstChecked,
                        onCheckedChange = {
                            isFirstChecked = true
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                    )
                }
                Box {
                    Image(
                        painter = painterResource(id = R.mipmap.ic_launcher_twitter_round),
                        contentDescription = null,
                        modifier = Modifier.padding(18.dp)
                    )
                    Checkbox(
                        checked = !isFirstChecked,
                        onCheckedChange = {
                            isFirstChecked = false
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                    )
                }
            }
            OutlinedTextField(
                modifier = Modifier.padding(top = 20.dp),
                value = appName,
                onValueChange = {
                    appName = it
                })

            Button(
                modifier = Modifier.padding(top = 15.dp),
                onClick = {
                    Helper.createIcon(
                        context = App.context,
                        name = appName,
                        iconRes = if (isFirstChecked) R.mipmap.ic_launcher_twitter else R.mipmap.ic_launcher_twitter_round,
                        shortcutId = if (isFirstChecked) "twitter_shortcut_id" else "twitter_round_shortcut_id",
                        onSuccess = {
//                        showSnackBar = true
                        }, onFail = {
                            showSnackBar = true
                            snackBarText = failText
                        })
                }) {
                Text(text = LocalContext.current.getString(R.string.add_text))
            }
        }

        if (showSnackBar) {
            ShowSnackBar(snackBarText, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun ShowSnackBar(
    text: String,
    modifier: Modifier
) {
    Snackbar(modifier = modifier) {
        Text(text = text)
    }
}