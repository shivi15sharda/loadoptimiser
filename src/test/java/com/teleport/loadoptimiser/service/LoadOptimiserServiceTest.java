package com.teleport.loadoptimiser.service;

import com.teleport.loadoptimiser.models.Order;
import com.teleport.loadoptimiser.models.Truck;
import com.teleport.loadoptimiser.response.LoadOptimiseResponse;
import com.teleport.loadoptimiser.service.implementation.LoadOptimiserServiceImpl;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LoadOptimizerServiceTest {

    private final LoadOptimiserServiceImpl service = new LoadOptimiserServiceImpl();

    private Truck buildTruck() {
        Truck t = new Truck();
        t.id = "truck-123";
        t.max_weight_lbs = 44000;
        t.max_volume_cuft = 3000;
        return t;
    }

    private Order order(String id, long payout, int weight, int volume, boolean hazmat) {
        Order o = new Order();
        o.id = id;
        o.payout_cents = payout;
        o.weight_lbs = weight;
        o.volume_cuft = volume;
        o.origin = "Los Angeles, CA";
        o.destination = "Dallas, TX";
        o.pickup_date = "2025-12-05";
        o.delivery_date = "2025-12-09";
        o.is_hazmat = hazmat;
        return o;
    }

    @Test
    void shouldPickBestCombination() {
        Truck truck = buildTruck();

        Order o1 = order("ord-001", 250_000, 18000, 1200, false);
        Order o2 = order("ord-002", 180_000, 12000, 900, false);
        Order o3 = order("ord-003", 320_000, 30000, 1800, false);

        LoadOptimiseResponse res = service.optimiseLoad(truck, List.of(o1, o2, o3));

        assertEquals(500_000, res.total_payout_cents);
        assertEquals(42000, res.total_weight_lbs);
        assertEquals(2700, res.total_volume_cuft);
        assertTrue(res.selected_order_ids.contains("ord-002"));
        assertEquals(2, res.selected_order_ids.size());
    }

    @Test
    void shouldRespectWeightLimit() {
        Truck truck = buildTruck();

        Order heavy = order("heavy", 500_000, 50000, 1000, false);

        LoadOptimiseResponse res = service.optimiseLoad(truck, List.of(heavy));

        assertEquals(0, res.total_payout_cents);
        assertTrue(res.selected_order_ids.isEmpty());
    }

    @Test
    void shouldRespectVolumeLimit() {
        Truck truck = buildTruck();

        Order big = order("big", 200_000, 10000, 4000, false);

        LoadOptimiseResponse res = service.optimiseLoad(truck, List.of(big));

        assertEquals(0, res.total_payout_cents);
        assertTrue(res.selected_order_ids.isEmpty());
    }

    @Test
    void shouldIgnoreHazmatOrders() {
        Truck truck = buildTruck();

        Order hazmat = order("haz", 999_999, 1000, 1000, true);
        Order normal = order("ok", 100_000, 1000, 1000, false);

        LoadOptimiseResponse res = service.optimiseLoad(truck, List.of(hazmat, normal));

        assertEquals(100_000, res.total_payout_cents);
        assertEquals(List.of("ok"), res.selected_order_ids);
    }

    @Test
    void shouldReturnEmptyIfNoOrders() {
        Truck truck = buildTruck();

        LoadOptimiseResponse res = service.optimiseLoad(truck, List.of());

        assertEquals(0, res.total_payout_cents);
        assertEquals(0, res.total_weight_lbs);
        assertEquals(0, res.total_volume_cuft);
        assertTrue(res.selected_order_ids.isEmpty());
    }

    @Test
    void shouldPickHighestPayoutWithinLimits() {
        Truck truck = buildTruck();

        Order o1 = order("A", 100_000, 10000, 500, false);
        Order o2 = order("B", 200_000, 20000, 1000, false);
        Order o3 = order("C", 150_000, 15000, 1000, false);

        LoadOptimiseResponse res = service.optimiseLoad(truck, List.of(o1, o2, o3));

        // Best = B + A
        assertEquals(350_000, res.total_payout_cents);
        assertTrue(res.selected_order_ids.contains("B"));
    }
}
