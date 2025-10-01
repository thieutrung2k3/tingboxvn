package org.kir.searchservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.kir.searchservice.dto.request.SeatSelectionRequest;
import org.kir.searchservice.dto.response.SeatMapResponse;
import org.kir.searchservice.dto.response.SeatResponse;
import org.kir.searchservice.entity.Seat;
import org.kir.searchservice.entity.Trip;
import org.kir.searchservice.mapper.SeatMapper;
import org.kir.searchservice.repository.SeatRepository;
import org.kir.searchservice.repository.TripRepository;
import org.kir.searchservice.service.SeatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final TripRepository tripRepository;
    private final SeatMapper seatMapper;

    @Override
    public SeatMapResponse getSeatMapByTripId(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));

        List<Seat> seats = seatRepository.findByTripIdAndIsDeleteFalseOrderByRowNumberAscColumnLetterAsc(tripId);
        
        // Group seats by class
        Map<String, List<SeatResponse>> seatsByClass = seats.stream()
                .collect(Collectors.groupingBy(
                    Seat::getSeatClass,
                    Collectors.mapping(seatMapper::toSeatResponse, Collectors.toList())
                ));

        // Count available seats by class
        Map<String, Long> availableCountByClass = seats.stream()
                .filter(seat -> seat.getStatus() == Seat.SeatStatus.AVAILABLE)
                .collect(Collectors.groupingBy(
                    Seat::getSeatClass,
                    Collectors.counting()
                ));

        // Get unique column letters
        List<String> columnLetters = seats.stream()
                .map(Seat::getColumnLetter)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        // Calculate total rows and columns
        int totalRows = seats.stream().mapToInt(Seat::getRowNumber).max().orElse(0);
        int totalColumns = columnLetters.size();

        return SeatMapResponse.builder()
                .tripId(tripId)
                .tripReferenceId(trip.getTripReferenceId())
                .vehicleInfo(trip.getVehicleInfo())
                .seatsByClass(seatsByClass)
                .availableCountByClass(availableCountByClass)
                .totalRows(totalRows)
                .totalColumns(totalColumns)
                .columnLetters(columnLetters)
                .build();
    }

    @Override
    public List<SeatResponse> getAvailableSeatsByTripId(Long tripId) {
        List<Seat> seats = seatRepository.findAvailableSeatsByTripId(tripId);
        return seatMapper.toSeatResponseList(seats);
    }

    @Override
            public List<SeatResponse> getAvailableSeatsByTripIdAndClass(Long tripId, String seatClass) {
        List<Seat> seats = seatRepository.findAvailableSeatsByTripIdAndClass(tripId, seatClass);
        return seatMapper.toSeatResponseList(seats);
    }

    @Override
    @Transactional
    public SeatResponse selectSeat(Long tripId, String seatNumber) {
        Seat seat = seatRepository.findByTripIdAndSeatNumberAndIsDeleteFalse(tripId, seatNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found"));

        if (seat.getStatus() != Seat.SeatStatus.AVAILABLE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat is not available");
        }

        seat.setStatus(Seat.SeatStatus.OCCUPIED);
        seatRepository.save(seat);

        return seatMapper.toSeatResponse(seat);
    }

    @Override
    @Transactional
    public List<SeatResponse> selectMultipleSeats(SeatSelectionRequest request) {
        List<SeatResponse> selectedSeats = new ArrayList<>();
        
        for (String seatNumber : request.getSeatNumbers()) {
            SeatResponse seatResponse = selectSeat(request.getTripId(), seatNumber);
            selectedSeats.add(seatResponse);
        }
        
        return selectedSeats;
    }

    @Override
    @Transactional
    public SeatResponse releaseSeat(Long tripId, String seatNumber) {
        Seat seat = seatRepository.findByTripIdAndSeatNumberAndIsDeleteFalse(tripId, seatNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found"));

        if (seat.getStatus() == Seat.SeatStatus.AVAILABLE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat is already available");
        }

        seat.setStatus(Seat.SeatStatus.AVAILABLE);
        seatRepository.save(seat);

        return seatMapper.toSeatResponse(seat);
    }

    @Override
    @Transactional
    public List<SeatResponse> releaseMultipleSeats(Long tripId, List<String> seatNumbers) {
        List<SeatResponse> releasedSeats = new ArrayList<>();
        
        for (String seatNumber : seatNumbers) {
            SeatResponse seatResponse = releaseSeat(tripId, seatNumber);
            releasedSeats.add(seatResponse);
        }
        
        return releasedSeats;
    }

    @Override
    @Transactional
    public SeatResponse reserveSeat(Long tripId, String seatNumber) {
        Seat seat = seatRepository.findByTripIdAndSeatNumberAndIsDeleteFalse(tripId, seatNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found"));

        if (seat.getStatus() != Seat.SeatStatus.AVAILABLE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat is not available");
        }

        seat.setStatus(Seat.SeatStatus.RESERVED);
        seatRepository.save(seat);

        return seatMapper.toSeatResponse(seat);
    }

    @Override
    @Transactional
    public List<SeatResponse> reserveMultipleSeats(SeatSelectionRequest request) {
        List<SeatResponse> reservedSeats = new ArrayList<>();
        
        for (String seatNumber : request.getSeatNumbers()) {
            SeatResponse seatResponse = reserveSeat(request.getTripId(), seatNumber);
            reservedSeats.add(seatResponse);
        }
        
        return reservedSeats;
    }
}



