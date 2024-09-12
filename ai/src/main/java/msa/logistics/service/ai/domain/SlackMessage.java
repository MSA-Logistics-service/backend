package msa.logistics.service.ai.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "p_slack_msgs")
public class SlackMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "slack_msg_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "slack_id")
    private String slackId;

    @Column(name = "slack_msg")
    private String slackMessage;

    @Column(name = "send_time")
    private LocalDateTime sendTime;

    private Long userId;
}
