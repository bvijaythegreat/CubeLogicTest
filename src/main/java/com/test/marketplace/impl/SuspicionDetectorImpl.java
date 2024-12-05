package com.test.marketplace.impl;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.marketplace.SuspicionDetector;
import com.test.pojo.Order;
import com.test.pojo.Side;
import com.test.pojo.Trade;

/**
 * Implementation of the SuspicionDetector interface.
 * Detects suspicious trades and orders based on specified criteria.
 */
public class SuspicionDetectorImpl implements SuspicionDetector {

	private static final Logger logger = LoggerFactory.getLogger(SuspicionDetectorImpl.class);
    /**
     * Finds suspicious trades and orders by analyzing the given lists.
     * Trades are suspicious if there are opposite-side orders within 30 minutes before the trade.
     * Orders are suspicious if their price is within 10% of the trade price.
     *
     * @param trades the list of trades to analyze
     * @param orders the list of orders to analyze
     * @return a list of suspicious trades and orders
     */
    @Override
    public List<Object> findSuspicious(List<Trade> trades, List<Order> orders) {
       logger.info("Analyzing trades and orders for suspicious activity.");
        List<Object> suspiciousList = new ArrayList<>();

        for (Trade trade : trades) {
            logger.debug("Analyzing Trade ID1: " + trade.toString());
            LocalDateTime tradeTime = trade.timestamp();
            double tradePrice = trade.price();
            Side tradeSide = trade.side();

            for (Order order : orders) {
              logger.debug("Analyzing Order ID1: " + order.toString());
                
                LocalDateTime orderTime = order.timestamp();
                double orderPrice = order.price();
                Side orderSide = order.side();

                // Check if the order is within 30 minutes before the trade and Check for opposite side orders
                if (orderTime.isAfter(tradeTime.minusMinutes(30)) && orderTime.isBefore(tradeTime) && !tradeSide.equals(orderSide)) {
                        suspiciousList.add(trade);
                        break;
                }

                // Check if the order price is within 10% of the trade price
                if (tradeSide == Side.BUY && orderSide == Side.SELL && orderPrice <= tradePrice * 1.1) {
                	logger.debug("Order is suspicious:"+order.toString());
                    suspiciousList.add(order);
                } else if (tradeSide == Side.SELL && orderSide == Side.BUY && orderPrice >= tradePrice * 0.9) {
                	logger.debug("order is suspicious:"+order.toString());
                    suspiciousList.add(order);
                }
            }
        }

        logger.info("Suspicious items found: " + suspiciousList.size());
        return suspiciousList;
    }
    
    /**
     * Finds suspicious trades and orders using streams and lambda expressions.
     * Trades are suspicious if there are opposite-side orders within 30 minutes before the trade.
     * Orders are suspicious if their price is within 10% of the trade price.
     *
     * @param trades the list of trades to analyze
     * @param orders the list of orders to analyze
     * @return a list of suspicious trades and orders
     */
    public List<Object> findSuspiciousWithStreams(List<Trade> trades, List<Order> orders) {
        logger.info("Analyzing trades and orders for suspicious activity using streams.");

        List<Object> suspiciousTrades = trades.stream()
            .filter(trade -> {
                logger.debug("Analyzing Trade ID: " + trade.id());
                return orders.stream()
                    .anyMatch(order -> {
                        logger.debug("Analyzing Order ID: " + order.id());
                        boolean isSuspicious = order.timestamp().isAfter(trade.timestamp().minusMinutes(30)) &&
                                              order.timestamp().isBefore(trade.timestamp()) &&
                                              trade.side() != order.side();
                        if (isSuspicious) {
                            logger.debug("Trade ID: " + trade.id() + " is suspicious due to Order ID: " + order.id());
                        }
                        return isSuspicious;
                    });
            })
            .collect(Collectors.toList());

        List<Object> suspiciousOrders = orders.stream()
            .filter(order -> {
                return trades.stream()
                    .anyMatch(trade -> {
                        boolean isSuspicious = (trade.side() == Side.BUY && order.side() == Side.SELL && order.price() <= trade.price() * 1.1) ||
                                              (trade.side() == Side.SELL && order.side() == Side.BUY && order.price() >= trade.price() * 0.9);
                        if (isSuspicious) {
                            logger.debug("Order ID: " + order.id() + " is suspicious due to Trade ID: " + trade.id());
                        }
                        return isSuspicious;
                    });
            })
            .collect(Collectors.toList());

        List<Object> suspiciousList = new ArrayList<>();
        suspiciousList.addAll(suspiciousTrades);
        suspiciousList.addAll(suspiciousOrders);

        logger.info("Suspicious items found: " + suspiciousList.size());
        return suspiciousList;
    }
}
