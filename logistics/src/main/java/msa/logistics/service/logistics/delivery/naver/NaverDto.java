package msa.logistics.service.logistics.delivery.naver;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONObject;

@Data
@AllArgsConstructor
public class NaverDto {
    private long duration; // 소요 시간 (초 단위)
    private double distance; // 거리 (미터 단위)

    // JSONObject를 통해 데이터를 받아오는 생성자
    public NaverDto(JSONObject summary) {
        this.duration = summary.getLong("duration"); // 소요 시간 (밀리초 단위)
        this.distance = summary.getDouble("distance"); // 거리 (미터 단위)
    }
}
