package com.example.codsoftuniversityattendance

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.codsoftuniversityattendance.models.Attendance
import com.example.codsoftuniversityattendance.models.AttendanceRepository
import com.example.codsoftuniversityattendance.models.Course

import com.example.codsoftuniversityattendance.models.CourseRepository

import com.example.codsoftuniversityattendance.models.FileMetadata
import com.example.codsoftuniversityattendance.models.UserProfile
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


class LogInAndCreateViewModel : ViewModel() {


    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val fireStore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val storage: FirebaseStorage by lazy {FirebaseStorage.getInstance()}
    private val storageRef: StorageReference by lazy { storage.reference}



    val logInSuccess = MutableStateFlow(false)
    val updateSuccess = MutableStateFlow(false)
    val registrationSuccess = MutableStateFlow(false)
    private val _userProfile = MutableLiveData<UserProfile?>()
    val userProfile: LiveData<UserProfile?> get() = _userProfile

    val loginErrorMessage = MutableStateFlow("")
    val updateErrorMessage = MutableStateFlow("")
    val addCourseStatusMessage = MutableStateFlow("")
    val registrationErrorMessage = MutableStateFlow("")
    var emailAsId by mutableStateOf("")



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
    var currentHomeDepartment by mutableStateOf("")

    var firstNameForUpdate by mutableStateOf("")
    var middleNameForUpdate by mutableStateOf("")
    var lastNameForUpdate by mutableStateOf("")
    var studentIdForUpdate by mutableStateOf("")
    var sexForUpdate by mutableStateOf("")
    var departmentForUpdate by mutableStateOf("")
    var dateOfBirthForUpdate by mutableStateOf("")
    var phoneNumberForUpdate by mutableStateOf("")


    var attendanceDate by mutableStateOf("")
    var selectedCourseForAttendance by mutableStateOf("")



    val courseRegistrationSuccess = MutableStateFlow(false)
    val courseDropSuccess = MutableStateFlow(false)

    val courseRepository = CourseRepository(FirebaseFirestore.getInstance())
    private val _registeredCourses = MutableStateFlow<List<String>>(emptyList())
    var selectedCourse by mutableStateOf("")
    val registeredCourses: StateFlow<List<String>> get() = _registeredCourses

    fun registerForCourse(courseName: String) {
        viewModelScope.launch {
            try {
                val userRef = fireStore.collection("users").document(emailAsId)
                userRef.update("registeredCourses", FieldValue.arrayUnion(courseName)).await()
                fetchRegisteredCourses(emailAsId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchRegisteredCourses(userId: String) {
        viewModelScope.launch {
            try {
                val userSnapshot = fireStore.collection("users").document(userId).get().await()
                val courses = userSnapshot.get("registeredCourses") as? List<String> ?: emptyList()
                _registeredCourses.value = courses
                courseRegistrationSuccess.value = true
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun registerForCourse(courses: List<String>, selectedCourse: String) {
        val courseId = courses.find { it == selectedCourse } ?: return
        courseRepository.registerForCourse(courseId,emailAsId , {
            courseRegistrationSuccess.value = true
            loadRegisteredCourses()
        }, {
            courseRegistrationSuccess.value = false
        })
    }

    fun dropCourse(courseName: String) {


        courseRepository.dropCourse(courseName, emailAsId,{
            courseDropSuccess.value = true
            loadRegisteredCourses()
        }, {
            courseDropSuccess.value = false
        })
    }

    fun loadRegisteredCourses() {

        courseRepository.getRegisteredCourses(emailAsId, { courseList ->
            _registeredCourses.value = courseList
        }, {
            // Handle failure
        })
    }

    private val attendanceRepository = AttendanceRepository(FirebaseFirestore.getInstance())
    val attendanceMarkedSuccess = MutableStateFlow(false)

    fun markAttendance(courseId: String, b: Boolean) {
        val attendance = Attendance(
            attendanceId = UUID.randomUUID().toString(),
            courseId = courseId,
            studentId = emailAsId,
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            status = true
        )
        attendanceRepository.markAttendance(attendance, {
            attendanceMarkedSuccess.value = true
        }, {
            attendanceMarkedSuccess.value = false
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
                        "phoneNumber" to phoneNumber,
                        "password" to password
                    )

                    fireStore.collection("users").document(email).set(user)
                        .addOnSuccessListener {
                            registrationErrorMessage.value = "Registration success"
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






    private val _uploadProgress = MutableStateFlow(0)
    val uploadProgress: StateFlow<Int>get() = _uploadProgress

    fun uploadFile(uri: Uri, department: String, course: String, fileType: String, fileName: String, onSuccess: (String) -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                val storageRef = storage.reference.child("files/$department/$course/$fileType/$fileName")
                val uploadTask = storageRef.putFile(uri)

                uploadTask.addOnProgressListener { taskSnapshot ->
                    val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                    _uploadProgress.value = progress
                }.await()

                val downloadUrl = storageRef.downloadUrl.await()
                addFileToFirestore(department, course, fileType, fileName, downloadUrl.toString())
                onSuccess(downloadUrl.toString())
            } catch (e: Exception) {
                onFailure()
            } finally {
                _uploadProgress.value = 0
            }
        }
    }

    private fun addFileToFirestore(departmentName: String, courseName: String, fileType: String, fileName: String, fileUrl: String) {
        val fileData = hashMapOf(
            "name" to fileName,
            "url" to fileUrl,
            "timestamp" to FieldValue.serverTimestamp()
        )
        fireStore.collection("departments").document(departmentName)
            .collection("courses").document(courseName)
            .collection(fileType).document(fileName).set(fileData)
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
            val email = it.email ?: ""
            val basicProfile = UserProfile(
                firstName = it.displayName ?: "",
                email = email
            )

            return try {
                val documentSnapshot = fireStore.collection("users").document(it.email?:"").get().await()
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
        phoneNumber: String,
        email: String
    ) {

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

        fireStore.collection("users").document(email).update(updatedUser as Map<String, Any>)
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

    private fun saveFileMetadata(url: String, fileType: String, department: String, courseId: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val fileMetadata = hashMapOf(
            "url" to url,
            "type" to fileType,
            "department" to department,
            "courseId" to courseId,
            "timestamp" to System.currentTimeMillis()
        )

        fireStore.collection("files")
            .add(fileMetadata)
            .addOnSuccessListener { documentReference ->
                onSuccess(documentReference.id)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }


    var sc by mutableStateOf("")
    var sd by mutableStateOf("")






    private val _directories = MutableStateFlow<List<String>>(emptyList())
    val directories: StateFlow<List<String>> get() = _directories

    private val _subdirectories = MutableStateFlow<List<String>>(emptyList())
    val subdirectories: StateFlow<List<String>> get() = _subdirectories

    fun fetchDirectories() {
        viewModelScope.launch {
            try {
                val result = storage.reference.child("files/").listAll().await()
                val directories = result.prefixes.map { it.name }
                _directories.value = directories
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchSubdirectories(directoryName: String) {
        viewModelScope.launch {
            try {
                val result = storage.reference.child("files/$directoryName").listAll().await()
                val subdirectories = result.prefixes.map { it.name }
                _subdirectories.value = subdirectories
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun addDirectory(directoryName: String) {
        viewModelScope.launch {
            try {
                val directoryRef = storage.reference.child("files/$directoryName/")
                directoryRef.child(".keep").putBytes(byteArrayOf()).await()
                fetchDirectories() // Refresh the directory list
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun addSubdirectory(directoryName: String, subdirectoryName: String) {
        viewModelScope.launch {
            try {
                val subdirectoryRef = storage.reference.child("files/$directoryName/$subdirectoryName/")
                subdirectoryRef.child(".keep").putBytes(byteArrayOf()).await()
                fetchSubdirectories(directoryName) // Refresh the subdirectory list
            } catch (e: Exception) {
                // Handle error
            }
        }
    }


    ////////////////////////////////////////////////////


    // Function to determine file type based on file extension
    private val _courseFilesTypeNameMap = MutableStateFlow<Map<String, Map<String, Map<String, String>>>>(emptyMap())
    val courseFilesTypeNameMap: StateFlow<Map<String, Map<String, Map<String, String>>>> = _courseFilesTypeNameMap

    private val _courseFilesMap = MutableStateFlow<Map<String, StorageReference>>(emptyMap())
    val courseFilesMap: StateFlow<Map<String, StorageReference>> = _courseFilesMap

    private fun getFileType(fileName: String): String {
        val fileExtension = fileName.substringAfterLast('.')
        return when (fileExtension.lowercase(Locale.ROOT)) {
            "mp4", "mov", "avi" -> "Videos"
            "jpg", "jpeg", "png", "gif" -> "Images"
            "txt", "pdf", "doc", "docx" -> "Text Files"
            else -> "Other"
        }
    }

    fun fetchStorageReferences(courses: List<String>, department: String) {
        _courseFilesMap.value = courses.associateWith { course ->
            storage.reference.child("files/$department/$course")
        }
    }

    fun fetchFilesForCourses() {
        viewModelScope.launch {
            try {
                val result = fetchDownloadURIs(_courseFilesMap.value)
                _courseFilesTypeNameMap.value = result
                Log.d(TAG, "Files fetched successfully: $result")
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching files: ${e.message}", e)
            }
        }
    }

    private suspend fun fetchDownloadURIs(courseFilesMap: Map<String, StorageReference>): Map<String, Map<String, Map<String, String>>> {
        val resultMap = mutableMapOf<String, Map<String, Map<String, String>>>()

        for ((courseName, storageRef) in courseFilesMap) {
            val courseMap = mutableMapOf<String, Map<String, String>>()
            val items = storageRef.listAll().await()

            items.items.groupBy { getFileType(it.name) }.forEach { (fileType, storageItems) ->
                val fileMap = storageItems.associate { item ->
                    val downloadUrl = item.downloadUrl.await()
                    item.name to downloadUrl.toString()
                }
                courseMap[fileType] = fileMap
            }

            resultMap[courseName] = courseMap
        }

        return resultMap
    }

    val studentsList = MutableStateFlow<List<String>>(emptyList())

    fun fetchStudentsForCourse(courseId: String) {
        attendanceRepository.getStudentsForCourse(courseId, { students ->
            studentsList.value = students
        }, { exception ->
            // Handle the error, e.g., log it or show a message
        })
    }

    fun sendAttendance(courseId: String, date: String, status: Boolean) {
        studentsList.value.forEach { studentEmail ->
            val attendance = Attendance(
                attendanceId = UUID.randomUUID().toString(),
                courseId = courseId,
                studentId = studentEmail,
                date = date,
                status = status
            )
            attendanceRepository.markAttendance(attendance, {
                attendanceMarkedSuccess.value = true
            }, {
                attendanceMarkedSuccess.value = false
            })
        }
    }

    companion object {
        private const val TAG = "LogInAndCreateViewModel"
    }
}





