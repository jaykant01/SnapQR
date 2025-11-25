package com.jay.qrCode_Service.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jay.qrCode_Service.dto.QRCodeRequest;
import com.jay.qrCode_Service.dto.QRCodeResponse;
import com.jay.qrCode_Service.entity.QRCodeEntity;
import com.jay.qrCode_Service.entity.UserEntity;
import com.jay.qrCode_Service.repository.QRCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QRCodeService {

    private final QRCodeRepository qrCodeRepository;

    // QR Generate Method
    public byte[] generateQRCodeImage(QRCodeRequest request, UserEntity user) throws WriterException, IOException {

        int size = 300;

        String type = request.getType().toLowerCase();
        if (!(type.equals("png") || type.equals("jpeg") || type.equals("gif"))) {
            throw new IllegalArgumentException("Only png, jpeg, gif are supported");
        }

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(request.getContent(), BarcodeFormat.QR_CODE, size, size);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, type.toUpperCase(), output);
        byte[] imageBytes = output.toByteArray();

        //save entity in db
        QRCodeEntity qr = QRCodeEntity.builder().content(request.getContent()).imageBytes(imageBytes).imageType("image/" + type).imageUrl("").user(user).createdAt(Instant.now()).build();

        qrCodeRepository.save(qr);

        return imageBytes;
    }

    // QrCode History Method
    public List<QRCodeResponse> getUserQRHistory(UserEntity user) {
        List<QRCodeEntity> list = qrCodeRepository.findByUser(user);

        return list.stream().sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt())).map(qr -> {
            QRCodeResponse dto = new QRCodeResponse();
            dto.setId(qr.getId());
            dto.setContent(qr.getContent());
            dto.setImageUrl(qr.getImageUrl());
            dto.setCreatedAt(qr.getCreatedAt());
            return dto;
        }).toList();

    }

    // QR Download Service
    public QRCodeEntity getORCodeByID(UUID id, UserEntity user) {
        QRCodeEntity qr = qrCodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QR Code not found"));

        if (!qr.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not allowed");
        }

        return qr;
    }

    public void deleteQRCode(UUID id, UserEntity user, String email) {
        QRCodeEntity qr = qrCodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QR Code not found"));

        if (!qr.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Not allowed to delete this QR code");
        }

        qrCodeRepository.deleteById(id);
    }
}
