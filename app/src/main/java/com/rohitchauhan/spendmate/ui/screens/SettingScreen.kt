package com.rohitchauhan.spendmate.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.TableInfo
import com.rohitchauhan.spendmate.R
import com.rohitchauhan.spendmate.ui.theme.alata_reguler
import com.rohitchauhan.spendmate.ui.theme.appColor
import com.rohitchauhan.spendmate.ui.viewModels.SettingScreenVM

@Composable
fun SettingScreen(
    onUploadClick:()-> Unit,
    onDownloadClick:()-> Unit
) {
    val context = LocalContext.current
    val viewModel : SettingScreenVM = hiltViewModel()
    val logInState = viewModel.loginState.collectAsStateWithLifecycle()
    val createAccountState = viewModel.createAccountState.collectAsStateWithLifecycle()
    val uploadState = viewModel.uploadState.collectAsStateWithLifecycle()
    val downLoadState = viewModel.downloadState.collectAsStateWithLifecycle()
    val hasPendingUploads = viewModel.hasPendingUploads.collectAsStateWithLifecycle().value
    val isLoggedIn = viewModel.isLoggedIn()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "arrow back"
                    )
                }
                Text(
                    "Settings",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = alata_reguler
                )
            }
            if(isLoggedIn){
                if(hasPendingUploads){
                    NotUploadedMessage(viewModel,context)
                }else{
                    UploadedMessage(viewModel)
                }
            }else{
                NotLoggedInMessage(viewModel,context)
            }
        }
    }
}

@Composable
fun NotUploadedMessage(viewModel: SettingScreenVM,context: Context) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(R.drawable.cloud),
            contentDescription = "cloud image",
            modifier = Modifier.size(250.dp)
        )
        Text(
            "Not uploaded to Cloud",
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
        Spacer(Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = {
                    if(viewModel.isLoggedIn()){
                        viewModel.uploadTransactions()
                    }else{
                        Toast.makeText(context, "login/signup to upload transactions", Toast.LENGTH_SHORT).show()
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
                    Icon(painter = painterResource(R.drawable.upload), contentDescription = "upload image", tint = Color.White)
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
                Row (verticalAlignment = Alignment.CenterVertically){
                    Image(painter = painterResource(R.drawable.download_icon), contentDescription = "download image")
                    Text("  Download from Cloud", color = appColor, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun NotLoggedInMessage(viewModel: SettingScreenVM,context:Context) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(R.drawable.cloud),
            contentDescription = "cloud image",
            modifier = Modifier.size(250.dp)
        )
        Text(
            "Not uploaded to Cloud",
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
        Spacer(Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = {
                    if(viewModel.isLoggedIn()){
                        viewModel.uploadTransactions()
                    }else{
                        Toast.makeText(context, "login/signup to upload transactions", Toast.LENGTH_SHORT).show()
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
                    Icon(painter = painterResource(R.drawable.upload), contentDescription = "upload image", tint = Color.White)
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
                Row (verticalAlignment = Alignment.CenterVertically){
                    Image(painter = painterResource(R.drawable.download_icon), contentDescription = "download image")
                    Text("  Download from Cloud", color = appColor, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun UploadedMessage(viewModel: SettingScreenVM) {

}