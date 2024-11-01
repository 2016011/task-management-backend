package com.example.taskmanagementback.services;

import com.example.taskmanagementback.modals.Tasks;
import com.example.taskmanagementback.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;


    public Tasks saveOrUpdateTasks(Tasks tasks) {
        Tasks tasksEntity = new Tasks();
        if (tasks.getTaskId()== null) {
            tasksEntity.setTitle(tasks.getTitle());
            tasksEntity.setDescription(tasks.getDescription());
            tasksEntity.setStatus(tasks.getStatus());
        } else {
            // Update the existing tasks from the database
            tasksEntity = taskRepository.findByTaskId(tasks.getTaskId())
                    .orElseThrow(() -> new RuntimeException("Task not found"));
            if (tasks.getTitle() != null) tasksEntity.setTitle(tasks.getTitle());
            if (tasks.getDescription() != null) tasksEntity.setDescription(tasks.getDescription());
            if (tasks.getStatus() != null) tasksEntity.setStatus(tasks.getStatus());
        }
        return taskRepository.save(tasksEntity);
    }

    // Fetch all tasks
    public List<Tasks> getAllTasks() {
            return taskRepository.findAll();
    }

    // Fetch tasks by ID
    public Tasks getTasksById(Long tasksId) {
        return taskRepository.findByTaskId(tasksId)
                .orElseThrow(() -> new RuntimeException("Tasks not found"));
    }

    public void deleteTasks(Long taskId) {
        Tasks tasks = taskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new RuntimeException("Tasks not found"));

        // Delete the tasks from the database
        taskRepository.delete(tasks);
    }
}
