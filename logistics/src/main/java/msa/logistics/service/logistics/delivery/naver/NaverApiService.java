package msa.logistics.service.logistics.delivery.naver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service


public class NaverApiService {

    private final RestTemplate restTemplate;

    public NaverApiService(RestTemplateBuilder builder) {
        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
    }

    public NaverDto getDirection(String start, String end) {
        //요청 URL 만들기

        URI uri = UriComponentsBuilder
                .fromUriString("https://naveropenapi.apigw.ntruss.com")
                .path("map-direction/v1/driving")
                .queryParam("start",start)
                .queryParam("goal",end)
                .queryParam("option","trafast")
                .encode()
                .build()
                .toUri();
        log.info("uri =" +uri);


        RequestEntity<Void> requestEntity = RequestEntity
                .get(uri)
                .header("X-NCP-APIGW-API-KEY-ID","vlb8sa5oe3")
                .header("X-NCP-APIGW-API-KEY","hBiabqMO6ycdcrBBu3h7qmYgsftW7Oi1TZDRHl49")
                .build();

        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        log.info("response =" +response);

        return fromJSONtoItems(response.getBody());
    }

    public NaverDto fromJSONtoItems(String responseEntity) {
        JSONObject jsonObject = new JSONObject(responseEntity);
        JSONArray trafastArray = jsonObject.getJSONObject("route").getJSONArray("trafast");
        List<NaverDto> itemDtoList = new ArrayList<>();

        // 첫 번째 trafast 경로만 처리 (다수의 경로가 있을 경우 추가 처리 필요)
        if (trafastArray.length() > 0) {
            JSONObject summary = trafastArray.getJSONObject(0).getJSONObject("summary");

            //summary에서 distance와 duration 추출하여 NaverDto로 반환
            double distance =summary.getDouble("distance");
            long duration = summary.getLong("duration");

            return new NaverDto(duration,distance);

        }

        return null;
    }

    }
