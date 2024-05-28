package com.example.codsoftuniversityattendance

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterForCourse(logInAndCreateViewModel: LogInAndCreateViewModel = viewModel(), navController: NavController) {
    val context = LocalContext.current

    // Load courses when the screen is displayed


    val courses by logInAndCreateViewModel.courses.collectAsState()
    val registrationSuccess by logInAndCreateViewModel.courseRegistrationSuccess.collectAsState()

    if (registrationSuccess) {
        Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
       // logInAndCreateViewModel.courseRegistrationSuccess.value = false

    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Register for new course") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                        logInAndCreateViewModel.addCourseStatusMessage.value = ""
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .padding(it)
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CustomDropDownMenu(
                expanded = logInAndCreateViewModel.showCourses,
                onExpandedChange = { logInAndCreateViewModel.showCourses = it },
                selectedItem = logInAndCreateViewModel.selectedCourse,
                onDismissedRequest = { logInAndCreateViewModel.showCourses = false },
                modifier = Modifier.fillMaxWidth(),
                options = courses,
                attachedCompose = {
                    OutlinedTextField(
                        readOnly = true,
                        value = logInAndCreateViewModel.selectedCourse,
                        onValueChange = { },
                        label = {},
                        trailingIcon = {
                            IconButton(onClick = { logInAndCreateViewModel.showCourses = true }) {
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Green)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = Color.White),
                        singleLine = true
                    )
                },
                addLeadingIcon = false
            ) { selectedList ->
                logInAndCreateViewModel.selectedCourse = selectedList
            }

            Spacer(modifier = Modifier.height(100.dp))

            Button(onClick = { logInAndCreateViewModel.registerForCourse(logInAndCreateViewModel.studentIdForUpdate) }) {
                Text(text = "Register")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewForAdd() {
    RegisterForCourse(navController = rememberNavController())
}