package msa.logistics.service.ai.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class Hub {

    private UUID hubId;
    private String hubName;
    private String hubAddress;
    private Double hubLatitude;
    private Double hubLongitude;
    private Integer hubRank;
}
