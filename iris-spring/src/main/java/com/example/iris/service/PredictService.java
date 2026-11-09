package com.example.iris.service;

import java.net.http.HttpClient;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.example.iris.dto.PredictRequest;
import com.example.iris.dto.PredictResponse;
import com.example.iris.exception.ModelServerException;

/** FastAPI 모델 서버와의 통신을 담당한다. */
@Service
public class PredictService {

    private final RestClient client;

    public PredictService(@Value("${model.server.url}") String modelServerUrl,
                          @Value("${model.server.token}") String token,
                          @Value("${model.server.connect-timeout}") Duration connectTimeout, 
                          @Value("${model.server.read-timeout}") Duration readTimeout) {
    	
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(connectTimeout)    // 연결이 안 되면 이 시간 뒤 포기
                .build();
        
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(readTimeout); // 연결은 됐는데 응답이 늦으면 이 시간 뒤 포기
    	
        this.client = RestClient.builder()
                .baseUrl(modelServerUrl)
                .defaultHeader("Authorization", "Bearer " + token)
                .requestFactory(requestFactory)
                .build();
    }

    public PredictResponse predict(PredictRequest request) {
        try {
            return client.post()
                    .uri("/predict")
                    .body(request)
                    .retrieve()
                    .body(PredictResponse.class);
        } catch (HttpStatusCodeException e) {
            // FastAPI가 돌려준 4xx/5xx(예: 입력 검증 실패 422)를 그대로 전달
            throw new ModelServerException(e.getStatusCode(), e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            throw new ModelServerException(HttpStatus.BAD_GATEWAY,
                    "{\"detail\":\"모델 서버에 연결할 수 없습니다.\"}", e);
        }
    }
}
