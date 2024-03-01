package com.example.studentsystemspring.repository;

import com.example.studentsystemspring.models.User;
import com.example.studentsystemspring.models.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Override
    Optional<User> findById(String id);
    User findByUserType(UserType userType);
}
