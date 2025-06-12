package com.phongnghia.example_websocket.repository;

import com.phongnghia.example_websocket.entity.code.VerifyCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VerifyCodeRepository extends JpaRepository<VerifyCodeEntity, UUID> {

    VerifyCodeEntity findByCode (String code);

}
