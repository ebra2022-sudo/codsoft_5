package com.example.codsoftuniversityattendance

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.codsoftuniversityattendance.ui.theme.CodsoftUniversityAttendanceTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// sample of the given alue of the  sample of the sampe
// smaple of the gien  alue of  he amunt ofthe
@Composable
fun CreateAccountScreen(
    logInAndCreateViewModel: LogInAndCreateViewModel = viewModel(),
    navController: NavController
) {
    val registrationSuccess by logInAndCreateViewModel.registrationSuccess.collectAsState()
    val registrationErrorMessage by logInAndCreateViewModel.registrationErrorMessage.collectAsState()
    var showErrorForFirstName by remember { mutableStateOf(false) }
    var showErrorForMiddleName by remember { mutableStateOf(false) }
    var showErrorForLastName by remember { mutableStateOf(false) }
    var showErrorForEmail by remember { mutableStateOf(false) }
    var showErrorForId by remember { mutableStateOf(false) }
    var showErrorForPassword  by remember { mutableStateOf(false) }
    var showErrorForStatus by remember { mutableStateOf(false) }
    var showErrorForSex by remember { mutableStateOf(false) }
    var showErrorForDepartment by remember { mutableStateOf(false) }
    var showErrorForDoB by remember { mutableStateOf(false) }
    var showErrorForPhoneNumber by remember { mutableStateOf(false) }
    var isPressed by  remember { mutableStateOf(false) }
    val colorFirstName = animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.firstName.isEmpty()) {
            showErrorForFirstName = true
            Color.Red
        }
        else {
            showErrorForFirstName = false
            Color.Green

        }, label = ""
    ).value
    val colorMiddleName = animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.middleName.isEmpty()) {
            showErrorForMiddleName = true
            Color.Red
        }
        else {
            showErrorForMiddleName = false
            Color.Green

        }, label = ""
    ).value
    val colorLastName = animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.lastName.isEmpty()) {
            showErrorForLastName = true
            Color.Red
        }
        else {
            showErrorForLastName = false
            Color.Green

        }, label = ""
    ).value
    val colorSex = animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.sex.isEmpty()) {
            showErrorForSex = true
            Color.Red
        }
        else {
            showErrorForSex = false
            Color.Green

        }, label = ""
    ).value
    val colorBirthDate = animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.dateOfBirth.isEmpty()) {
            showErrorForDoB = true
            Color.Red
        }
        else {
            showErrorForDoB= false
            Color.Green

        }, label = ""
    ).value
    val colorId = animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.studentId.isEmpty()) {
            showErrorForId = true
            Color.Red
        }
        else {
            showErrorForId = false
            Color.Green

        }, label = ""
    ).value
    val colorPhoneNumber = animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.phoneNumber.isEmpty()) {
            showErrorForPhoneNumber = true
            Color.Red
        }
        else {
            showErrorForPhoneNumber = false
            Color.Green

        }, label = ""
    ).value
    val colorDepartment = animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.department.isEmpty()) {
            showErrorForDepartment = true
            Color.Red
        }
        else {
            showErrorForDepartment = false
            Color.Green

        }, label = ""
    ).value
    val colorEmail =  animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.emailForCreate.isEmpty()) {
            showErrorForEmail = true
            Color.Red
        }
        else {
            showErrorForEmail = false
            Color.Green

        }, label = ""
    ).value
    val colorPassword = animateColorAsState(
        targetValue = if (isPressed && logInAndCreateViewModel.passwordForCreate.isEmpty()) {
            showErrorForPassword = true
            Color.Red
        }
        else {
            showErrorForPassword = false
            Color.Green
        }, label = ""
    ).value


    val dateDialogState = rememberMaterialDialogState()
    var pickedDate by remember {mutableStateOf(LocalDate.now())}
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
                logInAndCreateViewModel.dateOfBirth= formattedDate
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

    if (registrationSuccess) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(64.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(id =R.drawable.check_circle ), contentDescription = null, tint = Color.Green
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "You have been registered successfully",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Green,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(200.dp))
            Button(onClick = { navController.navigateUp()},
                modifier = Modifier.fillMaxWidth()) {
                Text(text = "Come Back to Login")
            }
        }
    }
    else {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.tt), contentDescription = null,
                modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .align(alignment = Alignment.BottomCenter)
            ) {

                Text(
                    text = "Create\nAccount",
                    fontSize = 45.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .align(Alignment.TopStart),
                    color = Color(0xFFE0EBE8),
                    lineHeight = 50.sp
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {

                    Text(text = registrationErrorMessage, color = Color.Red)
                    var passwordVisible by remember { mutableStateOf(false) }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)){
                        OutlinedTextField(
                            value = logInAndCreateViewModel.firstName,
                            onValueChange = { logInAndCreateViewModel.firstName = it },
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
                            value = logInAndCreateViewModel.middleName,
                            onValueChange = { logInAndCreateViewModel.middleName = it },
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
                            value = logInAndCreateViewModel.lastName,
                            onValueChange = { logInAndCreateViewModel.lastName = it },
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
                            selectedItem = logInAndCreateViewModel.sex,
                            onDismissedRequest = {
                                logInAndCreateViewModel.showGenderMenu = false
                            },
                            modifier = Modifier.weight(0.5f),
                            options = logInAndCreateViewModel.genders,
                            attachedCompose = {
                                OutlinedTextField(
                                    readOnly = true,
                                    value = logInAndCreateViewModel.sex,
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
                            logInAndCreateViewModel.sex = selectedList
                        }

                    }
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = logInAndCreateViewModel.dateOfBirth,
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
                            value = logInAndCreateViewModel.studentId,
                            onValueChange = { logInAndCreateViewModel.studentId = it },
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
                        value = logInAndCreateViewModel.phoneNumber,
                        onValueChange = { logInAndCreateViewModel.phoneNumber = it },
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
                        selectedItem = logInAndCreateViewModel.department,
                        onDismissedRequest = {
                            logInAndCreateViewModel.showDepartmentMenu = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        options = logInAndCreateViewModel.departments,
                        attachedCompose = {
                            OutlinedTextField(
                                readOnly = true,
                                value = logInAndCreateViewModel.department,
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
                        logInAndCreateViewModel.department = selectedList
                    }

                    OutlinedTextField(
                        value = logInAndCreateViewModel.emailForCreate,
                        onValueChange = { logInAndCreateViewModel.emailForCreate = it },
                        label = {
                            Text(
                                text = if (showErrorForEmail) "Email is required!" else "Email",
                                color = colorEmail
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = Color.White),
                        isError = showErrorForEmail,
                        singleLine = true,

                        )


                    OutlinedTextField(
                        value = logInAndCreateViewModel.passwordForCreate.trim(),
                        onValueChange = { logInAndCreateViewModel.passwordForCreate = it },
                        label = {
                            Text(
                                text = if (showErrorForPassword) "Password is required! " else "Password",
                                color = colorPassword
                            )
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible) painterResource(id = R.drawable.eye)
                            else painterResource(id = R.drawable.eye_off)

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(painter = image, contentDescription = null, tint = Color.Green)
                            }
                        },
                        textStyle = TextStyle(color = Color.White),
                        isError = showErrorForPassword,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,

                        )
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { logInAndCreateViewModel.createdAs = Status.Student.name },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = animateColorAsState(
                                    targetValue = if (logInAndCreateViewModel.createdAs == Status.Student.name)
                                        Color.Green else Color.White, label = "lecturer indicator"
                                ).value,
                                contentColor = Color.Blue
                            ),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.height(50.dp)
                        ) {
                            Text(text = "I'm Student", fontWeight = FontWeight.Medium)
                        }
                        Button(
                            onClick = { logInAndCreateViewModel.createdAs = Status.Lecturer.name },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = animateColorAsState(
                                    targetValue = if (logInAndCreateViewModel.createdAs == Status.Lecturer.name)
                                        Color.Green else Color.White, label = "lecturer indicator"
                                ).value,
                                contentColor = Color.Blue
                            ),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.height(50.dp)
                        ) {
                            Text(text = "I'm Lecturer", fontWeight = FontWeight.Medium)
                        }
                    }
                    if (logInAndCreateViewModel.createdAs.isEmpty()) {
                        showErrorForStatus = true
                        Text(text = "please select student or lecturer", color = Color.Red)
                    } else {
                        showErrorForStatus = false
                    }


                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            isPressed = true
                            if (!showErrorForFirstName && !showErrorForMiddleName && !showErrorForLastName &&
                                !showErrorForId && !showErrorForEmail && !showErrorForPassword && !showErrorForStatus &&
                                !showErrorForSex && !showErrorForDoB && !showErrorForPhoneNumber && !showErrorForDepartment
                            ) {
                                logInAndCreateViewModel.createAccount(
                                    firstName = logInAndCreateViewModel.firstName,
                                    middleName = logInAndCreateViewModel.middleName,
                                    lastName = logInAndCreateViewModel.lastName,
                                    studentId = logInAndCreateViewModel.studentId,
                                    email = logInAndCreateViewModel.emailForCreate,
                                    password = logInAndCreateViewModel.passwordForCreate,
                                    createdFor = logInAndCreateViewModel.createdAs,
                                    sex = logInAndCreateViewModel.sex,
                                    dateOfBirth = logInAndCreateViewModel.dateOfBirth,
                                    department = logInAndCreateViewModel.department,
                                    phoneNumber = logInAndCreateViewModel.phoneNumber
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Sign up", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(onClick = { navController.navigate(Screens.LogInScreen.name) }) {
                        Text(text = "Already have an account? ", color = Color.Green)
                        Text(text = "Log in", fontWeight = FontWeight.Medium, color = Color.Magenta)
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedItem: String,
    onDismissedRequest: ()->Unit,
    options: List<String>,
    addLeadingIcon: Boolean = true,
    topComposable: @Composable () -> Unit = {},
    bottomComposable: @Composable () -> Unit = {},
    attachedCompose: @Composable () -> Unit = {
        Text(
            text = selectedItem,
            modifier = Modifier
                .fillMaxWidth()
        )
    },
    textColor: Color = MaterialTheme.colorScheme.onSecondary,
    onItemClicked: (String)-> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        Box(modifier = Modifier.menuAnchor()) {
            attachedCompose()
        }
        ExposedDropdownMenu(expanded = expanded,
            onDismissRequest = onDismissedRequest,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .width(200.dp)) {
            topComposable()
            options.toSet().forEach { option: String ->
                DropdownMenuItem(
                    text = { Row(horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                            .fillMaxSize()
                            .background(color = if (selectedItem == option) MaterialTheme.colorScheme.secondary else Color.Transparent)) {
                        if (addLeadingIcon) Icon(painter = painterResource(id = R.drawable.account), contentDescription =null )
                        Text(text = option, fontWeight = FontWeight.Medium)
                    } },
                    onClick = {
                        onItemClicked(option)
                        onDismissedRequest()
                    },
                    colors =
                    if (selectedItem == option)
                        MenuDefaults.itemColors(textColor = textColor)
                    else MenuDefaults.itemColors(MaterialTheme.colorScheme.secondary)
                )
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
            }
            bottomComposable()
        }
    }
}
@Preview
@Composable
private fun CreateAccountScreenPreview() {
    CodsoftUniversityAttendanceTheme {
        CreateAccountScreen(navController = rememberNavController())
    }
}