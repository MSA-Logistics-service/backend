package msa.logistics.service.logistics.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ProductUpdateRequestDto {
    @NotBlank
    private String productName;

    @NotNull
    private Long stockQuantity;

    @NotNull
    private UUID vendorId;

    @NotNull
    private UUID hubId;
}
