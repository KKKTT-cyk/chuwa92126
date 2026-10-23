package com.example.settlement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(long id, String accountId, BigDecimal amount,
                          String currency, LocalDateTime timestamp) { }
