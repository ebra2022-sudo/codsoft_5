package com.example.codsoftuniversityattendance

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun LoginScreen(
    logInAndCreateViewModel: LogInAndCreateViewModel = viewModel(),
    navController: NavController
) {
    val logInSuccess by logInAndCreateViewModel.logInSuccess.collectAsState()
    val logInErrorMessage by logInAndCreateViewModel.loginErrorMessage.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(painter = painterResource(id = R.drawable.log_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)

        Box(modifier = Modifier
            .fillMaxHeight(0.9f)
            .padding(horizontal = 32.dp)
            .align(alignment = Alignment.BottomCenter)) {

            Text(
                text = "Join The\nCommunity",
                fontSize = 30.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.TopStart),
                color = Color(0xFFF305F3),
                lineHeight = 50.sp
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                var passwordVisible by remember { mutableStateOf(false) }

                if (!logInSuccess) {
                    Text(text = logInErrorMessage, color = Color.Red, textAlign = TextAlign.Center)
                }
                Spacer(modifier = Modifier.height(36.dp))

                OutlinedTextField(
                    value = logInAndCreateViewModel.emailForLogin,
                    onValueChange = {
                        logInAndCreateViewModel.emailForLogin = it
                        logInAndCreateViewModel.loginErrorMessage.value = ""
                    },
                    label = { Text(text = "Email", color = Color.Green) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = Color.Black),
                    trailingIcon = if (logInAndCreateViewModel.emailForLogin.isNotEmpty()) {
                        {
                            IconButton(onClick = { logInAndCreateViewModel.emailForLogin = "" }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "", tint = Color.Red)
                            }
                        }
                    } else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        cursorColor = Color.Black,
                        selectionColors = LocalTextSelectionColors.current,
                        focusedBorderColor = Color.Green,
                        unfocusedBorderColor = Color.Blue,
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = logInAndCreateViewModel.passwordForLog,
                    onValueChange = {
                        logInAndCreateViewModel.passwordForLog = it
                        logInAndCreateViewModel.loginErrorMessage.value = ""
                    },
                    label = { Text(text = "Password", color = Color.Green) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = Color.Black),
                    trailingIcon = {
                        val image = if (passwordVisible) painterResource(id = R.drawable.eye) else painterResource(id = R.drawable.eye_off)
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(painter = image, contentDescription = null)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        cursorColor = Color.Black,
                        selectionColors = LocalTextSelectionColors.current,
                        focusedBorderColor = Color.Green,
                        unfocusedBorderColor = Color.Blue,
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = { /* Navigate to reset password screen */ },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    TextButton(onClick = {navController.navigate(Screens.ForgetPassWordScreen.name)}){
                        Text(text = "Forgot password?", color = Color.Blue, textDecoration = TextDecoration.Underline)

                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                            logInAndCreateViewModel.loginUser(
                                email = logInAndCreateViewModel.emailForLogin,
                                password = logInAndCreateViewModel.passwordForLog,
                                onSuccess = { createdFor ->
                                    if (createdFor == Status.Student.name) {
                                        navController.navigate(Screens.HomeScreenForStudent.name) {
                                            popUpTo(Screens.LogInScreen.name) { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate(Screens.HomeScreenForLecturer.name) {
                                            popUpTo(Screens.LogInScreen.name) { inclusive = true }
                                        }
                                    }
                                },
                                onFailure = {
                                }
                            )
                        },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Login", color = Color.White)
                }


                Spacer(modifier = Modifier.height(36.dp))

                TextButton(
                    onClick = { navController.navigate(Screens.CreateAccountScreen.name) },
                ) {
                    Text(text = "Don't have an account? ", color = Color.Black)
                    Text(text = "Sign Up", color = Color.Blue)
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoginPreview() {
    LoginScreen(navController = rememberNavController())
}