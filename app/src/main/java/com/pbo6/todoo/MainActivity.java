package com.pbo6.todoo;

import android.os.Bundle;
import android.view.View;
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

    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList = new ArrayList<>();
    private FloatingActionButton fabAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // RecyclerView setup
        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this, taskList);
        taskRecyclerView.setAdapter(taskAdapter);

        // FloatingActionButton setup
        fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(v -> showAddTaskDialog());
    }

    // Method to show the dialog for adding a new task
    private void showAddTaskDialog() {
        // Create an EditText for user to input task name
        EditText taskInput = new EditText(this);
        taskInput.setHint("Enter task name");

        // Create an AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("Add New Task")
                .setView(taskInput)
                .setPositiveButton("Add", (dialog, which) -> {
                    String taskName = taskInput.getText().toString().trim();

                    if (!taskName.isEmpty()) {
                        // Add task to the list
                        Task newTask = new Task(taskName);
                        taskList.add(newTask);
                        taskAdapter.notifyItemInserted(taskList.size() - 1);

                        // Show confirmation
                        Toast.makeText(MainActivity.this, "Task added: " + taskName, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Task name cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
