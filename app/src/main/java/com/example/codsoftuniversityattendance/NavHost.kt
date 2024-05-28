package com.example.codsoftuniversityattendance

import HomeScreenForLecturer
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class Screens {
    CreateAccountScreen,
    LogInScreen,
    HomeScreenForStudent,
    HomeScreenForLecturer,
    TermsAndConditionsScreen,
    EditProfileScreen,
    ForgetPassWordScreen,
    AddCourseScreen
}

@Composable
fun ScreenContainer() {
    val navController =  rememberNavController()
    val viewModel: LogInAndCreateViewModel = viewModel()
    NavHost(navController = navController, startDestination = Screens.LogInScreen.name) {
        composable(route = Screens.LogInScreen.name) {
            LoginScreen(navController = navController, logInAndCreateViewModel = viewModel)
        }
        composable(route = Screens.CreateAccountScreen.name) {
            CreateAccountScreen(navController = navController, logInAndCreateViewModel = viewModel)
        }
        composable(route = Screens.ForgetPassWordScreen.name) {
            ForgetPasswordScreen(navController = navController, logInAndCreateViewModel = viewModel)
        }
        composable(route = Screens.EditProfileScreen.name) {
            EditProfileScreen(navController = navController, logInAndCreateViewModel = viewModel)
        }
        composable(route = Screens.HomeScreenForStudent.name) {
            HomeScreenForStudent(navController = navController, logInAndCreateViewModel = viewModel)
        }
        composable(route = Screens.HomeScreenForLecturer.name) {
            HomeScreenForLecturer(navController = navController, logInAndCreateViewModel = viewModel)
        }
        composable(route = Screens.TermsAndConditionsScreen.name) {
            TermsAAndConditionSScreen(navController = navController)
        }
        composable(route = Screens.AddCourseScreen.name) {
            RegisterForCourse(navController = navController, logInAndCreateViewModel = viewModel)
        }
    }
}