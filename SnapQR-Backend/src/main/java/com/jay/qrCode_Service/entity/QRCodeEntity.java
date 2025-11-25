package com.jay.qrCode_Service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "qr_codes")
public class QRCodeEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String content;

    //(required for immediate download)
    @Column(nullable = false)
    @Basic(fetch = FetchType.LAZY)
    private byte[] imageBytes;

    // image/png, image/jpeg, image/gif
    @Column(nullable = false)
    private String imageType;

    @Column(nullable = false)
    private String imageUrl = "";

    @Column(nullable = false)
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
