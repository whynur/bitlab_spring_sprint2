package com.example.bitlab_spring_sprint2.repository;

import com.example.bitlab_spring_sprint2.model.Courses;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface CourseRepository extends JpaRepository<Courses, Long> {
}
