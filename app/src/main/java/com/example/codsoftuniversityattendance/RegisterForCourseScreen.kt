package com.example.codsoftuniversityattendance

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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


    val courses by logInAndCreateViewModel.subdirectories.collectAsState()
    val registrationMessage by logInAndCreateViewModel.registrationErrorMessage.collectAsState()
    val registeredCourses by logInAndCreateViewModel.registeredCourses.collectAsState()


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
            var showCourses by remember {mutableStateOf(false)}
            Text(text = registrationMessage,
                color = Color.Green)

            Spacer(modifier = Modifier.height(50.dp))
            CustomDropDownMenu(
                expanded = showCourses,
                onExpandedChange = { showCourses = it
                                   },
                selectedItem = logInAndCreateViewModel.selectedCourse,
                onDismissedRequest = { showCourses = false },
                modifier = Modifier.fillMaxWidth(),
                options = courses,
                attachedCompose = {
                    OutlinedTextField(
                        readOnly = true,
                        value = logInAndCreateViewModel.selectedCourse,
                        onValueChange = { },
                        label = { Text(text = "select Course")},
                        trailingIcon = {
                            IconButton(onClick = { showCourses = true }) {
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

            Button(onClick = {
                if (logInAndCreateViewModel.selectedCourse !in registeredCourses) {
                    logInAndCreateViewModel.registerForCourse(
                    selectedCourse = logInAndCreateViewModel.selectedCourse,
                    courses = courses)
                }
                else {
                    logInAndCreateViewModel.registrationErrorMessage.value = "you have already registered for this course"
                }
            }) {
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