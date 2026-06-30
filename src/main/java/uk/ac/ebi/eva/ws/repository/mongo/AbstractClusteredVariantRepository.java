package uk.ac.ebi.eva.ws.repository.mongo;

import org.springframework.data.domain.Pageable;
import uk.ac.ebi.eva.ws.entity.mongo.ClusteredVariantEntity;

import java.util.List;

public interface AbstractClusteredVariantRepository {
    List<AccessionProjection> findByAccessionGreaterThanOrderByAccessionAsc(Long accessionCursor, Pageable pageable);
    List<ClusteredVariantEntity> findByAccession(Long accession);
}
