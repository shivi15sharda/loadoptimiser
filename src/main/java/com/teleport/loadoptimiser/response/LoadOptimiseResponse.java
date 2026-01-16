package com.teleport.loadoptimiser.response;

import java.util.List;

public class LoadOptimiseResponse {
    public String truck_id;
    public List<String> selected_order_ids;
    public long total_payout_cents;
    public int total_weight_lbs;
    public int total_volume_cuft;
    public double utilization_weight_percent;
    public double utilization_volume_percent;
}
