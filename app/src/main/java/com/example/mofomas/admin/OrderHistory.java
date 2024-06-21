package com.example.mofomas.admin;

import java.util.ArrayList;
import java.util.List;

public class OrderHistory {

    private static OrderHistory instance;
    private List<Order> orderHistoryList;

    private OrderHistory() {
        orderHistoryList = new ArrayList<>();
    }

    public static synchronized OrderHistory getInstance() {
        if (instance == null) {
            instance = new OrderHistory();
        }
        return instance;
    }

    public void addOrder(Order order) {
        orderHistoryList.add(order);
    }

    public List<Order> getOrderHistoryList() {
        return orderHistoryList;
    }
}
