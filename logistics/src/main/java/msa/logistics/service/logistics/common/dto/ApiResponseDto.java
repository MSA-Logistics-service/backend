package msa.logistics.service.logistics.common.dto;

import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto<T> {
    private HttpStatus status;  // 상태 코드
    private String message;     // 메시지
    private T data;             // 제네릭 타입 데이터
}
