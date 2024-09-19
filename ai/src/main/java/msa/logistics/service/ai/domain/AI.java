package msa.logistics.service.ai.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.logistics.service.ai.common.domain.BaseEntity;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_ais")
public class AI extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ai_id", updatable = false, nullable = false)
    private UUID id;

    @Size(max = 50)
    @Column(name = "ai_request")
    private String aiRequest;

    @Size(max = 50)
    @Column(name = "ai_response")
    private String aiResponse;

    private Long userId;

    public static AI createAI(String request, String response) {
        AI ai = new AI();
        ai.aiRequest = request;
        ai.aiResponse = response;
        return ai;
    }

    public void softDelete(String deletedBy) {
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
    }
}