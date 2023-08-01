package me.simple.backtwitter

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage() {
    val titleName = LocalContext.current.getString(R.string.add_text)
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

        Text(
            text = titleName,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 15.dp)
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
                        contentDescription = null
                    )
                    Checkbox(
                        checked = isFirstChecked,
                        onCheckedChange = {
                            isFirstChecked = true
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(5.dp)
                    )
                }
                Box {
                    Image(
                        painter = painterResource(id = R.mipmap.ic_launcher_twitter_round),
                        contentDescription = null
                    )
                    Checkbox(
                        checked = !isFirstChecked,
                        onCheckedChange = {
                            isFirstChecked = false
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(5.dp)
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
                    Helper.createIcon(appName, onSuccess = {
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