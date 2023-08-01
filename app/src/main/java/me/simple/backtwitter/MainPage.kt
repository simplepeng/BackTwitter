package me.simple.backtwitter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        var showSnackBar by remember {
            mutableStateOf(false)
        }
        var snackBarText by remember {
            mutableStateOf("")
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var appName by remember {
                mutableStateOf("Twitter")
            }

            OutlinedTextField(value = appName, onValueChange = {
                appName = it
            })

            Button(
                modifier = Modifier.padding(top = 10.dp),
                onClick = {
                    Helper.createIcon(appName, onSuccess = {
                        showSnackBar = true
//                        snackBarText = "设置成功"
                    }, onFail = {
                        showSnackBar = true
                        snackBarText = "设置失败，似乎系统版本过低"
                    })
                }) {
                Text(text = "Add")
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