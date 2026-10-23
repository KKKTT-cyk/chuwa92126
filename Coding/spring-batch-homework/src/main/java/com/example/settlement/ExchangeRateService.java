package com.example.settlement;

import java.math.BigDecimal;

public interface ExchangeRateService {
    boolean supports(String currency);
    BigDecimal toUsd(BigDecimal amount, String currency);
}
