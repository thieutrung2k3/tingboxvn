package com.kir.commonservice.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public class TicketEmailRequest {
      private String to;
      private String orderCode;
      private String itemType;
      private String itemReferenceId;
      private String departureLocationCode;
      private String arrivalLocationCode;
      private LocalDateTime departureTime;
      private LocalDateTime arrivalTime;
      private List<TicketPassenger> passengers;

      public TicketEmailRequest() { }

      public TicketEmailRequest(String to, String orderCode, String itemType, String itemReferenceId, String departureLocationCode, String arrivalLocationCode, LocalDateTime departureTime, LocalDateTime arrivalTime, List<TicketPassenger> passengers) {
            this.to = to;
            this.orderCode = orderCode;
            this.itemType = itemType;
            this.itemReferenceId = itemReferenceId;
            this.departureLocationCode = departureLocationCode;
            this.arrivalLocationCode = arrivalLocationCode;
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
            this.passengers = passengers;
      }

      public String getTo() {
            return to;
      }

      public void setTo(String to) {
            this.to = to;
      }

      public String getOrderCode() {
            return orderCode;
      }

      public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
      }

      public String getItemType() {
            return itemType;
      }

      public void setItemType(String itemType) {
            this.itemType = itemType;
      }

      public String getItemReferenceId() {
            return itemReferenceId;
      }

      public void setItemReferenceId(String itemReferenceId) {
            this.itemReferenceId = itemReferenceId;
      }

      public String getDepartureLocationCode() {
            return departureLocationCode;
      }

      public void setDepartureLocationCode(String departureLocationCode) {
            this.departureLocationCode = departureLocationCode;
      }

      public String getArrivalLocationCode() {
            return arrivalLocationCode;
      }

      public void setArrivalLocationCode(String arrivalLocationCode) {
            this.arrivalLocationCode = arrivalLocationCode;
      }

      public LocalDateTime getDepartureTime() {
            return departureTime;
      }

      public void setDepartureTime(LocalDateTime departureTime) {
            this.departureTime = departureTime;
      }

      public LocalDateTime getArrivalTime() {
            return arrivalTime;
      }

      public void setArrivalTime(LocalDateTime arrivalTime) {
            this.arrivalTime = arrivalTime;
      }

      public List<TicketPassenger> getPassengers() {
            return passengers;
      }

      public void setPassengers(List<TicketPassenger> passengers) {
            this.passengers = passengers;
      }

      public static class Builder {
            private final TicketEmailRequest ins = new TicketEmailRequest();
            public Builder to(String to) {
                  ins.to = to;
                  return this;
            }
            public Builder orderCode(String orderCode) {
                  ins.orderCode = orderCode;
                  return this;
            }
            public Builder itemType(String itemType) {
                  ins.itemType = itemType;
                  return this;
            }

            public Builder itemReferenceId(String itemReferenceId) {
                  ins.itemReferenceId = itemReferenceId;
                  return this;
            }
            public Builder departureLocationCode(String departureLocationCode) {
                  ins.departureLocationCode = departureLocationCode;
                  return this;
            }

            public Builder arrivalLocationCode(String arrivalLocationCode) {
                  ins.arrivalLocationCode = arrivalLocationCode;
                  return this;
            }
            public Builder departureTime(LocalDateTime departureTime) {
                  ins.departureTime = departureTime;
                  return this;
            }

            public Builder arrivalTime(LocalDateTime arrivalTime) {
                  ins.arrivalTime = arrivalTime;
                  return this;
            }
            public Builder passengers(List<TicketPassenger> passengers) {
                  ins.passengers = passengers;
                  return this;
            }

            public TicketEmailRequest build() {
                  return ins;
            }
      }
      public static Builder builder() {
            return new Builder();
      }

      public static class TicketPassenger {
            private String firstName;
            private String lastName;
            private String identityDocumentNumber;
            private String seatNumber;

            public TicketPassenger(String firstName, String lastName, String identityDocumentNumber, String seatNumber) {
                  this.firstName = firstName;
                  this.lastName = lastName;
                  this.identityDocumentNumber = identityDocumentNumber;
                  this.seatNumber = seatNumber;
            }

            public TicketPassenger() { }

            public String getFirstName() {
                  return firstName;
            }

            public void setFirstName(String firstName) {
                  this.firstName = firstName;
            }

            public String getLastName() {
                  return lastName;
            }

            public void setLastName(String lastName) {
                  this.lastName = lastName;
            }

            public String getIdentityDocumentNumber() {
                  return identityDocumentNumber;
            }

            public void setIdentityDocumentNumber(String identityDocumentNumber) {
                  this.identityDocumentNumber = identityDocumentNumber;
            }

            public String getSeatNumber() {
                  return seatNumber;
            }

            public void setSeatNumber(String seatNumber) {
                  this.seatNumber = seatNumber;
            }

            public static class Builder {
                  private final TicketPassenger ins = new TicketPassenger();

                  public Builder firstName(String firstName) {
                        ins.firstName = firstName;
                        return this;
                  }
                  public Builder lastName(String lastName) {
                        ins.lastName = lastName;
                        return this;
                  }
                  public Builder identityDocumentNumber(String identityDocumentNumber) {
                        ins.identityDocumentNumber = identityDocumentNumber;
                        return this;
                  }
                  public Builder seatNumber(String seatNumber) {
                        ins.seatNumber = seatNumber;
                        return this;
                  }
                  public TicketPassenger build() {
                        return ins;
                  }
            }

            public static Builder builder() {
                  return new Builder();
            }
      }
}
