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
    private String id;
    private Long payoutCents;
    private Long weightLbs;
    private Long volumeCuft;
    private String origin;
    private String destination;
    @NotNull(message = "Pickup date required")
    private Date pickupDate;
    @NotNull(message = "Delivery date required")
    private Date deliveryDate;
    @NotNull(message = "is_hazmat not provided")
    private Boolean isHazmat;
}
