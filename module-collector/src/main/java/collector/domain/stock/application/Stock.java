package collector.domain.stock.application;

import lombok.Getter;

@Getter
public enum Stock {
    KRW_BTC("KRW-BTC"),
    KRW_ETH("KRW-ETH"),
    KRW_XRP("KRW-XRP"),
    KRW_DOGE("KRW-DOGE");

    private final String ticker;

    Stock(String ticker) {
        this.ticker = ticker;
    }
}
