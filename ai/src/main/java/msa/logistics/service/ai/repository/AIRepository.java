package msa.logistics.service.ai.repository;

import java.util.UUID;
import msa.logistics.service.ai.domain.AI;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIRepository extends JpaRepository<AI, UUID> {
}
