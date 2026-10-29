package com.rohitchauhan.spendmate.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rohitchauhan.spendmate.R
import com.rohitchauhan.spendmate.ui.theme.alata_reguler
import com.rohitchauhan.spendmate.ui.theme.appColor
import com.rohitchauhan.spendmate.ui.viewModels.SettingScreenVM
import com.rohitchauhan.spendmate.utils.ResultState
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NotUploadedMessage(viewModel: SettingScreenVM) {
    val dataFormatter = remember { SimpleDateFormat("dd MMM yyyy hh:mm:ss", Locale.getDefault()) }
    val lastSync = viewModel.lastSyncTime.collectAsStateWithLifecycle().value
    val formatedDate = if(lastSync==0L) "Not Synced" else dataFormatter.format(lastSync)
    val context = LocalContext.current
    var isDownloading by  remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }
    val isAnythingToSync = viewModel.isAnythingToSync()
    val image = if(isAnythingToSync) R.drawable.pending_sync else R.drawable.all_synced
    val text = if(isAnythingToSync) "Not uploaded to Cloud" else "Uploaded to Cloud"
    LaunchedEffect(Unit) {
        viewModel.uploadState.collect { result->
            when(result){
                is ResultState.Error ->{
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    isUploading=false
                }
                ResultState.Idle -> {}
                ResultState.Loading -> {
                    isUploading=true
                }
                is ResultState.Success-> {
                    isUploading=false
                    Toast.makeText(context, "Upload success", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.downloadState.collect { result->
            when(result){
                is ResultState.Error ->{
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    isDownloading=false
                }
                ResultState.Idle -> {}
                ResultState.Loading -> {
                    isDownloading=true
                }
                is ResultState.Success-> {
                    isDownloading=false
                    Toast.makeText(context, "Download success", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = "cloud image",
            modifier = Modifier.size(250.dp)
        )
        Text(
            text,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontFamily = alata_reguler,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            "by uploading all the transaction data uploaded to cloud and you can access/view all the data even your app will unInstall",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp),
            fontSize = 13.sp,
            fontFamily = alata_reguler,
            color = Color.Gray
        )
        Text(
            "last sync: $formatedDate",
            fontFamily = alata_reguler,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 30.dp)
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = {
                    if (viewModel.isLoggedIn()) {
                        viewModel.uploadTransactions()
                    } else {
                        Toast.makeText(
                            context,
                            "login/signup to upload transactions",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = appColor
                ),
                shape = RoundedCornerShape(24.dp),
                elevation = ButtonDefaults.buttonElevation(
                    4.dp
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if(isUploading){
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp)
                        )
                    }else{
                        Icon(
                            painter = painterResource(R.drawable.upload),
                            contentDescription = "upload image",
                            tint = Color.White
                        )
                    }

                    Text(" Upload to Cloud", fontSize = 12.sp)
                }
            }
            Button(
                onClick = {
                    viewModel.downloadTransactions()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    4.dp
                ),
                shape = RoundedCornerShape(24.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if(isDownloading){
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp)
                        )
                    }else{
                        Image(
                            painter = painterResource(R.drawable.download_icon),
                            contentDescription = "download image"
                        )
                    }
                    Text("  Download from Cloud", color = appColor, fontSize = 12.sp)
                }
            }
        }
    }
}
