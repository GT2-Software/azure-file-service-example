package dev.gt2software.main.services.v1;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.json.JSONException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRService {

    /**
     * 
     * @param qrCodeText LPAcode to be converted to QR code
     * @return
     * @throws WriterException
     * @throws JSONException
     * @throws IOException
     * @throws NullPointerException
     */
    public String createQR(String qrCodeText) throws WriterException, JSONException, IOException, NullPointerException {

        if (qrCodeText == null || qrCodeText.isEmpty()) {
            throw new NullPointerException("El texto del código QR no puede ser nulo o vacío.");
        }

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        int qrCodeWidth = 200;
        int qrCodeHeight = 200;
        // Generate the QR code matrix
        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(qrCodeText, BarcodeFormat.QR_CODE, qrCodeWidth, qrCodeHeight, hints);

        // Create the BufferedImage object
        BufferedImage qrImage = new BufferedImage(qrCodeWidth, qrCodeHeight, BufferedImage.TYPE_INT_RGB);

        // Write QR code to BufferedImage object
        for (int x = 0; x < qrCodeWidth; x++) {
            for (int y = 0; y < qrCodeHeight; y++) {
                qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        // Convert BufferedImage to Base64 encoded image for further processing
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "jpg", baos);
        baos.flush();

        // Convert BufferedImage to Base64 encoded image for further processing
        byte[] imageBytes = baos.toByteArray();
        baos.close();

        String base64QRCode = Base64.getEncoder().encodeToString(imageBytes);
        return base64QRCode;
    }
}
