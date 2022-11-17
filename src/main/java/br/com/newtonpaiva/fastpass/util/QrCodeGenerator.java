package br.com.newtonpaiva.fastpass.util;

import br.com.newtonpaiva.fastpass.dto.QrCodeResponseDTO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;

@UtilityClass
public class QrCodeGenerator {

    private static final String DATA_FILE_JPEG_BASE_64 = "data:@file/jpeg;base64,";
    @Value("${api.base.url}")
    private String apiBaseUrl;

    public static QrCodeResponseDTO qrCodeEventBuilder(Integer eventId, Integer userId) {
        int imageSize = 400;

        try {

            String url = String.format("%s/events/buy/%s/%s", apiBaseUrl, userId.toString(), eventId.toString());
            BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE,
                    imageSize, imageSize);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "jpeg", bos);

            return QrCodeResponseDTO.builder()
                    .qrCode(DATA_FILE_JPEG_BASE_64 + Base64.getEncoder().encodeToString(bos.toByteArray()))
                    .eventId(eventId)
                    .build();

        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static QrCodeResponseDTO qrCodeRechargeBuilder(BigDecimal value, Integer userId) {
        int imageSize = 400;

        try {

            String url = String.format("%s/recharges/buy/%s/%s", apiBaseUrl, userId.toString(), value.toString());
            BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE,
                    imageSize, imageSize);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "jpeg", bos);

            return QrCodeResponseDTO.builder()
                    .qrCode(DATA_FILE_JPEG_BASE_64 + Base64.getEncoder().encodeToString(bos.toByteArray()))
                    .userId(userId)
                    .value(value)
                    .build();

        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
