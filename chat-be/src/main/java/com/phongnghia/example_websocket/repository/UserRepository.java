package com.phongnghia.example_websocket.repository;

import com.phongnghia.example_websocket.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findByUserCode(String userCode);

    UserEntity findByEmail(String email);

}
