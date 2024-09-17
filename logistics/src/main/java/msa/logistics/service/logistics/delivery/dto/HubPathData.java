package msa.logistics.service.logistics.delivery.dto;

import java.util.UUID;

public class HubPathData {

    // 소요 시간
    private int duration;

    // 예상 거리
    private double estimatedDistance;

    // 출발 허브 ID
    private UUID startHubId;

    // 도착 허브 ID
    private UUID destinationHubId;

    // 기본 생성자
    public HubPathData() {
    }

    // 모든 필드를 초기화하는 생성자
    public HubPathData(int duration, double estimatedDistance, UUID startHubId, UUID destinationHubId) {
        this.duration = duration;
        this.estimatedDistance = estimatedDistance;
        this.startHubId = startHubId;
        this.destinationHubId = destinationHubId;
    }

    // Getters and Setters
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getEstimatedDistance() {
        return estimatedDistance;
    }

    public void setEstimatedDistance(double estimatedDistance) {
        this.estimatedDistance = estimatedDistance;
    }

    public UUID getStartHubId() {
        return startHubId;
    }

    public void setStartHubId(UUID startHubId) {
        this.startHubId = startHubId;
    }

    public UUID getDestinationHubId() {
        return destinationHubId;
    }

    public void setDestinationHubId(UUID destinationHubId) {
        this.destinationHubId = destinationHubId;
    }
}
