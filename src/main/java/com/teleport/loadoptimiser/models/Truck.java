package com.teleport.loadoptimiser.models;

import lombok.Data;

@Data
public class Truck {
    public String id;
    public int max_weight_lbs;
    public int max_volume_cuft;
}