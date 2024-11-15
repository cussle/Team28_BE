package com.devcard.devcard.card.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QrServiceImpl {

    private static final int QR_SIZE_WIDTH = 200;
    private static final int QR_SIZE_HEIGHT = 200;

    @Value("${qr.domain.uri}")
    private String domainUri;

    public ByteArrayOutputStream generateQrImageStream(Long cardId) throws WriterException, IOException {
        // QR URL 생성
        String url = domainUri + "shared/cards/" + cardId;

        // QR Code 생성
        BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, QR_SIZE_WIDTH, QR_SIZE_HEIGHT);

        // QR Code 이미지를 메모리 스트림에 저장
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return outputStream;
    }
}
