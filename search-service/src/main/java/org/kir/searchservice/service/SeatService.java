package org.kir.searchservice.service;

import org.kir.searchservice.dto.request.SeatSelectionRequest;
import org.kir.searchservice.dto.response.SeatMapResponse;
import org.kir.searchservice.dto.response.SeatResponse;

import java.util.List;

public interface SeatService {
    SeatMapResponse getSeatMapByTripId(Long tripId);

    List<SeatResponse> getAvailableSeatsByTripId(Long tripId);

    List<SeatResponse> getAvailableSeatsByTripIdAndClass(Long tripId, String seatClass);

    SeatResponse selectSeat(Long tripId, String seatNumber);

    List<SeatResponse> selectMultipleSeats(SeatSelectionRequest request);

    SeatResponse releaseSeat(Long tripId, String seatNumber);

    List<SeatResponse> releaseMultipleSeats(Long tripId, List<String> seatNumbers);

    SeatResponse reserveSeat(Long tripId, String seatNumber);

    List<SeatResponse> reserveMultipleSeats(SeatSelectionRequest request);
}
