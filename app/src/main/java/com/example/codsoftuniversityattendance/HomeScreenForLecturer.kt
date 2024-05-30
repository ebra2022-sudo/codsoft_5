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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.codsoftuniversityattendance.AddDirectoryScreen
import com.example.codsoftuniversityattendance.Department
import com.example.codsoftuniversityattendance.FileUploadScreen
import com.example.codsoftuniversityattendance.HomeContentStudent
import com.example.codsoftuniversityattendance.LogInAndCreateViewModel
import com.example.codsoftuniversityattendance.R
import com.example.codsoftuniversityattendance.Screens
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenForLecturer(
    logInAndCreateViewModel: LogInAndCreateViewModel = viewModel(),
    navController: NavHostController
) {
    val userProfile by logInAndCreateViewModel.userProfile.observeAsState()
    LaunchedEffect(Unit) {
        logInAndCreateViewModel.loadUserProfile()
    }

    userProfile.let {user ->
        logInAndCreateViewModel.firstNameForUpdate = user?.firstName ?: ""
        logInAndCreateViewModel.middleNameForUpdate = user?.middleName ?: ""
        logInAndCreateViewModel.lastNameForUpdate = user?.lastName ?: ""
        logInAndCreateViewModel.studentIdForUpdate = user?.studentId ?: ""
        logInAndCreateViewModel.sexForUpdate = user?.sex ?: ""
        logInAndCreateViewModel.dateOfBirthForUpdate = user?.dateOfBirth ?: ""
        logInAndCreateViewModel.departmentForUpdate = user?.department ?: ""
        logInAndCreateViewModel.phoneNumberForUpdate = user?.phoneNumber ?: ""
    }

    val departments = listOf(
        Department("Biomedical Engineering", R.raw.biomedical_engineering),
        Department("Electrical And Computer Engineering", R.raw.electrical_engineering),
        Department("Computer Science", R.raw.computer_science),
        Department("Mechanical Engineering", R.raw.mechanical_engineering),
        Department("Chemical Engineering", R.raw.chemical_engineering),
        Department("Software Engineering", R.raw.software_engineering),
        Department("Civil Engineering", R.raw.civil_engineering),
    )

    var title by remember {
        mutableStateOf("Welcome, ${userProfile?.firstName ?: ""}")
    }
    var currentContent by remember { mutableStateOf(Contents.Home) }

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
                        HorizontalDivider()
                        Text(text = "Setting", lineHeight = 50.sp, fontFamily = FontFamily.Serif, color = Color(0xFF061B64))
                        HorizontalDivider()
                        Text(text = "Contact", lineHeight = 50.sp, fontFamily = FontFamily.Serif, color = Color(0xFF061B64))
                        HorizontalDivider()
                        Text(text = "Contact", lineHeight = 50.sp, fontFamily = FontFamily.Serif, color = Color(0xFF061B64))
                        HorizontalDivider()
                        Text(text = "Contact", lineHeight = 50.sp, fontFamily = FontFamily.Serif, color = Color(0xFF061B64))
                        HorizontalDivider()
                        Text(text = "Contact", lineHeight = 50.sp, fontFamily = FontFamily.Serif, color = Color(0xFF061B64))
                        HorizontalDivider()
                        Text(text = "Contact", lineHeight = 50.sp, fontFamily = FontFamily.Serif, color = Color(0xFF061B64))
                        HorizontalDivider()
                    }
                    Text(text = "All right reserved", modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp))
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
                    }
                    },
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
                                title = "Upload file"
                                currentContent = Contents.ShareResource}) {
                            Icon(
                                painter = painterResource(id = R.drawable.bookshelf),
                                contentDescription = null,
                                tint = Color(0xFFEA80FC)
                            )
                            Text("Upload file")
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
                                title = "Manage courses"
                                currentContent = Contents.ManageCourses}) {
                            Icon(
                                painter = painterResource(id = R.drawable.library),
                                contentDescription = null,
                                tint = Color.Green
                            )
                            Text("Manage Courses")
                        }
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                var numberOfColumns by remember {
                    mutableIntStateOf(1)
                }
                val courses by logInAndCreateViewModel.subdirectories.collectAsState()

                when (currentContent) {
                    Contents.ShareResource -> FileUploadScreen(viewModel = logInAndCreateViewModel)
                    Contents.ManageCourses -> AddDirectoryScreen(viewModel = logInAndCreateViewModel)
                    Contents.Home -> HomeContentStudent(
                        departmentMedia = departments,
                        currentCourses = courses,
                        numberOfColumns = numberOfColumns,
                        viewModel = logInAndCreateViewModel
                    ) {

                        numberOfColumns = if (numberOfColumns == 1) 2 else 1

                    }
                    Contents.Attendance -> SendAttendanceScreen(courses = courses, viewModel = logInAndCreateViewModel)
                }
            }
        }
    }
}


@Composable
fun SendAttendanceScreen(
    courses: List<String>,
    viewModel: LogInAndCreateViewModel
) {
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
                viewModel.attendanceDate = formattedDate
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
    var show by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Course Selection
        Text(
            text = "Select Course:",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable {show = true }
        )
        DropdownMenu(
            expanded = show,
            onDismissRequest = {show = !show },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            courses.forEach { course ->
                DropdownMenuItem(onClick = {
                    viewModel.selectedCourseForAttendance = course
                }, text = {Text(text = course)})
            }
        }
        Text(
            text = "Selected Course: ${viewModel.selectedCourseForAttendance}",
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Date Selection
        Text(
            text = "Select Date:",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp).clickable { dateDialogState.show() }
        )
        TextField(
            value = viewModel.attendanceDate,
            readOnly = true,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Send Attendance Button
        Button(
            onClick = {
                viewModel.sendAttendance(
                courseId = viewModel.selectedCourseForAttendance,
                date = viewModel.attendanceDate,
                status = false
            )},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Send Attendance")
        }
    }
}


enum class Contents {
    Home, Attendance, ShareResource,ManageCourses
}









