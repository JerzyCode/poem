package com.example.poem.core.model.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDataRepository extends JpaRepository<UserData, Long> {
  Optional<UserData> findByUser(User user);
}
