package com.teleport.loadoptimiser.service.implementation;

import com.teleport.loadoptimiser.models.Order;
import com.teleport.loadoptimiser.models.Truck;
import com.teleport.loadoptimiser.response.LoadOptimiseResponse;
import com.teleport.loadoptimiser.service.ILoadOptimiserService;

import java.util.ArrayList;
import java.util.List;

public class LoadOptimiserServiceImpl implements ILoadOptimiserService {

    public LoadOptimiseResponse optimiseLoad(Truck truck, List<Order> orders) {

        if (orders == null || orders.isEmpty()) {
            return emptyResult(truck);
        }

        // filter compatible orders (same lane, no hazmat)
        String origin = orders.get(0).origin;
        String destination = orders.get(0).destination;

        List<Order> compatible = new ArrayList<>();
        for (Order o : orders) {
            if (o.origin.equals(origin) &&
                    o.destination.equals(destination) &&
                    !o.is_hazmat) {
                compatible.add(o);
            }
        }

        int n = compatible.size();
        long bestPayout = 0;
        int bestWeight = 0, bestVolume = 0;
        List<String> bestSet = new ArrayList<>();

        // bitmask brute force
        for (int mask = 0; mask < (1 << n); mask++) {
            long payout = 0;
            int weight = 0, volume = 0;
            List<String> picked = new ArrayList<>();

            boolean valid = true;

            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    Order o = compatible.get(i);
                    payout += o.payout_cents;
                    weight += o.weight_lbs;
                    volume += o.volume_cuft;
                    picked.add(o.id);

                    if (weight > truck.max_weight_lbs || volume > truck.max_volume_cuft) {
                        valid = false;
                        break;
                    }
                }
            }

            if (valid && payout > bestPayout) {
                bestPayout = payout;
                bestWeight = weight;
                bestVolume = volume;
                bestSet = picked;
            }
        }

        LoadOptimiseResponse res = new LoadOptimiseResponse();
        res.truck_id = truck.id;
        res.selected_order_ids = bestSet;
        res.total_payout_cents = bestPayout;
        res.total_weight_lbs = bestWeight;
        res.total_volume_cuft = bestVolume;
        res.utilization_weight_percent = (bestWeight * 100.0) / truck.max_weight_lbs;
        res.utilization_volume_percent = (bestVolume * 100.0) / truck.max_volume_cuft;
        return res;
    }

    private LoadOptimiseResponse emptyResult(Truck truck) {
        LoadOptimiseResponse res = new LoadOptimiseResponse();
        res.truck_id = truck.id;
        res.selected_order_ids = List.of();
        res.total_payout_cents = 0;
        res.total_weight_lbs = 0;
        res.total_volume_cuft = 0;
        res.utilization_weight_percent = 0;
        res.utilization_volume_percent = 0;
        return res;
    }
}
