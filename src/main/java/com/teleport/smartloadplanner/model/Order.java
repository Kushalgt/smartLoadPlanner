package com.teleport.smartloadplanner.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @NotNull(message = "ID required")
    private String id;
    @NotNull(message = "Payout cents required")
    private Long payoutCents;
    @NotNull(message = "weight required")
    private Long weightLbs;
    @NotNull(message = "volume required")
    private Long volumeCuft;
    @NotNull(message = "origin required")
    private String origin;
    @NotNull(message = "Destination required")
    private String destination;
    @NotNull(message = "Pickup date required")
    private Date pickupDate;
    @NotNull(message = "Delivery date required")
    private Date deliveryDate;
    @NotNull(message = "is hazmat not provided")
    private Boolean isHazmat;
}
