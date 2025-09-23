package org.kir.bookingservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kir.commonservice.cache.QueueMessage;
import com.kir.commonservice.constant.AppConstants;
import com.kir.commonservice.constant.QueueConstant;
import com.kir.commonservice.dto.request.TicketEmailRequest;
import lombok.RequiredArgsConstructor;
import org.kir.bookingservice.dto.BookingRequest;
import org.kir.bookingservice.dto.BookingResponse;
import org.kir.bookingservice.entity.Order;
import org.kir.bookingservice.entity.OrderItem;
import org.kir.bookingservice.entity.Passenger;
import org.kir.bookingservice.repository.OrderItemRepository;
import org.kir.bookingservice.repository.OrderRepository;
import org.kir.bookingservice.repository.PassengerRepository;
import org.kir.bookingservice.service.BookingService;
import org.kir.bookingservice.client.NotificationClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import static com.kir.commonservice.config.RabbitMQConfig.EXCHANGE;
import static com.kir.commonservice.config.RabbitMQConfig.ROUTING_KEY;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PassengerRepository passengerRepository;
    private final NotificationClient notificationClient;

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        String orderCode = generateOrderCode();
        Order order = Order.builder()
                .customerId(request.getCustomerId())
                .orderCode(orderCode)
                .orderDate(LocalDateTime.now())
                .totalAmount(request.getPrice())
                .paymentStatus(AppConstants.PaymentStatus.PENDING)
                .bookingStatus(AppConstants.BookingStatus.CREATED)
                .isDelete(false)
                .build();
        order = orderRepository.save(order);

        OrderItem item = OrderItem.builder()
                .order(order)
                .itemType(request.getItemType())
                .itemReferenceId(request.getItemReferenceId())
                .departureLocationCode(request.getDepartureLocationCode())
                .arrivalLocationCode(request.getArrivalLocationCode())
                .departureTime(request.getDepartureTime())
                .arrivalTime(request.getArrivalTime())
                .price(request.getPrice())
                .seatNumber(null)
                .isDelete(false)
                .build();
        item = orderItemRepository.save(item);

        List<BookingResponse.TicketInfo> ticketInfos = new ArrayList<>();
        List<TicketEmailRequest.TicketPassenger> emailPassengers = new ArrayList<>();
        if (request.getPassengers() != null) {
            for (BookingRequest.PassengerRequest p : request.getPassengers()) {
                Passenger passenger = Passenger.builder()
                        .orderItem(item)
                        .firstName(p.getFirstName())
                        .lastName(p.getLastName())
                        .gender(p.getGender())
                        .identityDocumentType(p.getIdentityDocumentType())
                        .identityDocumentNumber(p.getIdentityDocumentNumber())
                        .isDelete(false)
                        .build();
                passengerRepository.save(passenger);

                emailPassengers.add(TicketEmailRequest.TicketPassenger.builder()
                        .firstName(p.getFirstName())
                        .lastName(p.getLastName())
                        .identityDocumentNumber(p.getIdentityDocumentNumber())
                        .seatNumber(p.getSeatNumber())
                        .build());

                ticketInfos.add(BookingResponse.TicketInfo.builder()
                        .orderItemId(item.getId())
                        .seatNumber(p.getSeatNumber())
                        .qrImageBase64(null)
                        .build());
            }
        }

        TicketEmailRequest emailPayload = TicketEmailRequest.builder()
                .to(request.getCustomerEmail())
                .orderCode(orderCode)
                .itemType(request.getItemType())
                .itemReferenceId(request.getItemReferenceId())
                .departureLocationCode(request.getDepartureLocationCode())
                .arrivalLocationCode(request.getArrivalLocationCode())
                .departureTime(request.getDepartureTime())
                .arrivalTime(request.getArrivalTime())
                .passengers(emailPassengers)
                .build();

        // call notification-service via Feign
        notificationClient.sendTicket(emailPayload);

        return BookingResponse.builder()
                .orderId(order.getId())
                .orderCode(orderCode)
                .bookingStatus(order.getBookingStatus())
                .paymentStatus(order.getPaymentStatus())
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .tickets(ticketInfos)
                .build();
    }

    private String generateOrderCode() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // no-op: email handled by notification-service via Feign
}


