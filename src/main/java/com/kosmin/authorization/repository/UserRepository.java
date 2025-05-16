package com.kosmin.authorization.repository;

import com.kosmin.authorization.model.RegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<RegisterEntity, Long> {
  RegisterEntity findByUsername(String username);
}
