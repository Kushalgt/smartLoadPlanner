package com.teleport.smartloadplanner.dto;

import com.teleport.smartloadplanner.model.Order;
import com.teleport.smartloadplanner.model.Truck;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadOptimizationRequest {
    @Valid
    @NotNull(message = "Truck details not present")
    Truck truck;
    @Valid
    @NotNull(message = "Orders not present")
    List<Order> orders;
}
