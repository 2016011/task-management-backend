package com.example.taskmanagementback.repositories;

import com.example.taskmanagementback.modals.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Tasks, Long> {
    Optional<Tasks> findByTaskId(Long id);
}
