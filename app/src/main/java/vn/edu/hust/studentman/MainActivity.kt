package vn.edu.hust.studentman

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
  private var students: MutableList<StudentModel> = mutableListOf(
    StudentModel("Nguyễn Văn An", "SV001"),
    StudentModel("Trần Thị Bảo", "SV002"),
    StudentModel("Lê Hoàng Cường", "SV003"),
    StudentModel("Phạm Thị Dung", "SV004"),
    StudentModel("Đỗ Minh Đức", "SV005"),
    StudentModel("Vũ Thị Hoa", "SV006"),
    StudentModel("Hoàng Văn Hải", "SV007"),
    StudentModel("Bùi Thị Hạnh", "SV008"),
    StudentModel("Đinh Văn Hùng", "SV009"),
    StudentModel("Nguyễn Thị Linh", "SV010"),
    StudentModel("Phạm Văn Long", "SV011"),
    StudentModel("Trần Thị Mai", "SV012"),
    StudentModel("Lê Thị Ngọc", "SV013"),
    StudentModel("Vũ Văn Nam", "SV014"),
    StudentModel("Hoàng Thị Phương", "SV015"),
    StudentModel("Đỗ Văn Quân", "SV016"),
    StudentModel("Nguyễn Thị Thu", "SV017"),
    StudentModel("Trần Văn Tài", "SV018"),
    StudentModel("Phạm Thị Tuyết", "SV019"),
    StudentModel("Lê Văn Vũ", "SV020")
  )
  private lateinit var studentAdapter: StudentAdapter
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)


    studentAdapter = StudentAdapter(
      students,
      onEditClick = { student, position ->
        showEditStudentDialog(student, position)
      },
      onDeleteClick = { student, position ->
        showDeleteConfirmationDialog(student, position)
      }
    )

    findViewById<RecyclerView>(R.id.recycler_view_students).run {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      showAddStudentDialog(students, studentAdapter)
    }
  }

  private fun showAddStudentDialog(students: MutableList<StudentModel>, adapter: StudentAdapter) {
    val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)

    val editTextName = dialogView.findViewById<EditText>(R.id.edit_text_student_name)
    val editTextId = dialogView.findViewById<EditText>(R.id.edit_text_student_id)

    AlertDialog.Builder(this)
      .setTitle("Add New Student")
      .setView(dialogView)
      .setPositiveButton("Add") { _, _ ->
        val name = editTextName.text.toString().trim()
        val id = editTextId.text.toString().trim()

        if (name.isNotEmpty() && id.isNotEmpty()) {
          val newStudent = StudentModel(name, id)
          students.add(newStudent)
          adapter.notifyItemInserted(students.size - 1)
        } else {
            showErrorDialog("Both fields are required.")
        }
      }
      .setNegativeButton("Cancel", null)
      .create()
      .show()
  }

  private fun showErrorDialog(message: String){
    AlertDialog.Builder(this)
      .setTitle("Error")
      .setMessage(message)
      .setPositiveButton("OK", null)
      .create()
      .show()
  }

  private fun showEditStudentDialog(student: StudentModel, position: Int) {

    val dialogView = layoutInflater.inflate(R.layout.dialog_edit_student, null)
    val editTextName = dialogView.findViewById<EditText>(R.id.edit_text_student_name)
    val editTextId = dialogView.findViewById<EditText>(R.id.edit_text_student_id)
    editTextName.setText(student.studentName)
    editTextId.setText(student.studentId)

    AlertDialog.Builder(this)
      .setTitle("Edit Student")
      .setView(dialogView)
      .setPositiveButton("Save") { _, _ ->
        val updatedName = editTextName.text.toString().trim()
        val updatedId = editTextId.text.toString().trim()

        if (updatedName.isNotEmpty() && updatedId.isNotEmpty()) {
          students[position] = StudentModel(updatedName, updatedId)
          studentAdapter.notifyItemChanged(position)
        } else {
          showErrorDialog("Both fields are required.")
        }
      }
      .setNegativeButton("Cancel", null)
      .create()
      .show()
  }

  private fun showDeleteConfirmationDialog(student: StudentModel, position: Int) {
    AlertDialog.Builder(this)
      .setTitle("Delete Student")
      .setMessage("Are you sure you want to delete ${student.studentName}?")
      .setPositiveButton("Delete") { _, _ ->
        val removedStudent = students.removeAt(position)
        studentAdapter.notifyItemRemoved(position)

        Snackbar.make(findViewById(R.id.recycler_view_students), "${removedStudent.studentName} deleted", Snackbar.LENGTH_LONG)
          .setAction("Undo") {
            students.add(position, removedStudent)
            studentAdapter.notifyItemInserted(position)
          }
          .show()
      }
      .setNegativeButton("Cancel", null)
      .create()
      .show()
  }
}