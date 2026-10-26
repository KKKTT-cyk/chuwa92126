package org.tiff.homework11.question11;

import java.math.BigDecimal;

public interface ExchangeRateService {
    boolean supports(String currency);

    BigDecimal toUsd(BigDecimal amount, String currency);
}
