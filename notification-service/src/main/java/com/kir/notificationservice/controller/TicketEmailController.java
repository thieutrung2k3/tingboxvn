package com.kir.notificationservice.controller;

import com.kir.commonservice.dto.ApiResponse;
import com.kir.commonservice.dto.request.TicketEmailRequest;
import com.kir.notificationservice.service.impl.EmailSenderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class TicketEmailController {

    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;
    private final com.kir.notificationservice.service.EmailSenderService emailSenderService;

    @PostMapping("/ticket")
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
            java.util.Map<com.google.zxing.EncodeHintType, Object> hints = new java.util.HashMap<>();
            hints.put(com.google.zxing.EncodeHintType.MARGIN, 1);
            com.google.zxing.common.BitMatrix matrix = new com.google.zxing.MultiFormatWriter()
                    .encode(content, com.google.zxing.BarcodeFormat.QR_CODE, width, height, hints);
            java.awt.image.BufferedImage image = com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage(matrix);
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            javax.imageio.ImageIO.write(image, "png", baos);
            return java.util.Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
}


