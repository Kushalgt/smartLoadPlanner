package com.teleport.smartloadplanner.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class LoadOptimizationResponse implements Comparable<LoadOptimizationResponse> {
    private String truckId;
    private List<String> selectedOrderIds;
    private long totalPayoutCents;
    private long totalWeightLbs;
    private long totalVolumeCuft;
    private double utilizationWeightPercent;
    private double utilizationVolumePercent;

    public static LoadOptimizationResponse of(String truckId) {
        return LoadOptimizationResponse.builder()
                .truckId(truckId)
                .totalPayoutCents(0L)
                .totalWeightLbs(0L)
                .totalVolumeCuft(0L)
                .selectedOrderIds(new ArrayList<>())
                .build();
    }

    @Override
    public int compareTo(LoadOptimizationResponse other) {
        // 1. Primary: Maximize Revenue
        if (other == null) {
            return -1;
        }
        if (this.totalPayoutCents != other.totalPayoutCents) {
            return Long.compare(this.totalPayoutCents, other.totalPayoutCents);
        }
        // 2. Secondary (Tie-breaker): Maximize Weight Utilization
        if (Double.compare(this.utilizationWeightPercent, other.utilizationWeightPercent) != 0) {
            return Double.compare(this.utilizationWeightPercent, other.utilizationWeightPercent);
        }

        // 3. Tertiary: Maximize Volume Utilization
        return Double.compare(this.utilizationVolumePercent, other.utilizationVolumePercent);
    }
}
