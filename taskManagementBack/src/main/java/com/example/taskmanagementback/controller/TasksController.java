package com.example.taskmanagementback.controller;

import com.example.taskmanagementback.modals.Tasks;
import com.example.taskmanagementback.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TasksController {
    @Autowired
    private TaskService taskService;

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<Tasks> createTasks(@RequestBody Tasks tasks) {
        if (tasks.getTaskId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Id should be null for new tasks
        }
        Tasks newTasks = taskService.saveOrUpdateTasks(tasks);
        return new ResponseEntity<>(newTasks, HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/all")
    public ResponseEntity<List<Tasks>> getAllTasks() {
        List<Tasks> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/get/{id}")
    public ResponseEntity<Tasks> getTaskById(@PathVariable Long id) {
        Tasks tasks = taskService.getTasksById(id);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Update an existing tasks
    @CrossOrigin
    @PutMapping("/update")
    public ResponseEntity<Tasks> updateUser(@RequestBody Tasks tasks) {
        if (tasks.getTaskId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Id should not be null for updates
        }
        Tasks updatedTasks = taskService.saveOrUpdateTasks(tasks);
        return new ResponseEntity<>(updatedTasks, HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTasks(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content if successful
    }
}
