package uk.ac.ebi.eva.ws.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.eva.ws.entity.mongo.SubmittedVariantEntity;

import java.util.List;

public interface SubmittedVariantRepository extends MongoRepository<SubmittedVariantEntity, String> {
    List<SubmittedVariantEntity> findByRsIdAndStudyId(Long rsId, String studyId);
}