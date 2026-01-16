package com.teleport.loadoptimiser.service;

import com.teleport.loadoptimiser.models.Order;
import com.teleport.loadoptimiser.models.Truck;
import com.teleport.loadoptimiser.response.LoadOptimiseResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ILoadOptimiserService {

    public LoadOptimiseResponse optimiseLoad(Truck truck, List<Order> orders);
}
