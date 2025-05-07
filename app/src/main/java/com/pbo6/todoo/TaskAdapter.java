package com.pbo6.todoo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private Context context;
    private TaskDatabaseHelper dbHelper;

    // Constructor
    public TaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        this.dbHelper = new TaskDatabaseHelper(context);
    }

    // Setters
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.taskNameTextView.setText(task.getTaskName());
        holder.checkbox.setChecked(task.isCompleted());

        // Handle checkbox click (update task completion status)
        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            dbHelper.updateTask(task);  // Update task completion status in database
        });

        // Handle Delete button click with confirmation
        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to delete this task?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        Toast.makeText(context, "Deleted: " + task.getTaskName(), Toast.LENGTH_SHORT).show();

                        // Inform MainActivity to handle deletion
                        if (context instanceof MainActivity) {
                            ((MainActivity) context).deleteTask(position);  // Call MainActivity's deleteTask method
                        }

                        // Optionally, delete task from database or API here
                        dbHelper.deleteTask(task);  // Delete task from database
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return tasks != null ? tasks.size() : 0;
    }

    // TaskViewHolder class
    public class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView taskNameTextView;
        CheckBox checkbox;
        Button deleteButton;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskNameTextView = itemView.findViewById(R.id.taskName);
            checkbox = itemView.findViewById(R.id.checkboxTask); // Checkbox
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
