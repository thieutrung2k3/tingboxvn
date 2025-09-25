package com.kir.notificationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.kir.commonservice.dto.ApiResponse;
import com.kir.commonservice.dto.request.TicketEmailRequest;
import com.kir.notificationservice.service.EmailSenderService;
import com.kir.notificationservice.service.impl.EmailSenderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class TicketEmailController {

    private final ObjectMapper objectMapper;
    private final EmailSenderService emailSenderService;

    @PostMapping("/public/ticket")
    public ApiResponse<String> sendTicketEmail(@RequestBody TicketEmailRequest req) {
        String html = buildTicketHtml(req);
        String subject = "Your e-ticket - " + req.getOrderCode();
        emailSenderService.send(req.getTo(), subject, html);
        return ApiResponse.data("SENT");
    }

    private String buildTicketHtml(TicketEmailRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>TingBox VN - E-Ticket ").append(req.getOrderCode()).append("</h2>");
        sb.append("<p>From ").append(req.getDepartureLocationCode()).append(" to ").append(req.getArrivalLocationCode()).append("</p>");
        sb.append("<p>Departure: ").append(req.getDepartureTime()).append("</p>");
        sb.append("<p>Arrival: ").append(req.getArrivalTime()).append("</p>");
        sb.append("<hr/>");
        if (req.getPassengers() != null) {
            for (TicketEmailRequest.TicketPassenger p : req.getPassengers()) {
                String qrPayload = req.getOrderCode() + "|" + req.getItemReferenceId() + "|" + p.getIdentityDocumentNumber();
                String qrBase64 = generateQrCodeBase64(qrPayload, 280, 280);
                sb.append("<div style='margin-bottom:16px'>");
                sb.append("<p>Passenger: ").append(p.getFirstName()).append(" ").append(p.getLastName()).append("</p>");
                sb.append("<p>Seat: ").append(p.getSeatNumber() == null ? "N/A" : p.getSeatNumber()).append("</p>");
                sb.append("<img alt='QR' src='data:image/png;base64,").append(qrBase64).append("' />");
                sb.append("</div>");
            }
        }
        return sb.toString();
    }

    private String generateQrCodeBase64(String content, int width, int height) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix matrix = new MultiFormatWriter()
                    .encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
}


