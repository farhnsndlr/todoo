package com.pbo6.todoo;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList = new ArrayList<>();
    private FloatingActionButton fabAddTask;
    private TaskDatabaseHelper taskDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskDatabaseHelper = new TaskDatabaseHelper(this);

        // Inisialisasi RecyclerView dan FloatingActionButton
        recyclerView = findViewById(R.id.taskRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inisialisasi adapter dan hubungkan ke RecyclerView
        taskAdapter = new TaskAdapter(this, taskList);
        recyclerView.setAdapter(taskAdapter);

        // Inisialisasi tombol tambah task
        fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(v -> showAddTaskDialog());

        // Memuat data task dari database SQLite
        loadTasksFromDatabase();
    }

    private void loadTasksFromDatabase() {
        taskList.clear();
        taskList.addAll(taskDatabaseHelper.getAllTasks()); // Memuat semua task dari database
        taskAdapter.notifyDataSetChanged();
    }

    private void showAddTaskDialog() {
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add New Task")
                .setMessage("Enter task name:")
                .setView(taskEditText)
                .setPositiveButton("Add", (dialog1, which) -> {
                    String taskName = taskEditText.getText().toString();
                    if (!taskName.isEmpty()) {
                        addNewTask(taskName);
                    } else {
                        Toast.makeText(MainActivity.this, "Task name cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void addNewTask(String taskName) {
        // Task baru akan ditambahkan dengan ID yang otomatis dari SQLite (gunakan saveTask)
        Task newTask = new Task(0, taskName, false); // ID otomatis akan diatur oleh SQLite
        taskDatabaseHelper.saveTask(newTask); // Simpan task ke database
        loadTasksFromDatabase(); // Muat ulang data dari database
    }

    public void deleteTask(int position) {
        if (position < 0 || position >= taskList.size()) {
            return; // Cek apakah posisi valid
        }

        Task taskToDelete = taskList.get(position);
        if (taskToDelete.getTaskName().equals("Task 2")) {
            Toast.makeText(this, "Cannot delete Task 2", Toast.LENGTH_SHORT).show();
            return;
        }

        taskDatabaseHelper.deleteTask(taskToDelete); // Hapus task dari database
        taskList.remove(position); // Hapus task dari list
        taskAdapter.notifyItemRemoved(position); // Notifikasi adapter bahwa item telah dihapus
    }

    public void updateTaskCompletionStatus(int position, boolean isChecked) {
        Task task = taskList.get(position);
        task.setCompleted(isChecked); // Perbarui status task

        // Update status completion di database
        taskDatabaseHelper.updateTask(task);

        // Notifikasi adapter bahwa data telah diubah
        taskAdapter.notifyItemChanged(position);
    }
}
