package com.example.settlement;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SettlementLine(long transactionId, String accountId,
                             BigDecimal amountUsd, LocalDate settleDate) { }
