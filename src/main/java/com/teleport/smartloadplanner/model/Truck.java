package com.teleport.smartloadplanner.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Truck {
    @NotNull(message = "Truck Id can't be null")
    private String id;
    @NotNull(message = "Truck's max weight not provided")
    private Long maxWeightLbs;
    @NotNull(message = "Truck's max volume not provided")
    private Long maxVolumeCuft;
}
