package com.example.codsoftuniversityattendance

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codsoftuniversityattendance.models.Attendance
import com.example.codsoftuniversityattendance.models.AttendanceRepository
import com.example.codsoftuniversityattendance.models.CourseMaterial
import com.example.codsoftuniversityattendance.models.CourseMaterialRepository
import com.example.codsoftuniversityattendance.models.CourseRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


class LogInAndCreateViewModel : ViewModel() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val fireStore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    val logInSuccess = MutableStateFlow(false)
    val updateSuccess = MutableStateFlow(false)
    val registrationSuccess = MutableStateFlow(false)
    private val _userProfile = MutableLiveData<UserProfile?>()
    val userProfile: LiveData<UserProfile?> get() = _userProfile

    val loginErrorMessage = MutableStateFlow("")
    val updateErrorMessage = MutableStateFlow("")
    val addCourseStatusMessage = MutableStateFlow("")
    val registrationErrorMessage = MutableStateFlow("")

    val genders = listOf("Male", "Female")
    var showGenderMenu by mutableStateOf(false)
    val onShowGenderMenu = { _: Boolean -> showGenderMenu = !showGenderMenu }

    var showCourses by mutableStateOf(false)
    val onShowCourses = { _: Boolean -> showCourses = !showCourses }
    var selectedCourse by mutableStateOf("")
    var courses = MutableStateFlow<List<String>>(emptyList())
    var registeredCourses = MutableStateFlow<List<String>>(emptyList())

    val departments = listOf("Bio-Medical Engineering","Chemical Engineering", "Civil Engineering",
        "Electrical And Computer Engineering","Mechanical Engineering", "Software Engineering", "Computer Science")
    var showDepartmentMenu by mutableStateOf(false)
    val onShowDepartmentMenu = { _: Boolean -> showDepartmentMenu = !showDepartmentMenu }

    var firstName by mutableStateOf("")
    var middleName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var studentId by mutableStateOf("")
    var sex by mutableStateOf("")
    var department by mutableStateOf("")
    var dateOfBirth by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var passwordForLog by mutableStateOf("")
    var passwordForCreate by mutableStateOf("")
    var emailForLogin by mutableStateOf("")
    var emailForReset by mutableStateOf("")
    val isLoadingProfile = MutableStateFlow(false)
    var emailForCreate by mutableStateOf("")
    var createdAs by mutableStateOf("")
    val resetStatusMessage = MutableStateFlow("")

    var firstNameForUpdate by mutableStateOf("")
    var middleNameForUpdate by mutableStateOf("")
    var lastNameForUpdate by mutableStateOf("")
    var studentIdForUpdate by mutableStateOf("")
    var sexForUpdate by mutableStateOf("")
    var departmentForUpdate by mutableStateOf("")
    var dateOfBirthForUpdate by mutableStateOf("")
    var phoneNumberForUpdate by mutableStateOf("")

    val courseRegistrationSuccess = MutableStateFlow(false)
    val courseDropSuccess = MutableStateFlow(false)

    val courseRepository = CourseRepository(FirebaseFirestore.getInstance())

    fun loadCoursesForDepartment(userProfile: UserProfile) {
        val department = userProfile.department ?: return

        courseRepository.getCoursesForDepartment(department, { courseList ->
            courses.value = courseList
        }, {
            // Handle failure
        })
    }

    fun registerForCourse(id: String) {
        val courseId = courses.value.find { it == selectedCourse } ?: return
        val studentId = auth.currentUser?.uid ?: return

        courseRepository.registerForCourse(courseId, studentId, {
            courseRegistrationSuccess.value = true
            loadRegisteredCourses(id)
        }, {
            courseRegistrationSuccess.value = false
        })
    }

    fun dropCourse(courseId: String, id: String) {


        courseRepository.dropCourse(courseId, id, {
            courseDropSuccess.value = true
            loadRegisteredCourses(id)
        }, {
            courseDropSuccess.value = false
        })
    }

    fun loadRegisteredCourses(id: String) {

        courseRepository.getRegisteredCourses(id, { courseList ->
            registeredCourses.value = courseList
        }, {
            // Handle failure
        })
    }

    private val materialRepository = CourseMaterialRepository(FirebaseFirestore.getInstance())
    private val attendanceRepository = AttendanceRepository(FirebaseFirestore.getInstance())
    val attendanceMarkedSuccess = MutableStateFlow(false)

    fun markAttendance(courseId: String) {
        val studentId = auth.currentUser?.uid ?: return
        val attendance = Attendance(
            attendanceId = UUID.randomUUID().toString(),
            courseId = courseId,
            studentId = studentId,
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            status = true
        )
        attendanceRepository.markAttendance(attendance, {
            attendanceMarkedSuccess.value = true
        }, {
            attendanceMarkedSuccess.value = false
        })
    }

    fun uploadCourseMaterial(courseId: String, title: String, url: String) {
        val material = CourseMaterial(
            materialId = UUID.randomUUID().toString(),
            courseId = courseId,
            title = title,
            url = url
        )
        materialRepository.uploadMaterial(material, {
            // Handle success
        }, {
            // Handle failure
        })
    }

    fun createAccount(
        firstName: String,
        middleName: String,
        lastName: String,
        studentId: String,
        email: String,
        password: String,
        createdFor: String,
        sex: String,
        dateOfBirth: String,
        department: String,
        phoneNumber: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    registrationSuccess.value = true
                    val userId = auth.currentUser?.uid ?: run {
                        registrationSuccess.value = false
                        Log.d(TAG, "User ID not found.")
                        return@addOnCompleteListener
                    }
                    val user = hashMapOf(
                        "firstName" to firstName,
                        "middleName" to middleName,
                        "lastName" to lastName,
                        "email" to email,
                        "studentId" to studentId,
                        "createdFor" to createdFor,
                        "sex" to sex,
                        "dateOfBirth" to dateOfBirth,
                        "department" to department,
                        "phoneNumber" to phoneNumber
                    )

                    fireStore.collection("users").document(userId).set(user)
                        .addOnSuccessListener {
                            Log.d(TAG, "Registration success")
                        }
                        .addOnFailureListener { e ->
                            registrationSuccess.value = false
                            Log.d(TAG, "Registration failed", e)
                        }
                } else {
                    registrationSuccess.value = false
                    registrationErrorMessage.value = task.exception?.message ?: ""
                    Log.d(TAG, "Registration failed", task.exception)
                }
            }
    }

    fun loginUser(email: String, password: String = "pseudo password", onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        auth.signInWithEmailAndPassword(
            email.ifEmpty { "pseudo email" },
            password.ifEmpty { "pseudo password" })
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Login is successful with email = $email")

                    viewModelScope.launch {
                        isLoadingProfile.value = true
                        val profile = getUserProfile()
                        isLoadingProfile.value = false

                        if (profile != null) {
                            _userProfile.value = profile
                            logInSuccess.value = true
                            onSuccess(profile.createdFor)  // Pass the createdFor value to onSuccess
                        } else {
                            logInSuccess.value = false
                            Log.d(TAG, "Failed to load user profile")
                            onFailure("Failed to load user profile")
                        }
                    }
                } else {
                    logInSuccess.value = false
                    loginErrorMessage.value = task.exception?.message ?: ""
                    Log.d(TAG, "Login failed", task.exception)
                    onFailure(task.exception?.message ?: "Login failed")
                }
            }
    }

    private fun getCurrentUser() = auth.currentUser

    private suspend fun getUserProfile(): UserProfile? {
        val user = getCurrentUser()
        user?.let {
            val userId = it.uid
            val email = it.email ?: ""
            val basicProfile = UserProfile(
                firstName = it.displayName ?: "",
                email = email
            )

            return try {
                val documentSnapshot = fireStore.collection("users").document(userId).get().await()
                if (documentSnapshot.exists()) {
                    val userProfile = documentSnapshot.toObject(UserProfile::class.java)
                    userProfile?.copy(email = email) // Ensure email is updated
                } else {
                    basicProfile
                }
            } catch (e: Exception) {
                Log.d(TAG, "Error getting user profile", e)
                null
            }
        }
        return null
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            isLoadingProfile.value = true
            _userProfile.value = getUserProfile()
            isLoadingProfile.value = false
        }
    }

    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resetStatusMessage.value = "Password reset email sent."
                    Log.d(TAG, "Password reset email sent.")
                } else {
                    val exception = task.exception
                    resetStatusMessage.value = when (exception) {
                        is FirebaseAuthInvalidUserException -> "Invalid email address."
                        is FirebaseAuthInvalidCredentialsException -> "Invalid email address format."
                        is FirebaseNetworkException -> "Network error. Please try again later."
                        else -> exception?.message ?: "Error sending password reset email"
                    }
                    Log.d(TAG, "Password reset failed", exception)
                }
            }
    }

    fun updateUserProfile(
        firstName: String,
        middleName: String,
        lastName: String,
        studentId: String,
        sex: String,
        dateOfBirth: String,
        department: String,
        phoneNumber: String
    ) {
        val userId = auth.currentUser?.uid ?: run {
            Log.d(TAG, "User ID not found.")
            return
        }

        val updatedUser = hashMapOf(
            "firstName" to firstName,
            "middleName" to middleName,
            "lastName" to lastName,
            "studentId" to studentId,
            "sex" to sex,
            "dateOfBirth" to dateOfBirth,
            "department" to department,
            "phoneNumber" to phoneNumber
        )

        fireStore.collection("users").document(userId).update(updatedUser as Map<String, Any>)
            .addOnSuccessListener {
                Log.d(TAG, "Profile updated successfully")
                updateSuccess.value = true
                updateErrorMessage.value = "Profile updated successfully"
                loadUserProfile()
            }
            .addOnFailureListener { e ->
                updateSuccess.value = false
                updateErrorMessage.value = e.message.toString()
                Log.d(TAG, "Profile update failed", e)
            }
    }

    companion object {
        private const val TAG = "LogInAndCreateViewModel"
    }
}

enum class Status {
    Student, Lecturer
}

data class UserProfile(
    var firstName: String = "",
    val middleName: String = "",
    var lastName: String = "",
    var studentId: String = "",
    var email: String = "",
    var createdFor: String = "",
    var sex: String = "",
    var department: String = "",
    var dateOfBirth: String = "",
    var phoneNumber: String = ""
)
