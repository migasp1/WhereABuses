package com.example.whereabuses;

import java.util.ArrayList;

public class Bus {
    private int id;
    private int carreira;

    public Bus(int id, int carreira){
        this.id=id;
        this.carreira=carreira;

    }
    public int getId() {
        return id;
    }

    public int getCarreira() {
        return carreira;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCarreira(int carreira) {
        this.carreira = carreira;
    }

    public static ArrayList<Bus> createBuses(int n){
        ArrayList<Bus> buses = new ArrayList<Bus>();
        for (int i = 1; i <= n; i++) {
            buses.add(new Bus(i, 730+i));
        }

        return buses;
    }

}
