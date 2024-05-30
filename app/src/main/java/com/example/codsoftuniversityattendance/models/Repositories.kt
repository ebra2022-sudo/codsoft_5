package com.example.codsoftuniversityattendance.models

import com.google.firebase.firestore.FirebaseFirestore

class CourseRepository(private val firestore: FirebaseFirestore) {


    fun registerForCourse(
        courseId: String,
        studentId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val registration = hashMapOf(
            "courseId" to courseId,
            "studentId" to studentId
        )

        firestore.collection("registrations")
            .add(registration)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getRegisteredCourses(
        studentId: String,
        onSuccess: (List<String>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("registrations")
            .whereEqualTo("studentId", studentId)
            .get()
            .addOnSuccessListener { documents ->
                val courses = documents.mapNotNull { it.getString("courseId") }
                onSuccess(courses)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun dropCourse(
        courseId: String,
        studentId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("registrations")
            .whereEqualTo("courseId", courseId)
            .whereEqualTo("studentId", studentId)
            .get()
            .addOnSuccessListener { documents ->
                val batch = firestore.batch()
                for (document in documents) {
                    batch.delete(document.reference)
                }
                batch.commit()
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e) }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

}




class AttendanceRepository(private val firestore: FirebaseFirestore) {



    fun getStudentsForCourse(courseId: String, onSuccess: (List<String>) -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("courseRegistrations")
            .whereEqualTo("courseId", courseId)
            .get()
            .addOnSuccessListener { result ->
                val students = result.map { document -> document.getString("studentEmail") ?: "" }
                onSuccess(students)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun markAttendance(attendance: Attendance, onSuccess: () -> Unit, onFailure: () -> Unit) {
        firestore.collection("attendances")
            .add(attendance)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure() }
    }

    fun getAttendanceRequests(courseId: String, onSuccess: (List<AttendanceRequest>) -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("attendanceRequests")
            .whereEqualTo("courseId", courseId)
            .get()
            .addOnSuccessListener { result ->
                val requests = result.map { document ->
                    AttendanceRequest(
                        courseId = document.getString("courseId") ?: "",
                        date = document.getString("date") ?: ""
                    )
                }
                onSuccess(requests)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}



object Repository {
    fun getCoursesForStudent(studentId: String): List<Course> {
        // Mock data
        return listOf(
            Course(id = "1", name = "Mathematics"),
            Course(id = "2", name = "Physics")
        )
    }

    fun getMaterialsForCourse(courseName: String): List<Material> {
        // Mock data
        return when (courseName) {
            "1" -> listOf(
                Material(id = "1", courseId = "1", fileName = "Lecture 1", fileType = "pdf", fileUrl = "https://example.com/lecture1.pdf"),
                Material(id = "2", courseId = "1", fileName = "Lecture 2", fileType = "pdf", fileUrl = "https://example.com/lecture2.pdf")
            )
            "2" -> listOf(
                Material(id = "3", courseId = "2", fileName = "Lab 1", fileType = "pdf", fileUrl = "https://example.com/lab1.pdf"),
                Material(id = "4", courseId = "2", fileName = "Lab 2", fileType = "pdf", fileUrl = "https://example.com/lab2.pdf")
            )
            else -> emptyList()
        }
    }
}