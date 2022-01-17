package com.epam.brest.dao;

import java.io.Serializable;

public class RandomObject implements Serializable {

    public double[] data;

    public RandomObject() {
        data = new double[10];
        for (int i = 0; i < data.length; i++) {
            data[i] = Math.random();
            System.out.println(data[i]);
        }
    }
}
