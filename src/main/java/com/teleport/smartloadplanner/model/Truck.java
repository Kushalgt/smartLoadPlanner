package com.teleport.smartloadplanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Truck {
    private String id;
    private Long maxWeightLbs;
    private Long maxVolumeCuft;
}
