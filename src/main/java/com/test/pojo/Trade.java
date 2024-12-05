package com.test.pojo;

import java.time.LocalDateTime;

public record Trade(long id,double price,double volume,Side side,LocalDateTime timestamp) {

}
