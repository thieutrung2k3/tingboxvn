package org.kir.searchservice.mapper;

import org.kir.searchservice.dto.response.SeatResponse;
import org.kir.searchservice.entity.Seat;
import org.kir.searchservice.entity.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    
    @Mapping(target = "tripId", source = "trip.id")
    @Mapping(target = "totalPrice", expression = "java(calculateTotalPrice(seat, seat.getTrip()))")
    SeatResponse toSeatResponse(Seat seat);
    
    List<SeatResponse> toSeatResponseList(List<Seat> seats);
    
    default BigDecimal calculateTotalPrice(Seat seat, Trip trip) {
        if (trip == null || trip.getBasePrice() == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal basePrice = trip.getBasePrice();
        Double modifier = seat.getPriceModifier() != null ? seat.getPriceModifier() : 0.0;
        BigDecimal modifierAmount = basePrice.multiply(BigDecimal.valueOf(modifier));
        
        return basePrice.add(modifierAmount);
    }
}

