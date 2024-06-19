package analyzer.domain.alert.application;

import analyzer.domain.alert.dto.AlertData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class AlertSlackService {

    RestClient restClient = RestClient.create();

    public void sendVolatilityAlert(String ticker, double changePercentage, String slackWebhookUrl) {
        restClient.post()
            .uri(slackWebhookUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .body(AlertData.of(ticker, changePercentage))
            .retrieve();
    }
}
