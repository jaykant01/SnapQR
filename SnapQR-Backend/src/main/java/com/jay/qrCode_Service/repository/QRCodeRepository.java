package com.jay.qrCode_Service.repository;

import com.jay.qrCode_Service.entity.QRCodeEntity;
import com.jay.qrCode_Service.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCodeEntity, UUID> {
    List<QRCodeEntity> findByUser(UserEntity user);
}
