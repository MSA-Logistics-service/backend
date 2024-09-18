package com.msa.hub.hubPath.controller;

import com.msa.hub.hubPath.domain.HubPath;
import com.msa.hub.hubPath.dto.HubPathRequestDto;
import com.msa.hub.hubPath.dto.HubPathResponseDto;
import com.msa.hub.hubPath.service.HubPathService;
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
@RequestMapping("/api/v1/hubPath")
@RequiredArgsConstructor
public class HubPathController {

    private final HubPathService hubPathService;

    // 허브 등록
    // 마스터 관리자만 가능
    @PostMapping
    public ResponseEntity<HubPathResponseDto> registerHubPath(@RequestBody HubPathRequestDto hubPathRequestDto) {
        HubPathResponseDto registeredHubPath = hubPathService.registerHubPath(hubPathRequestDto);
        return ResponseEntity.ok(registeredHubPath);
    }


    // HubPath 수정
    // 마스터 관리자만 가능
    @PatchMapping("/{hubPathId}")
    public ResponseEntity<HubPathResponseDto> updateHubPath(
            @PathVariable UUID hubPathId,
            @RequestBody HubPathRequestDto hubPathRequestDto,
            @RequestHeader("X-User-Id") String user,         // 헤더에서 사용자 ID 받기
            @RequestHeader("X-User-Role") String userRole) { // 헤더에서 사용자 역할 받기

        // 서비스에서 HubPath 수정 로직 호출
        HubPathResponseDto updatedHubPath = hubPathService.updateHubPath(hubPathId, hubPathRequestDto, user, userRole);

        return ResponseEntity.ok(updatedHubPath);
    }

    // HubPath 삭제 (소프트 삭제)
    // 마스터 관리자만 가능
    @DeleteMapping("/delete/{hubPathId}")
    public ResponseEntity<Void> deleteHubPath(@PathVariable UUID hubPathId) {
        hubPathService.deleteHubPath(hubPathId);
        return ResponseEntity.noContent().build();
    }

    // HubPath 상세 조회
    @GetMapping("/{hubPathId}")
    public ResponseEntity<HubPath> getHubPathDetail(@PathVariable UUID hubPathId) {
        Optional<HubPath> hubPath = hubPathService.getHubPathDetail(hubPathId);
        return hubPath.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 모든 HubPath 조회 (is_delete = false인 항목만 페이지네이션 및 정렬 추가)
    // 로그인 사용자가 가능
    @GetMapping
    public ResponseEntity<Page<HubPath>> getAllHubPaths(
            @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "startHub", required = false) String startHub,
            @RequestParam(value = "endHub", required = false) String endHub,
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
        // Page<HubPath> hubPaths = hubPathService.getAllHubPaths(pageable);
        // 필터링 조건을 사용한 서비스 호출
        Page<HubPath> hubPaths = hubPathService.getFilteredHubPaths(startHub, endHub, pageable);


        // 페이징된 결과 반환
        return ResponseEntity.ok(hubPaths);
    }

    //시작허브 도착허브 받을 때
    // 로그인 사용자가 가능
    @GetMapping("/list")
    public ResponseEntity<List<HubPath>> getHubPaths(
            @RequestParam(value = "startHub") UUID  startHub,
            @RequestParam(value = "endHub") UUID  endHub) {

        // 필터링 조건을 사용한 서비스 호출
        List<HubPath> hubPaths = hubPathService.getStardEndPaths(startHub, endHub);


        // 페이징된 결과 반환
        return ResponseEntity.ok(hubPaths);
    }

}
