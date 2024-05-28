package com.example.codsoftuniversityattendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetPasswordScreen(navController: NavController, logInAndCreateViewModel: LogInAndCreateViewModel = viewModel()) {
    val  resetMessage by logInAndCreateViewModel.resetStatusMessage.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Reset Password") },
                navigationIcon =  {
                    IconButton(onClick = {
                        navController.navigate(Screens.LogInScreen.name)
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        
                    }
                }
            )
        }

    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(
                Color.DarkGray
            )
            .padding(it)
            .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){

            Text(text = resetMessage, color = Color.Yellow)

            Text(text = "Reset Password", fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif, color = Color.White)
            Spacer(modifier = Modifier.height(64.dp))
            OutlinedTextField(
                value = logInAndCreateViewModel.emailForReset,
                onValueChange = { logInAndCreateViewModel.emailForReset = it },
                label = { Text(text ="enter the email", color = Color.Green) },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {logInAndCreateViewModel.resetPassword(logInAndCreateViewModel.emailForReset)}) {
                Text(text = "Reset")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewForget() {
    ForgetPasswordScreen(navController = rememberNavController())
}