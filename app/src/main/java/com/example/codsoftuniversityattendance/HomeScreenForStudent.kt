package com.example.codsoftuniversityattendance

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract.Attendees
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.offline.Download
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.codsoftuniversityattendance.models.Course
import com.example.codsoftuniversityattendance.models.Material
import com.example.codsoftuniversityattendance.models.Repository

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.firebase.storage.StorageReference
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
        logInAndCreateViewModel.emailAsId = user?.email?: ""
        logInAndCreateViewModel.fetchDirectories()
        logInAndCreateViewModel.fetchSubdirectories(user?.department?:"")
        logInAndCreateViewModel.loadRegisteredCourses()




    }

    val departments = listOf(
        Department("Biomedical Engineering ", R.raw.biomedical_engineering),
        Department("Electrical And Computer Engineering ", R.raw.electrical_engineering),
        Department("Computer Science ", R.raw.computer_science),
        Department("Mechanical Engineering ", R.raw.mechanical_engineering),
        Department("Chemical Engineering ", R.raw.chemical_engineering),
        Department("Software Engineering", R.raw.software_engineering),
        Department("Civil Engineering ", R.raw.civil_engineering),
    )


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
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = it, lineHeight = 50.sp, fontFamily = FontFamily.Serif, color = Color(0xFF061B64))
                                    IconButton(onClick = { logInAndCreateViewModel.dropCourse(it) }) {
                                        Icon(painter = painterResource(id = R.drawable.minus_circle_outline), contentDescription = "remove course", tint = Color.Red)
                                    }
                                }
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
                .padding(paddingValues)
                .padding(10.dp)) {
                val courses by logInAndCreateViewModel.subdirectories.collectAsState()
                var numberOfColumns by remember {
                    mutableIntStateOf(1)
                }



                when(currentContent) {
                    Contents.Home ->
                    {
                        HomeContentStudent(
                            departmentMedia = departments,
                            currentCourses = courses,
                            numberOfColumns = numberOfColumns,
                            viewModel = logInAndCreateViewModel
                        ) {
                            numberOfColumns = if (numberOfColumns == 1) 2 else 1
                        }
                    }
                    Contents.Resources -> FileUploadScreen(viewModel = logInAndCreateViewModel)
                    Contents.Courses -> MainScreen(department = logInAndCreateViewModel.department, courses = registeredCourses, viewModel =logInAndCreateViewModel )
                    Contents.Attendance -> AttendanceScreen(courseId = logInAndCreateViewModel.sc , courses = courses, viewModel = logInAndCreateViewModel) {_:String, _:Boolean ->
                        logInAndCreateViewModel.markAttendance(logInAndCreateViewModel.selectedCourseForAttendance, true)

                    }
                }
            }
        }
    }
}




@Composable
fun HomeContentStudent(
    departmentMedia: List<Department>,
    currentCourses: List<String>,
    numberOfColumns: Int,
    viewModel: LogInAndCreateViewModel,
    onToggleColumns: () -> Unit
) {
    // the
    if (departmentMedia.isEmpty()) {
        // Handle the case where department media is empty (e.g., display loading indicator)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() // Display a loading indicator
        }
    } else {
        Column(modifier = Modifier
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Welcome to Addis Ababa University", fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold, fontSize = 22.sp, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(32.dp))
            LazyRow {
                items(departmentMedia) { media ->
                    VideoCard(media = media) {
                        viewModel.currentHomeDepartment = media.name
                        viewModel.fetchSubdirectories(media.name)

                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Courses in ${viewModel.currentHomeDepartment}")
                IconButton(onClick = onToggleColumns) {
                    Icon(painter = painterResource(id = if (numberOfColumns == 1) R.drawable.view_grid_outline
                    else R.drawable.view_sequential_outline), contentDescription = null)
                }
            }
            HorizontalDivider(thickness = 3.dp)
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth(),
                columns = GridCells.Fixed(numberOfColumns)
            ) {
                items(currentCourses) { course ->
                    CourseCard(course = course, numberOfColumns = numberOfColumns)
                }
            }
        }
    }
}



data class Department(
    val name: String,
    val descriptionVideoResId: Int // Raw resource ID for the video
)

@Composable
fun VideoCard(media: Department, onClick: () -> Unit) {
    Column(
        modifier = Modifier.width(280.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            val context = LocalContext.current
            var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }

            DisposableEffect(key1 = media.descriptionVideoResId) {
                val uri = Uri.parse("android.resource://${context.packageName}/${media.descriptionVideoResId}")
                val player = ExoPlayer.Builder(context).build().apply {
                    setMediaItem(MediaItem.fromUri(uri))
                    prepare()
                    playWhenReady = true
                }
                exoPlayer = player

                onDispose {
                    player.release()
                }
            }

            exoPlayer?.let { player ->
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = {
                        PlayerView(it).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            this.player = player
                            this.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                            this.useController = false
                            this.requestFocus()
                        }
                    }
                )
            }
        }
        Text(
            text = media.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.W500,
            fontStyle = FontStyle.Italic,
            color = Color.Green,
            textAlign = TextAlign.Center
        )
    }
}




enum class Contents {
    Home, Attendance, Resources, Courses
}
@Composable
fun CourseCard(modifier: Modifier = Modifier, course: String, numberOfColumns: Int =1) {
    if (numberOfColumns == 1) {
        Row(
            modifier = Modifier
                .height(180.dp)
                .padding(16.dp)
                .then(modifier)
        ) {
            Card(
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Image(painter = painterResource(id = R.drawable.book_open_page_variant), contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer), modifier = Modifier.fillMaxSize())
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = course,
                modifier = Modifier
                    .weight(2f)
                    .align(Alignment.CenterVertically),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
    else {
        Column(
            modifier = Modifier
                .size(height = 120.dp, width = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Image(painter = painterResource(id = R.drawable.book_open_page_variant),
                    contentDescription =null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize() )
                
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = course,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                textAlign = TextAlign.Center
                
            )
        }

    }
}



////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun FileUploadScreen(viewModel: LogInAndCreateViewModel = viewModel()) {
    var fileName by remember { mutableStateOf("") }
    var fileType by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }



    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> selectedFileUri = uri }
    )

    val uploadProgress by viewModel.uploadProgress.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val departments by viewModel.directories.collectAsState()
        var currentDepartment by remember { mutableStateOf("") }
        val coursesForDepartment by viewModel.subdirectories.collectAsState()
        var currentCourse by remember { mutableStateOf("") }
        var showDepartments by remember { mutableStateOf(false) }
        CustomDropDownMenu(
            expanded = showDepartments,
            onExpandedChange = {
                viewModel.fetchDirectories()
                showDepartments = it },
            selectedItem = currentDepartment,
            onDismissedRequest = { showDepartments= false },
            modifier = Modifier.fillMaxWidth(),
            options = departments,
            attachedCompose = {
                TextField(
                    value = currentDepartment,
                    onValueChange = {  },
                    label = { Text("Department Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            addLeadingIcon = false
        ) { selectedList ->
            currentDepartment = selectedList
            viewModel.fetchSubdirectories(currentDepartment)
        }



        Spacer(modifier = Modifier.height(8.dp))
        var showCourses by remember { mutableStateOf(false) }
        CustomDropDownMenu(
            expanded = showCourses,
            onExpandedChange = { showCourses = it },
            selectedItem = currentCourse,
            onDismissedRequest = { showCourses = false },
            modifier = Modifier.fillMaxWidth(),
            options = coursesForDepartment,
            attachedCompose = {
                TextField(
                    value = currentCourse,
                    onValueChange = { },
                    label = { Text("Course Name") },
                    modifier = Modifier.fillMaxWidth()
                )

            },
            addLeadingIcon = false
        ) { selectedList ->
            currentCourse = selectedList
        }
        Spacer(modifier = Modifier.height(8.dp))
        var showFileType by remember { mutableStateOf(false) }
        CustomDropDownMenu(
            expanded = showFileType,
            onExpandedChange = { showFileType = it },
            selectedItem = fileType,
            onDismissedRequest = { showFileType = false },
            modifier = Modifier.fillMaxWidth(),
            options = listOf("Videos", "Images","Text Files"),
            attachedCompose = {
                TextField(
                    value = fileType,
                    onValueChange = {  },
                    label = { Text("File Type") },
                    modifier = Modifier.fillMaxWidth()
                )

            },
            addLeadingIcon = false
        ) { selectedList ->
            fileType = selectedList
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = fileName,
            onValueChange = { fileName = it },
            label = { Text("File Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { filePickerLauncher.launch("*/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select File")
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (isUploading) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = {
                        uploadProgress / 100f
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Uploading... $uploadProgress%")
            }
        } else {
            Button(
                onClick = {
                    selectedFileUri?.let { uri ->
                        isUploading = true
                        viewModel.uploadFile(
                            uri, currentDepartment, currentCourse, fileType, fileName,
                            onSuccess = { id ->
                                isUploading = false
                                // Handle success, e.g., show a success message
                            },
                            onFailure = {
                                isUploading = false
                                // Handle failure, e.g., show an error message
                            }
                        )
                    }
                },
                enabled = selectedFileUri != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Upload File")
            }
        }
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////


@Composable
fun AttendanceScreen(
    courseId: String,
    courses: List<String>,
    viewModel: LogInAndCreateViewModel,
    onMarkAttendance: (courseId: String, status: Boolean) -> Unit
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
                viewModel.sd = formattedDate
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
        
        var status by remember {
            mutableStateOf(false)
        }
        DropdownMenu(
            expanded = show,
            onDismissRequest = {show = !show },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            courses.forEach { course ->
                DropdownMenuItem(onClick = {
                    viewModel.sc = course
                }, text = {Text(text = course)})
            }
        }
        Text(
            text = "Selected Course: ${viewModel.sc}",
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .clickable { show = true }
        )
        
        Spacer(modifier = Modifier.height(30.dp))

        // Date Selection
        Text(
            text = "Selected Date:${viewModel.sd}",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable { dateDialogState.show() }
        )

        Row {
            RadioButton(
                selected = status,
                onClick = { status = true }
            )
            Text(text = "Present", modifier = Modifier.padding(end = 16.dp))
            RadioButton(
                selected = !status,
                onClick = { status = false }
            )
            Text(text = "Absent")
        }
        Button(
            onClick = { onMarkAttendance(courseId, status) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Mark Attendance")
        }

        // Send Attendance Button
    }

}




////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun AddDepartmentScreen(viewModel: LogInAndCreateViewModel) {
    val departments by viewModel.directories.collectAsState()
    var currentDepartment by remember { mutableStateOf("") }
    var showDepartments by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CustomDropDownMenu(
            expanded = showDepartments,
            onExpandedChange = {
                viewModel.fetchDirectories()
                showDepartments = it },
            selectedItem = currentDepartment,
            onDismissedRequest = { showDepartments= false },
            modifier = Modifier.fillMaxWidth(),
            options = departments,
            attachedCompose = {
                TextField(
                    value = currentDepartment,
                    onValueChange = { currentDepartment = it },
                    label = { Text("Department Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            addLeadingIcon = false
        ) { selectedList ->
            currentDepartment = selectedList
            viewModel.fetchSubdirectories(currentDepartment)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (currentDepartment.isNotEmpty()) {
                    viewModel.addDirectory(currentDepartment)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Department")
        }
    }
}

@Composable
fun AddCourseScreen(viewModel: LogInAndCreateViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val departments by viewModel.directories.collectAsState()
        var currentDepartment by remember { mutableStateOf("") }
        val coursesForDepartment by viewModel.subdirectories.collectAsState()
        var currentCourse by remember { mutableStateOf("") }
        var showDepartments by remember { mutableStateOf(false) }
        CustomDropDownMenu(
            expanded = showDepartments,
            onExpandedChange = {
                viewModel.fetchDirectories()
                showDepartments = it },
            selectedItem = currentDepartment,
            onDismissedRequest = { showDepartments= false },
            modifier = Modifier.fillMaxWidth(),
            options = departments,
            attachedCompose = {
                TextField(
                    value = currentDepartment,
                    onValueChange = { currentDepartment = it },
                    label = { Text("Department Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            addLeadingIcon = false
        ) { selectedList ->
            currentDepartment = selectedList
            viewModel.fetchSubdirectories(currentDepartment)
        }



        Spacer(modifier = Modifier.height(8.dp))
        var showCourses by remember { mutableStateOf(false) }
        CustomDropDownMenu(
            expanded = showCourses,
            onExpandedChange = { showCourses = it },
            selectedItem =currentCourse,
            onDismissedRequest = { showCourses = false },
            modifier = Modifier.fillMaxWidth(),
            options = coursesForDepartment,
            attachedCompose = {
                TextField(
                    value = currentCourse,
                    onValueChange = { currentCourse = it },
                    label = { Text("Course Name") },
                    modifier = Modifier.fillMaxWidth()
                )

            },
            addLeadingIcon = false
        ) { selectedList ->
            currentCourse = selectedList
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (currentDepartment.isNotEmpty() && currentCourse.isNotEmpty()) {
                    viewModel.addSubdirectory(currentDepartment, currentCourse)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Course")
        }
    }
}



@Composable
fun AddDirectoryScreen(viewModel: LogInAndCreateViewModel) {
    var selectedScreen by remember { mutableStateOf("AddDepartment") }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { selectedScreen = "AddDepartment" }) {
                Text("Add Department")
            }
            Button(onClick = { selectedScreen = "AddCourse" }) {
                Text("Add Course")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        when (selectedScreen) {
            "AddDepartment" -> AddDepartmentScreen(viewModel)
            "AddCourse" -> AddCourseScreen(viewModel)
        }

    }
}


//////////////////////////////////////////////////////////////////////////////////////////////////



////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun CoursesFilesScreen(department: String, courses: List<String>, viewModel: LogInAndCreateViewModel) {
    val courseFilesMap by viewModel.courseFilesTypeNameMap.collectAsState()

    LaunchedEffect(courses) {
        Log.d("CoursesFilesScreen", "Fetching storage references for courses: $courses in department: $department")
        viewModel.fetchStorageReferences(courses = courses, department = department)
        viewModel.fetchFilesForCourses()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Files by Courses:", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (courseFilesMap.isEmpty()) {
            Text(text = "No files found.")
        } else {
            courseFilesMap.forEach { (course, fileTypes) ->
                Log.d("CoursesFilesScreen", "Displaying files for course: $course with fileTypes: $fileTypes")
                Text(text = "Course: $course", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    fileTypes.forEach { (fileType, files) ->
                        item {
                            Text(text = "File Type: $fileType", style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(8.dp)
                            ) {
                                items(files.entries.toList()) { (fileName, downloadUrl) ->
                                    FileItem(fileName, downloadUrl)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FileItem(fileName: String, downloadUrl: String) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .border(1.dp, Color.Gray)
            .padding(8.dp)
    ) {
        Text(text = fileName, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = downloadUrl,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Blue,
            modifier = Modifier.clickable { Log.d("FileItem", "Clicked on file: $fileName with URL: $downloadUrl") }
        )
    }
}

@Composable
fun MainScreen(department: String, courses: List<String>, viewModel: LogInAndCreateViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        if (courses.isNotEmpty()) {
            CoursesFilesScreen(department = department, courses = courses,  viewModel)
        }
    }
}
