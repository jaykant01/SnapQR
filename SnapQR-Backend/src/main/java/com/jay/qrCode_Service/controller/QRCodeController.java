package com.jay.qrCode_Service.controller;

import com.google.zxing.WriterException;
import com.jay.qrCode_Service.dto.QRCodeRequest;
import com.jay.qrCode_Service.dto.QRCodeResponse;
import com.jay.qrCode_Service.entity.QRCodeEntity;
import com.jay.qrCode_Service.entity.UserEntity;
import com.jay.qrCode_Service.repository.QRCodeRepository;
import com.jay.qrCode_Service.repository.UserRepository;
import com.jay.qrCode_Service.service.QRCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/qrcode")
@CrossOrigin
@RequiredArgsConstructor
public class QRCodeController {

    private final QRCodeService qrCodeService;
    private final QRCodeRepository qrCodeRepository;
    private final UserRepository userRepository;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generate(@RequestBody QRCodeRequest request, Authentication auth) throws IOException, WriterException {
        String email = auth.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        byte[] qrImage = qrCodeService.generateQRCodeImage(request, user);

        MediaType mediaType = switch (request.getType().toLowerCase()) {
            case "jpeg", "jpg" -> MediaType.IMAGE_JPEG;
            case "gif" -> MediaType.IMAGE_GIF;
            default -> MediaType.IMAGE_PNG;
        };

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(qrImage);
    }

    @GetMapping("/history")
    public List<QRCodeResponse> getMyHistory(Authentication auth) {
        String email = auth.getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return qrCodeService.getUserQRHistory(user);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadQR(@PathVariable UUID id, Authentication auth) {
        String email = auth.getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        QRCodeEntity qr = qrCodeService.getORCodeByID(id, user);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(qr.getImageType()));

        String ext = qr.getImageType().replace("image/", "");

        headers.set(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"qr_" + id + "." + ext + "\""
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(qr.getImageBytes());
    }

    // Delete By Id to delete qr
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteQR(@PathVariable UUID id, Authentication auth) {
        String email = auth.getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        qrCodeService.deleteQRCode(id, user, email);

        return ResponseEntity.ok("QR Code deleted successfully");
    }

}
