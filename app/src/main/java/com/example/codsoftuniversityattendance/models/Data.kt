package com.example.codsoftuniversityattendance.models



data class Attendance(
    val attendanceId: String = "",
    val courseId: String = "",
    val studentId: String = "",
    val date: String = "",
    val status: Boolean = false
)

data class AttendanceRequest(
    val courseId: String,
    val date: String
)




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
    var phoneNumber: String = "",
    var password: String = ""
)

data class FileMetadata(
    val url: String = "",
    val type: String = "",
    val department: String = "",
    val courseId: String = "",
    val timestamp: Long = 0L
)




enum class Status {
    Student, Lecturer
}

data class Course(val id: String, val name: String)
data class Material(val id: String, val courseId: String, val fileName: String, val fileType: String, val fileUrl: String)