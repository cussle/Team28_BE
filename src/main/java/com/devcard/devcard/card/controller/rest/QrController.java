package com.devcard.devcard.card.controller.rest;

import com.devcard.devcard.card.service.QrServiceImpl;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
public class QrController {

    private final QrServiceImpl qrServiceImpl;

    @Autowired
    public QrController(QrServiceImpl qrServiceImpl) {
        this.qrServiceImpl = qrServiceImpl;
    }

    @GetMapping("/cards/{card_id}/qrcode-image")
    public ResponseEntity<byte[]> generateQrImage(@PathVariable(name = "card_id") Long cardId) throws IOException, WriterException {
        ByteArrayOutputStream qrImageStream = qrServiceImpl.generateQrImageStream(cardId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"")
                .contentType(MediaType.IMAGE_PNG)
                .body(qrImageStream.toByteArray());
    }
}
