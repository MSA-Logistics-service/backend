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
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/hub-path")
@RequiredArgsConstructor
public class HubPathController {

    private final HubPathService hubPathService;

    // HubPath 등록 (마스터 관리자만 가능)
    @PostMapping
    public ResponseEntity<HubPathResponseDto> registerHubPath(@RequestBody HubPathRequestDto hubPathRequestDto) {
        HubPathResponseDto registeredHubPath = hubPathService.registerHubPath(hubPathRequestDto);
        return ResponseEntity.ok(registeredHubPath);
    }

    // HubPath 수정 (마스터 관리자만 가능)
    @PatchMapping("/{hubPathId}")
    public ResponseEntity<HubPathResponseDto> updateHubPath(
            @PathVariable UUID hubPathId,
            @RequestBody HubPathRequestDto hubPathRequestDto,
            @RequestHeader("X-User-Name") String username,         // 헤더에서 사용자 ID 받기
            @RequestHeader("X-User-Roles") String userRole) { // 헤더에서 사용자 역할 받기
        HubPathResponseDto updatedHubPath = hubPathService.updateHubPath(hubPathId, hubPathRequestDto, username, userRole);
        return ResponseEntity.ok(updatedHubPath);
    }

    // HubPath 삭제 (소프트 삭제, 마스터 관리자만 가능)
    @PatchMapping("/delete/{hubPathId}")
    public ResponseEntity<Void> deleteHubPath(@PathVariable UUID hubPathId) {
        hubPathService.deleteHubPath(hubPathId);
        return ResponseEntity.noContent().build();
    }

    // HubPath 상세 조회 (모든 사용자 가능)
    @GetMapping("/{hubPathId}")
    public ResponseEntity<HubPathResponseDto> getHubPathDetail(@PathVariable UUID hubPathId) {
        HubPath hubPath = hubPathService.getHubPathDetail(hubPathId)
                .orElseThrow(() -> new IllegalArgumentException("HubPath not found: " + hubPathId));
        return ResponseEntity.ok(new HubPathResponseDto(hubPath));
    }

    // 모든 HubPath 조회 (필터링 및 페이지네이션, 로그인 사용자 가능)
    @GetMapping
    public ResponseEntity<Page<HubPathResponseDto>> getAllHubPaths(
            @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "startHub", required = false) String startHub,
            @RequestParam(value = "endHub", required = false) String endHub,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc) {

        // 유효한 pageSize 및 sortBy 값 검증
        pageSize = validatePageSize(pageSize);
        sortBy = validateSortBy(sortBy);

        // 정렬 방향 설정
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        // 페이징 및 정렬 설정
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        // startHub와 endHub를 UUID로 변환
        UUID startHubUUID = convertStringToUUID(startHub);
        UUID endHubUUID = convertStringToUUID(endHub);
        // 필터링 조건을 사용한 서비스 호출
        Page<HubPath> hubPaths = hubPathService.getFilteredHubPaths(startHubUUID, endHubUUID, pageable);

        // HubPathResponseDto로 변환하여 반환
        Page<HubPathResponseDto> responseDtos = hubPaths.map(HubPathResponseDto::new);

        return ResponseEntity.ok(responseDtos);
    }

    // 시작 허브와 도착 허브 기준으로 경로 조회 (로그인 사용자 가능)
    @GetMapping("/list")
    public ResponseEntity<List<HubPathResponseDto>> getHubPathsByStartAndEnd(
            @RequestParam(value = "startHub", required = false) String startHub,
            @RequestParam(value = "endHub", required = false) String endHub) {
        UUID startHubUUID = convertStringToUUID(startHub);
        UUID endHubUUID = convertStringToUUID(endHub);

        // 시작 및 종료 허브 경로 검색
        List<HubPath> hubPaths = hubPathService.getStartEndPaths(startHubUUID, endHubUUID);

        // HubPathResponseDto로 변환하여 반환
        List<HubPathResponseDto> responseDtos = hubPaths.stream()
                .map(HubPathResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

    // 유효한 pageSize 값을 검증하고 기본값 설정
    private int validatePageSize(int pageSize) {
        if (pageSize != 10 && pageSize != 30 && pageSize != 50) {
            return 10;
        }
        return pageSize;
    }

    // 유효한 sortBy 값을 검증하고 기본값 설정
    private String validateSortBy(String sortBy) {
        if (!sortBy.equals("createdAt") && !sortBy.equals("updatedAt")) {
            return "createdAt";
        }
        return sortBy;
    }

    private UUID convertStringToUUID(String uuidString) {
        try {
            return (uuidString != null && !uuidString.isEmpty()) ? UUID.fromString(uuidString) : null;
        } catch (IllegalArgumentException e) {
            return null; // 변환이 실패하면 null 반환
        }
    }
}
