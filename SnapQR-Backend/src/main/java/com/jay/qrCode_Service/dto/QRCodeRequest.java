package com.jay.qrCode_Service.dto;

import lombok.Data;

@Data
public class QRCodeRequest {
    private String content;

    private String type;
}
