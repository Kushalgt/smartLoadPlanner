package com.teleport.smartloadplanner.service;

import com.teleport.smartloadplanner.dto.LoadOptimizationResponse;
import com.teleport.smartloadplanner.model.Order;
import com.teleport.smartloadplanner.model.Truck;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SmartLoaderService {

    public LoadOptimizationResponse optimizeLoad(Truck truck, List<Order> orders) {
        Map<String, List<Order>> compatibleOrders = new HashMap<>();
        LoadOptimizationResponse finalResponse = LoadOptimizationResponse.of(truck.getId());
        for (Order order : orders) {
            String compatibilityCriteria = order.getOrigin() + order.getDestination() + order.isHazmat();
            compatibleOrders.computeIfAbsent(compatibilityCriteria, key -> new ArrayList<>()).add(order);
        }
        for (List<Order> orderList : compatibleOrders.values()) {
            LoadOptimizationResponse response = optimizeLoadForCompatibleOrders(truck, orderList);

            if (finalResponse != null && finalResponse.compareTo(response) < 0) {
                finalResponse = response;
            }
        }
        return finalResponse;
    }

    private LoadOptimizationResponse optimizeLoadForCompatibleOrders(Truck truck, List<Order> orders) {
        // 1. Thread-safe wrapper to track the best solution across recursive calls
        LoadOptimizationResponse[] bestResponseWrapper = new LoadOptimizationResponse[1];
        bestResponseWrapper[0] = LoadOptimizationResponse.of(truck.getId());

        // 2. The working state that will be modified and reverted during backtracking
        LoadOptimizationResponse currentResponse = LoadOptimizationResponse.of(truck.getId());

        // 3. Start the backtracking at index 0.
        // Passing null for dates is cleaner than Instant.MIN/MAX if your canOrderBeTaken handles nulls.
        backtrack(orders, 0, truck, currentResponse, null, null, bestResponseWrapper);

        // 4. Calculate final utilization percentages only once at the end
        LoadOptimizationResponse finalBest = bestResponseWrapper[0];
        finalBest.setUtilizationWeightPercent(
                Math.round(((double) finalBest.getTotalWeightLbs() / truck.getMaxWeightLbs()) * 10000.0) / 100.0
        );
        finalBest.setUtilizationVolumePercent(
                Math.round(((double) finalBest.getTotalVolumeCuft() / truck.getMaxVolumeCuft()) * 10000.0) / 100.0
        );

        return finalBest;
    }


    private boolean canOrderBeTaken(LoadOptimizationResponse response, Order order, Truck truck, Date currentMaxPickup, Date currentMinDelivery) {

        // 1. Weight Constraint
        // The current total weight plus the new order's weight must not exceed the truck's capacity
        if (response.getTotalWeightLbs() + order.getWeightLbs() > truck.getMaxWeightLbs()) {
            return false;
        }

        // 2. Volume Constraint
        // The current total volume plus the new order's volume must not exceed the truck's capacity
        if (response.getTotalVolumeCuft() + order.getVolumeCuft() > truck.getMaxVolumeCuft()) {
            return false;
        }

        // 3. Date Constraint (Overlapping Window Check)
        // Determine what the new max pickup date would be if we added this order
        Date newMaxPickup = order.getPickupDate();
        if (currentMaxPickup != null && currentMaxPickup.after(newMaxPickup)) {
            newMaxPickup = currentMaxPickup;
        }

        // Determine what the new min delivery date would be if we added this order
        Date newMinDelivery = order.getDeliveryDate();
        if (currentMinDelivery != null && currentMinDelivery.before(newMinDelivery)) {
            newMinDelivery = currentMinDelivery;
        }

        // The feasibility rule: The latest pickup must happen on or before the earliest delivery.
        // If newMaxPickup > newMinDelivery, the schedule conflicts.
        return !newMaxPickup.after(newMinDelivery);

        // If it passed all the pruning checks, the order can be taken!
    }

    private LoadOptimizationResponse cloneResponse(LoadOptimizationResponse source) {
        return LoadOptimizationResponse.builder()
                .truckId(source.getTruckId())
                .selectedOrderIds(new ArrayList<>(source.getSelectedOrderIds())) // Create a new List instance
                .totalPayoutCents(source.getTotalPayoutCents())
                .totalWeightLbs(source.getTotalWeightLbs())
                .totalVolumeCuft(source.getTotalVolumeCuft())
                .build();
    }

    private void backtrack(List<Order> orders, int index, Truck truck,
                           LoadOptimizationResponse currentResponse,
                           Date currentMaxPickup, Date currentMinDelivery,
                           LoadOptimizationResponse[] bestResponseWrapper) {

        // BASE CASE: If the current payout is better than our global best, update the global best.
        // We must DEEP COPY the currentResponse because we are about to modify it in the next steps.
        if (currentResponse.getTotalPayoutCents() > bestResponseWrapper[0].getTotalPayoutCents()) {
            bestResponseWrapper[0] = cloneResponse(currentResponse);
        }

        // BASE CASE: Reached the end of the order list
        if (index == orders.size()) {
            return;
        }

        Order order = orders.get(index);

        // ==========================================
        // BRANCH 1: SKIP THIS ORDER
        // ==========================================
        // We simply move to the next index without modifying currentResponse or the dates.
        backtrack(orders, index + 1, truck, currentResponse, currentMaxPickup, currentMinDelivery, bestResponseWrapper);

        // ==========================================
        // BRANCH 2: TAKE THIS ORDER (If valid)
        // ==========================================
        if (canOrderBeTaken(currentResponse, order, truck, currentMaxPickup, currentMinDelivery)) {

            // 1. Calculate new date bounds.
            // We calculate these into NEW variables so we don't have to "undo" them later.
            Date newMaxPickup = (currentMaxPickup == null || order.getPickupDate().after(currentMaxPickup))
                    ? order.getPickupDate() : currentMaxPickup;
            Date newMinDelivery = (currentMinDelivery == null || order.getDeliveryDate().before(currentMinDelivery))
                    ? order.getDeliveryDate() : currentMinDelivery;

            // 2. DO (State Modification)
            currentResponse.getSelectedOrderIds().add(order.getId());
            currentResponse.setTotalPayoutCents(currentResponse.getTotalPayoutCents() + order.getPayoutCents());
            currentResponse.setTotalWeightLbs(currentResponse.getTotalWeightLbs() + order.getWeightLbs());
            currentResponse.setTotalVolumeCuft(currentResponse.getTotalVolumeCuft() + order.getVolumeCuft());

            // 3. RECURSE (Move to next index with updated state)
            backtrack(orders, index + 1, truck, currentResponse, newMaxPickup, newMinDelivery, bestResponseWrapper);

            // 4. UNDO (Backtrack step: Revert state modification to allow Branch 1 to run correctly as we bubble up)
            currentResponse.getSelectedOrderIds().remove(currentResponse.getSelectedOrderIds().size() - 1);
            currentResponse.setTotalPayoutCents(currentResponse.getTotalPayoutCents() - order.getPayoutCents());
            currentResponse.setTotalWeightLbs(currentResponse.getTotalWeightLbs() - order.getWeightLbs());
            currentResponse.setTotalVolumeCuft(currentResponse.getTotalVolumeCuft() - order.getVolumeCuft());
        }
    }
}
