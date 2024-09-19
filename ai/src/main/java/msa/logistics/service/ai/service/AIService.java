package msa.logistics.service.ai.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.logistics.service.ai.domain.AI;
import msa.logistics.service.ai.dto.ai.ChatRequest;
import msa.logistics.service.ai.dto.ai.ChatResponse;
import msa.logistics.service.ai.repository.AIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIService {
    @Qualifier("AIRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    private final AIRepository aiRepository;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private static final String LIMIT_MESSAGE = "답변을 최대한 간결하게 한 문장으로 100자 이하로";

    private static final String WEATHER_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";

    private static final String WEATHER_SECRET_KEY = "=UhsauVItp7kbeG26NdjY1%2BZxwQ4PLWXlVCJ7cj3jFWsffm9sF21a3pCqWBVkHxPTyED7uGzaMCIxZ1ABN2d%2BRg%3D%3D";

    private static final String ENCODING = "UTF-8";

    public String getAIRequest(String requestMessage) {

        // Gemini에 요청 전송
        String requestUrl = apiUrl + "?key=" + geminiApiKey;

        ChatRequest request = new ChatRequest(requestMessage + LIMIT_MESSAGE);
        ChatResponse response = restTemplate.postForObject(requestUrl, request, ChatResponse.class);

        String responseMessage = response.getCandidates().get(0).getContent().getParts().get(0).getText().toString();

        aiRepository.save(AI.createAI(requestMessage, responseMessage));

        return responseMessage;
    }

    public String getWeather(Integer latitude, Integer longitude) throws IOException {

        String beforeUrl = makeUrl(latitude, longitude);

        URL url = new URL(beforeUrl);

        return getWeatherResult(url);
    }

    private String makeUrl(Integer latitude, Integer longitude) throws UnsupportedEncodingException {

        String date = getTodayDate();
        String lat = latitude.toString();
        String lng = longitude.toString();

        StringBuilder urlBuilder = new StringBuilder(WEATHER_URL); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", ENCODING) + WEATHER_SECRET_KEY); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", ENCODING) + "=" + URLEncoder.encode("1", ENCODING));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", ENCODING) + "=" + URLEncoder.encode("1000", ENCODING));
        urlBuilder.append("&" + URLEncoder.encode("dataType", ENCODING) + "=" + URLEncoder.encode("JSON", ENCODING));
        urlBuilder.append(
                "&" + URLEncoder.encode("base_date", ENCODING) + "=" + URLEncoder.encode(date, ENCODING));
        urlBuilder.append("&" + URLEncoder.encode("base_time", ENCODING) + "=" + URLEncoder.encode("0600", ENCODING));
        urlBuilder.append("&" + URLEncoder.encode("nx", ENCODING) + "=" + URLEncoder.encode(lat, ENCODING));
        urlBuilder.append("&" + URLEncoder.encode("ny", ENCODING) + "=" + URLEncoder.encode(lng, ENCODING));

        return urlBuilder.toString();
    }

    private String getTodayDate() {
        // 현재 날짜 가져오기
        LocalDate today = LocalDate.now();

        // 날짜 형식 지정: 두 자리 년, 월, 일
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        // 지정된 형식으로 날짜 변환
        String formattedDate = today.format(formatter);

        return formattedDate;
    }

    private String getWeatherResult(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        // System.out.println("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        return sb.toString();
    }
}
