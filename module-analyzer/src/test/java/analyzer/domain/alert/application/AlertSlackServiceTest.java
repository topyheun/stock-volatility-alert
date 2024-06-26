package analyzer.domain.alert.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import analyzer.domain.alert.dto.AlertData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestBodySpec;
import org.springframework.web.client.RestClient.RequestBodyUriSpec;
import org.springframework.web.client.RestClient.ResponseSpec;

@ExtendWith(MockitoExtension.class)
class AlertSlackServiceTest {

    @Mock
    RestClient restClient;

    @Mock
    RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    RequestBodySpec requestBodySpec;

    @Mock
    ResponseSpec responseSpec;

    @InjectMocks
    AlertSlackService sut; // System Under Test

    @Captor
    ArgumentCaptor<AlertData> alertDataCaptor;

    @Test
    @DisplayName("데이터를 받았을 때 Slack에 변동성 알림 메시지를 전송한다")
    void should_sendVolatilityAlert_when_dataIsReceived() {
        // Arrange
        String slackWebhookUrl = "https://hooks.slack.com/services/TEST";
        String ticker = "KRW-BTC";
        double changePercentage = 5.0;

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any(MediaType.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(AlertData.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);

        // Act
        sut.sendVolatilityAlert(ticker, changePercentage, slackWebhookUrl);

        // Assert
        verify(requestBodyUriSpec).uri(eq(slackWebhookUrl));
        verify(requestBodySpec).contentType(eq(MediaType.APPLICATION_JSON));
        verify(requestBodySpec).body(alertDataCaptor.capture());
        verify(requestBodySpec).retrieve();

        AlertData actual = alertDataCaptor.getValue();
        AlertData expected = AlertData.of(ticker, changePercentage);

        assertEquals(expected.text(), actual.text());
    }
}