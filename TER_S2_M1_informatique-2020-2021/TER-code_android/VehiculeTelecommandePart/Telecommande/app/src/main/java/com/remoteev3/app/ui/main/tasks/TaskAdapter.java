package com.remoteev3.app.ui.main.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.remoteev3.app.R;

import com.remoteev3.app.domain.Task;

import java.util.ArrayList;
import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private List<Task> tasks = new ArrayList<>();

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.textViewTitle.setText(currentTask.getTitle());
        holder.textViewStatus.setText(String.valueOf(currentTask.getStatus()));
        holder.textViewTimestamp.setText(currentTask.getTimestamp());
        holder.textViewEffectiveTimestamp.setText(currentTask.getEffectiveTimestamp());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewTimestamp;
        private TextView textViewEffectiveTimestamp;
        private TextView textViewStatus;

        public TaskHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewTimestamp = itemView.findViewById(R.id.text_view_timestamp);
            textViewEffectiveTimestamp = itemView.findViewById(R.id.text_view_effective_timestamp);
            textViewStatus = itemView.findViewById(R.id.text_view_status);
        }

    }
}
