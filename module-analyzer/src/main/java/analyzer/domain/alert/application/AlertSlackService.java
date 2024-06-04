package analyzer.domain.alert.application;

import static analyzer.global.common.Const.SLACK_WEBHOOK_URL_VOLATILITY_ALERT;

import analyzer.domain.alert.dto.AlertData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class AlertSlackService {

    RestClient restClient = RestClient.create();

    public void sendVolatilityAlert(String ticker, double changePercentage) {
        restClient.post()
            .uri(SLACK_WEBHOOK_URL_VOLATILITY_ALERT)
            .contentType(MediaType.APPLICATION_JSON)
            .body(AlertData.of(ticker, changePercentage))
            .retrieve();
    }
}
