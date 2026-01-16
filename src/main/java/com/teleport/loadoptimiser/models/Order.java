package com.teleport.loadoptimiser.models;

import lombok.Data;

@Data
public class Order {
    public String id;
    public long payout_cents;
    public int weight_lbs;
    public int volume_cuft;
    public String origin;
    public String destination;
    public String pickup_date;
    public String delivery_date;
    public boolean is_hazmat;
}
