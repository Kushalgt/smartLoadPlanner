package com.teleport.smartloadplanner.dto;

import com.teleport.smartloadplanner.model.Order;
import com.teleport.smartloadplanner.model.Truck;
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
    Truck truck;
    List<Order> orders;
}
