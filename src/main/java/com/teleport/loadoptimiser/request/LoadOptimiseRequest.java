package com.teleport.loadoptimiser.request;

import com.teleport.loadoptimiser.models.Order;
import com.teleport.loadoptimiser.models.Truck;

import java.util.List;

public class LoadOptimiseRequest {
    public Truck truck;
    public List<Order> orders;
}
