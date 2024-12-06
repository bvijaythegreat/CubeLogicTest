package com.test.marketplace;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.test.marketplace.impl.SuspicionDetectorImpl;
import com.test.pojo.Order;
import com.test.pojo.Side;
import com.test.pojo.Trade;

public class SuspicionDetectorTest {

    @Test
    public void testFindSuspicious() {
    	LocalDateTime parsedDateTime = LocalDateTime.parse("2024-12-05T14:40");
        Trade trade1 = new Trade(1, 100, 10, Side.BUY, parsedDateTime);
        Trade trade2 = new Trade(2, 150, 5, Side.SELL, parsedDateTime);

        Order order1 = new Order(1, 109, 10, Side.SELL, parsedDateTime.minusMinutes(10));
        Order order2 = new Order(2, 140, 5, Side.BUY, parsedDateTime.minusMinutes(30));

        List<Trade> trades = Arrays.asList(trade1, trade2);
        List<Order> orders = Arrays.asList(order1, order2);

        SuspicionDetector detector = new SuspicionDetectorImpl();
        List<Object> suspicious = detector.findSuspicious(trades, orders);
       
        // Expected: trade1 and order2 should be suspicious
        assertEquals(2, suspicious.size(), "There should be two suspicious items.");
        assertEquals(trade1, suspicious.get(0), "Trade 1 should be suspicious.");
        assertEquals(order2, suspicious.get(1), "Order 1 should be suspicious.");
    }

    @Test
    public void testNoSuspiciousTradesOrOrders() {
        Trade trade = new Trade(3, 200, 15, Side.BUY, LocalDateTime.now().minusHours(1));
        Order order = new Order(3, 250, 15, Side.SELL, LocalDateTime.now().minusHours(2));

        List<Trade> trades = Arrays.asList(trade);
        List<Order> orders = Arrays.asList(order);

        SuspicionDetector detector = new SuspicionDetectorImpl();
        List<Object> suspicious = detector.findSuspicious(trades, orders);

        // Expected: No suspicious trades or orders
        assertEquals(0, suspicious.size(), "There should be no suspicious items.");
    }

    @Test
    public void testEdgeCaseExact30MinutesBefore() {
    	Order order = new Order(4, 270, 20, Side.BUY, LocalDateTime.now().minusMinutes(30));
        Trade trade = new Trade(4, 300, 20, Side.SELL, LocalDateTime.now());
        

        List<Trade> trades = Arrays.asList(trade);
        List<Order> orders = Arrays.asList(order);

        SuspicionDetector detector = new SuspicionDetectorImpl();
        List<Object> suspicious = detector.findSuspicious(trades, orders);

        // Expected: Order should be suspicious because it is exactly 30 minutes before the trade
        assertEquals(1, suspicious.size(), "There should be one suspicious item.");
        assertEquals(order, suspicious.get(0), "Order should be suspicious.");
    }

    @Test
    public void testPriceWithGreater10PercentBuySide() {
    	Trade trade = new Trade(5, 100, 10, Side.BUY, LocalDateTime.now());
    	Order order = new Order(5, 110, 10, Side.SELL, LocalDateTime.now().minusMinutes(40));
        
        List<Trade> trades = Arrays.asList(trade);
        List<Order> orders = Arrays.asList(order);

        SuspicionDetector detector = new SuspicionDetectorImpl();
        List<Object> suspicious = detector.findSuspicious(trades, orders);

        // Expected: Order should be suspicious because its price is exactly 10% higher than the trade price with Buy
        assertEquals(1, suspicious.size(), "There should be one suspicious item.");
        assertEquals(order, suspicious.get(0), "Order should be suspicious."+suspicious.get(0).toString());
    }
    
    @Test
    public void testPriceWithLess10PercentSellSide() {
    	Trade trade = new Trade(5, 100, 10, Side.SELL, LocalDateTime.now());
    	Order order = new Order(5, 90, 10, Side.BUY, LocalDateTime.now().minusMinutes(40));
        
        List<Trade> trades = Arrays.asList(trade);
        List<Order> orders = Arrays.asList(order);

        SuspicionDetector detector = new SuspicionDetectorImpl();
        List<Object> suspicious = detector.findSuspicious(trades, orders);

        // Expected: Order should be suspicious because its price is exactly 10% lower than the trade price with SELL
        assertEquals(1, suspicious.size(), "There should be one suspicious item.");
        assertEquals(order, suspicious.get(0), "Order should be suspicious."+suspicious.get(0).toString());
    }

}
