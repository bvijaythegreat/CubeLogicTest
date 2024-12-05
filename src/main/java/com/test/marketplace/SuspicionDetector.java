package com.test.marketplace;

import java.util.List;

import com.test.pojo.Order;
import com.test.pojo.Trade;

/**
 * Interface for detecting suspicious trades and orders.
 */
public interface SuspicionDetector {
    /**
     * Identifies suspicious trades and orders from the given lists.
     *
     * @param trades the list of trades to analyze
     * @param orders the list of orders to analyze
     * @return a list of suspicious trades and orders
     */
    List<Object> findSuspicious(List<Trade> trades, List<Order> orders);
}

