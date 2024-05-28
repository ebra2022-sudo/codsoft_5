package com.example.codsoftuniversityattendance.models

import com.google.firebase.firestore.FirebaseFirestore

class CourseRepository(private val firestore: FirebaseFirestore) {

    // Predefined courses by department
    private val coursesByDepartment = mapOf(
        "Bio-Medical Engineering" to listOf("Biomedical Instrumentation", "Medical Imaging"),
        "Chemical Engineering" to listOf("Chemical Process Principles", "Chemical Reaction Engineering"),
        "Civil Engineering" to listOf("Structural Analysis", "Geotechnical Engineering"),
        "Electrical Engineering" to listOf("Circuit Analysis", "Electro-Magnetics"),
        "Mechanical Engineering" to listOf("Thermodynamics", "Fluid Mechanics"),
        "Software Engineering" to listOf("Data Structures", "Operating Systems")
    )

    fun getCoursesForDepartment(
        department: String,
        onSuccess: (List<String>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("departments").document(department).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val courses = document.get("courses") as? List<String> ?: emptyList()
                    onSuccess(courses)
                } else {
                    onSuccess(emptyList()) // Or handle the case where the department doesn't exist
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

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

    fun markAttendance(attendance: Attendance, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("attendance").add(attendance)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }
}

class CourseMaterialRepository(private val firestore: FirebaseFirestore) {

    fun uploadMaterial(material: CourseMaterial, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("courseMaterials").add(material)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getMaterialsForCourse(courseId: String, onSuccess: (List<CourseMaterial>) -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("courseMaterials").whereEqualTo("courseId", courseId).get()
            .addOnSuccessListener { snapshot ->
                val materials = snapshot.documents.map { it.toObject(CourseMaterial::class.java)!! }
                onSuccess(materials)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }
}