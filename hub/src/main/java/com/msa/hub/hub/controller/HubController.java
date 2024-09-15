package com.msa.hub.hub.controller;

import com.msa.hub.hub.domain.Hub;
import com.msa.hub.hub.dto.HubResponseDto;
import com.msa.hub.hub.service.HubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hub")
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    // 허브 등록
    @PostMapping
    public ResponseEntity<Hub> registerHub(@RequestBody HubResponseDto hubDTO) {
        Hub registeredHub = hubService.registerHub(hubDTO);
        return ResponseEntity.ok(registeredHub);
    }


    // Hub 수정
    @PutMapping("/{hubId}")
    public ResponseEntity<Hub> updateHub(@PathVariable UUID hubId, @RequestBody HubResponseDto hubDTO) {
        Hub updatedHub = hubService.updateHub(hubId, hubDTO);
        return ResponseEntity.ok(updatedHub);
    }

    // Hub 삭제 (소프트 삭제)
    @DeleteMapping("/delete/{hubId}")
    public ResponseEntity<Void> deleteHub(@PathVariable UUID hubId) {
        hubService.deleteHub(hubId);
        return ResponseEntity.noContent().build();
    }

    // Hub 상세 조회
    @GetMapping("/{hubId}")
    public ResponseEntity<Hub> getHubDetail(@PathVariable UUID hubId) {
        Optional<Hub> hub = hubService.getHubDetail(hubId);
        return hub.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 모든 Hub 조회 (is_delete = false인 항목만 페이지네이션 및 정렬 추가)
    @GetMapping
    public ResponseEntity<Page<Hub>> getAllHubs(
            @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "hubName", required = false) String hubName,
            @RequestParam(value = "hub_address", required = false) String hubAddress,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc) {

        // 유효한 pageSize 및 sortBy 값 검증
        if (pageSize != 10 && pageSize != 30 && pageSize != 50) {
            pageSize = 10;
        }
        if (!sortBy.equals("createdAt") && !sortBy.equals("updatedAt")) {
            sortBy = "createdAt";
        }

        // 정렬 방향 설정
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        // 페이징 및 정렬 설정
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        // 서비스에서 페이지네이션된 결과 가져오기 (is_delete = false인 항목만)
        // Page<Hub> hubs = hubService.getAllHubs(pageable);
        // 필터링 조건을 사용한 서비스 호출
        Page<Hub> hubs = hubService.getFilteredHubs(hubName, hubAddress, pageable);


        // 페이징된 결과 반환
        return ResponseEntity.ok(hubs);
    }
}
