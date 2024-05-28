package com.example.codsoftuniversityattendance.models

data class Course(
    val courseId: String = "",
    val courseName: String = "",
    val lecturerId: String = ""
)

data class Attendance(
    val attendanceId: String = "",
    val courseId: String = "",
    val studentId: String = "",
    val date: String = "",
    val status: Boolean = false
)

data class CourseMaterial(
    val materialId: String = "",
    val courseId: String = "",
    val title: String = "",
    val url: String = ""
)
