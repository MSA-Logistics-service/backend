package msa.logistics.service.logistics.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ProductCreateRequestDto {
    @NotBlank
    private String productName;

    @NotNull
    private Long stockQuantity;

    @NotNull
    private UUID vendorId;

    @NotNull
    private UUID hubId;

    @Builder
    public ProductCreateRequestDto(String productName, Long stockQuantity, UUID vendorId, UUID hubId) {
        this.productName = productName;
        this.stockQuantity = stockQuantity;
        this.vendorId = vendorId;
        this.hubId = hubId;
    }
}
