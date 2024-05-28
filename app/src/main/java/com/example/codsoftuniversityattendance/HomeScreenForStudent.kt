package com.example.codsoftuniversityattendance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenForStudent(
    logInAndCreateViewModel: LogInAndCreateViewModel = viewModel(),
    navController: NavHostController
) {
    val userProfile by logInAndCreateViewModel.userProfile.observeAsState()
    val registeredCourses by logInAndCreateViewModel.registeredCourses.collectAsState()
    LaunchedEffect(Unit) {
        logInAndCreateViewModel.loadUserProfile()
    }
    userProfile.let {user ->
        logInAndCreateViewModel.firstNameForUpdate= user?.firstName ?: ""
        logInAndCreateViewModel.middleNameForUpdate = user?.middleName ?: ""
        logInAndCreateViewModel.lastNameForUpdate = user?.lastName ?: ""
        logInAndCreateViewModel.studentIdForUpdate = user?.studentId ?: ""
        logInAndCreateViewModel.sexForUpdate = user?.sex ?: ""
        logInAndCreateViewModel.dateOfBirthForUpdate = user?.dateOfBirth ?: ""
        logInAndCreateViewModel.departmentForUpdate = user?.department ?: ""
        logInAndCreateViewModel.phoneNumberForUpdate = user?.phoneNumber ?: ""
        logInAndCreateViewModel.loadCoursesForDepartment(user!!)
    }

    var title by remember {
        mutableStateOf("Welcome, ${userProfile?.firstName ?: ""}")
    }
    var currentContent by remember { mutableStateOf(Contents.Home)}

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.8f)) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(16.dp)) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart), verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            shape = CircleShape, modifier = Modifier
                                .size(50.dp), colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    text = "${userProfile?.firstName?.firstOrNull() ?: ""}${userProfile?.middleName?.firstOrNull() ?: "E"}",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .align(Alignment.Center),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "${userProfile?.firstName ?: "No"} ${userProfile?.middleName ?: "Name"}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W400,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = userProfile?.email ?: "",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W400,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        IconButton(onClick = {
                            navController.navigate(Screens.EditProfileScreen.name)
                        }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                        }
                    }
                    Column(modifier = Modifier.align(Alignment.BottomStart)) {
                        Text(
                            text = "Id: ${userProfile?.studentId ?: ""}",
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "Department: ${userProfile?.department ?: ""}",
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
                Box(modifier = Modifier
                    .fillMaxSize()) {
                    Image(painter = painterResource(id = R.drawable.sidebare_bg),
                        contentDescription = null, contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxSize())
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)) {
                        HorizontalDivider(color = Color.Magenta, thickness = 2.dp)
                        Text(text = "Your courses", fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 50.sp, fontFamily = FontFamily.Serif, color = Color(0xFF061B64))
                        HorizontalDivider(color = Color.Magenta, thickness = 2.dp)

                        LazyColumn {
                            items(registeredCourses) {
                                Text(text = it, lineHeight = 50.sp, fontFamily = FontFamily.Serif, color = Color(0xFF061B64))
                            }
                        }
                    }
                    Text(text = "All right reserved", modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp),
                        color = Color(0xFF061B64))
                    FloatingActionButton(onClick = { navController.navigate(Screens.AddCourseScreen.name) }, modifier = Modifier
                        .align(
                            Alignment.BottomEnd
                        )
                        .padding(16.dp)) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)

                    }

                }
            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = title) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = { IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    }},
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary)
                )
            },
            bottomBar = {
                BottomAppBar(modifier = Modifier.height(60.dp)) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier.clickable {
                                title = "Welcome, ${userProfile?.firstName ?: ""}"
                                currentContent = Contents.Home }) {
                            Icon(
                                painter = painterResource(id = R.drawable.home_outline),
                                contentDescription = null,
                                tint = Color(0xFF80D8FF)
                            )
                            Text("Home", fontFamily = FontFamily.SansSerif)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier.clickable {
                                title = "Resources"
                                currentContent = Contents.Resources }) {
                            Icon(
                                painter = painterResource(id = R.drawable.bookshelf),
                                contentDescription = null,
                                tint = Color(0xFFEA80FC)
                            )
                            Text("Resources")
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier.clickable {
                                title = "Attendance"
                                currentContent = Contents.Attendance }) {
                            Icon(
                                painter = painterResource(id = R.drawable.calendar_check),
                                contentDescription = null,
                                tint = Color(0xFF177AF1)
                            )
                            Text("Attendance")
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier.clickable {
                                title = "My Courses"
                                currentContent = Contents.Courses }) {
                            Icon(
                                painter = painterResource(id = R.drawable.library),
                                contentDescription = null,
                                tint = Color.Green
                            )
                            Text("My Courses")
                        }
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                when (currentContent) {
                    Contents.Home -> HomeContentStudent()
                    Contents.Attendance -> AttendanceContentStudent(logInAndCreateViewModel = logInAndCreateViewModel, courseId = "")
                    Contents.Resources -> ResourceContentStudent()
                    Contents.Courses -> MyCourseContent()
                }
            }
        }
    }

}




@Composable
fun HomeContentStudent(modifier: Modifier = Modifier) {
    Column(modifier = Modifier
        .fillMaxSize()
        .then(modifier)) {
        Text(text = "This is Home content")

    }
}
@Composable
fun ResourceContentStudent(modifier: Modifier = Modifier) {
    Column(modifier = Modifier
        .fillMaxSize()
        .then(modifier)) {
        Text(text = "This is resource content")
    }
}
@Composable
fun AttendanceContentStudent(modifier: Modifier = Modifier, logInAndCreateViewModel: LogInAndCreateViewModel, courseId: String) {
    Column(modifier = Modifier
        .fillMaxSize()
        .then(modifier), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text ="This is Attendance content")
        Button(onClick = { logInAndCreateViewModel.markAttendance(courseId) }) {
            Text("Mark Attendance")
        }

        if (logInAndCreateViewModel.attendanceMarkedSuccess.collectAsState().value) {
            Text("Attendance marked!")
        }

    }
}
enum class Contents {
    Home, Attendance, Resources, Courses
}
@Composable
fun MyCourseContent(modifier: Modifier = Modifier) {
    Column(modifier = Modifier
        .fillMaxSize()
        .then(modifier)) {
        Text(text = "This is My Course content")

    }
}
// the sample sample the sample of the sample of  the given vlau eo teh
@Preview
@Composable
private fun PreviewStudent() {
    HomeScreenForStudent(navController = rememberNavController())

}
