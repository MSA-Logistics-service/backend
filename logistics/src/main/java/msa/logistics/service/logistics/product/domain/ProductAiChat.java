package msa.logistics.service.logistics.product.domain;

import jakarta.persistence.*;
import lombok.*;
import msa.logistics.service.logistics.common.entity.BaseEntity;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Entity
@Builder
@Table(name = "p_product_ai_chats")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductAiChat extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "product_ai_chat_id")
    private UUID productAiChatId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(length = 2000) // 500자로 길이를 늘림
    private String response;

}
