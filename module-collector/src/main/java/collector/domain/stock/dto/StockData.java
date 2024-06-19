package collector.domain.stock.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StockData(
    @JsonProperty("market")
    String ticker,

    @JsonProperty("trade_price")
    double price
) {

}
