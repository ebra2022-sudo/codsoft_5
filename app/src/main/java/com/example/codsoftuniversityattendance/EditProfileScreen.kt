package com.example.codsoftuniversityattendance

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    logInAndCreateViewModel: LogInAndCreateViewModel = viewModel(),
    navController: NavController
) {
    val updateErrorMessage by logInAndCreateViewModel.updateErrorMessage.collectAsState()
    var showErrorForFirstName by remember { mutableStateOf(false) }
    var showErrorForMiddleName by remember { mutableStateOf(false) }
    var showErrorForLastName by remember { mutableStateOf(false) }
    var showErrorForId by remember { mutableStateOf(false) }
    var showErrorForSex by remember { mutableStateOf(false) }
    var showErrorForDepartment by remember { mutableStateOf(false) }
    var showErrorForDoB by remember { mutableStateOf(false) }
    var showErrorForPhoneNumber by remember { mutableStateOf(false) }
    var isPressed by  remember { mutableStateOf(false) }
    val colorFirstName = animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.firstNameForUpdate.isEmpty()) {
            showErrorForFirstName = true
            Color.Red
        }
        else {
            showErrorForFirstName = false
            Color.Green

        }, label = ""
    ).value
    val colorMiddleName = animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.middleNameForUpdate.isEmpty()) {
            showErrorForMiddleName = true
            Color.Red
        }
        else {
            showErrorForMiddleName = false
            Color.Green

        }, label = ""
    ).value
    val colorLastName = animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.lastNameForUpdate.isEmpty()) {
            showErrorForLastName = true
            Color.Red
        }
        else {
            showErrorForLastName = false
            Color.Green

        }, label = ""
    ).value
    val colorSex = animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.sexForUpdate.isEmpty()) {
            showErrorForSex = true
            Color.Red
        }
        else {
            showErrorForSex = false
            Color.Green

        }, label = ""
    ).value
    val colorBirthDate = animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.dateOfBirthForUpdate.isEmpty()) {
            showErrorForDoB = true
            Color.Red
        }
        else {
            showErrorForDoB= false
            Color.Green

        }, label = ""
    ).value
    val colorId = animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.studentIdForUpdate.isEmpty()) {
            showErrorForId = true
            Color.Red
        }
        else {
            showErrorForId = false
            Color.Green

        }, label = ""
    ).value
    val colorPhoneNumber = animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.phoneNumberForUpdate.isEmpty()) {
            showErrorForPhoneNumber = true
            Color.Red
        }
        else {
            showErrorForPhoneNumber = false
            Color.Green

        }, label = ""
    ).value
    val colorDepartment = animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.departmentForUpdate.isEmpty()) {
            showErrorForDepartment = true
            Color.Red
        }
        else {
            showErrorForDepartment = false
            Color.Green

        }, label = ""
    ).value



    val dateDialogState = rememberMaterialDialogState()
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("EEE, d MMM yyyy")
                .format(pickedDate)
        }
    }

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                logInAndCreateViewModel.dateOfBirthForUpdate= formattedDate
                LocalDate.of(pickedDate.year, pickedDate.month, pickedDate.dayOfMonth)
            }
            negativeButton(text = "Cancel")
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pick a date"
        ) { localDate ->
            pickedDate = localDate
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Your Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp()
                    logInAndCreateViewModel.updateErrorMessage.value= ""}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = updateErrorMessage)
            Spacer(modifier = Modifier.height(50.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)){
                OutlinedTextField(
                    value = logInAndCreateViewModel.firstNameForUpdate,
                    onValueChange = { logInAndCreateViewModel.firstNameForUpdate = it },
                    label = {
                        Text(
                            text = if (showErrorForFirstName) "F.Name is required!" else "First Name",
                            color = colorFirstName,
                            fontSize = 12.sp
                        )
                    },
                    modifier = Modifier.weight(0.5f),
                    textStyle = TextStyle(color = Color.White),
                    isError = showErrorForFirstName,
                    singleLine = true,

                    )


                OutlinedTextField(
                    value = logInAndCreateViewModel.middleNameForUpdate,
                    onValueChange = { logInAndCreateViewModel.middleNameForUpdate = it },
                    label = {
                        Text(
                            text = if (showErrorForMiddleName) "M.Name is required!" else "Middle Name",
                            color = colorMiddleName,
                            fontSize = 12.sp
                        )
                    },
                    modifier = Modifier.weight(0.5f),
                    textStyle = TextStyle(color = Color.White),
                    isError = showErrorForMiddleName,
                    singleLine = true,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = logInAndCreateViewModel.lastNameForUpdate,
                    onValueChange = { logInAndCreateViewModel.lastNameForUpdate = it },
                    label = {
                        Text(
                            text = if (showErrorForLastName) "L.Name is required! " else "Last Name",
                            color = colorLastName,
                            fontSize = 12.sp
                        )
                    },
                    modifier = Modifier.weight(0.5f),
                    textStyle = TextStyle(color = Color.White),
                    isError = showErrorForLastName,
                    singleLine = true,

                    )
                CustomDropDownMenu(
                    expanded = logInAndCreateViewModel.showGenderMenu,
                    onExpandedChange = { logInAndCreateViewModel.onShowGenderMenu },
                    selectedItem = logInAndCreateViewModel.sexForUpdate,
                    onDismissedRequest = {
                        logInAndCreateViewModel.showGenderMenu = false
                    },
                    modifier = Modifier.weight(0.5f),
                    options = logInAndCreateViewModel.genders,
                    attachedCompose = {
                        OutlinedTextField(
                            readOnly = true,
                            value = logInAndCreateViewModel.sexForUpdate,
                            onValueChange = { },
                            label = {
                                Text(
                                    text = if (showErrorForSex) "Gender is required! " else "Sex",
                                    color = colorSex,
                                    fontSize = 12.sp
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { logInAndCreateViewModel.showGenderMenu = true}) {
                                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Green)

                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(color = Color.White),
                            isError = showErrorForSex,
                            singleLine = true,

                            )

                    },
                    addLeadingIcon = false
                ) { selectedList ->
                    logInAndCreateViewModel.sexForUpdate = selectedList
                }

            }
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = logInAndCreateViewModel.dateOfBirthForUpdate,
                    readOnly = true,
                    onValueChange = {  },
                    label = {
                        Text(
                            text = if (showErrorForDoB) "DoB is required! " else "Birth date",
                            color = colorBirthDate,
                            fontSize = 12.sp
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { dateDialogState.show()}) {
                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Green)

                        }
                    },
                    modifier = Modifier
                        .weight(0.5f),
                    textStyle = TextStyle(color = Color.White),
                    isError = showErrorForDoB,
                    singleLine = true,

                    )
                OutlinedTextField(
                    value = logInAndCreateViewModel.studentIdForUpdate,
                    onValueChange = { logInAndCreateViewModel.studentIdForUpdate = it },
                    label = {
                        Text(
                            text = if (showErrorForId) "Id is required! " else "Id",
                            color = colorId,
                            fontSize = 12.sp
                        )
                    },
                    modifier = Modifier.weight(0.5f),
                    textStyle = TextStyle(color = Color.White),
                    isError = showErrorForId,
                    singleLine = true,

                    )
            }
            OutlinedTextField(
                value = logInAndCreateViewModel.phoneNumberForUpdate,
                onValueChange = { logInAndCreateViewModel.phoneNumberForUpdate = it },
                label = {
                    Text(
                        text = if (showErrorForPhoneNumber) "Phone Number is required! " else "Phone Number",
                        color = colorPhoneNumber
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                isError = showErrorForPhoneNumber,
                singleLine = true,

                )
            CustomDropDownMenu(
                expanded = logInAndCreateViewModel.showDepartmentMenu,
                onExpandedChange = { logInAndCreateViewModel.onShowDepartmentMenu},
                selectedItem = logInAndCreateViewModel.departmentForUpdate,
                onDismissedRequest = {
                    logInAndCreateViewModel.showDepartmentMenu = false
                },
                modifier = Modifier.fillMaxWidth(),
                options = logInAndCreateViewModel.departments,
                attachedCompose = {
                    OutlinedTextField(
                        readOnly = true,
                        value = logInAndCreateViewModel.departmentForUpdate,
                        onValueChange = { },
                        label = {
                            Text(
                                text = if (showErrorForDepartment) "field is required! " else "Department",
                                color = colorDepartment
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { logInAndCreateViewModel.showDepartmentMenu = true}) {
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Green)

                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = Color.White),
                        isError = showErrorForDepartment,
                        singleLine = true,

                        )


                },
                addLeadingIcon = false
            ) { selectedList ->
                logInAndCreateViewModel.departmentForUpdate = selectedList
            }
            Spacer(modifier = Modifier.height(100.dp))
            Button(onClick = {
                isPressed = true
                if (!showErrorForFirstName && !showErrorForMiddleName && !showErrorForLastName &&
                    !showErrorForId && !showErrorForSex && !showErrorForDoB &&
                    !showErrorForPhoneNumber && !showErrorForDepartment
                ) {
                    logInAndCreateViewModel.updateUserProfile(
                        firstName = logInAndCreateViewModel.firstNameForUpdate,
                        middleName = logInAndCreateViewModel.middleNameForUpdate,
                        lastName = logInAndCreateViewModel.lastNameForUpdate,
                        studentId = logInAndCreateViewModel.studentIdForUpdate,
                        dateOfBirth = logInAndCreateViewModel.dateOfBirthForUpdate,
                        phoneNumber = logInAndCreateViewModel.phoneNumberForUpdate,
                        department = logInAndCreateViewModel.departmentForUpdate,
                        sex = logInAndCreateViewModel.sexForUpdate
                    )

                }
            }
            ) {
                Text(text = "Edit the profile")

            }
        }
    }
}

@Preview
@Composable
private fun PreviewForEdit() {
    EditProfileScreen(navController = rememberNavController())

}