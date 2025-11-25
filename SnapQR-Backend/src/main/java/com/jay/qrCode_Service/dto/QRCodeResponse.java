package com.jay.qrCode_Service.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class QRCodeResponse {
    private UUID id;
    private String content;
    private String imageUrl;
    private Instant createdAt;
}
