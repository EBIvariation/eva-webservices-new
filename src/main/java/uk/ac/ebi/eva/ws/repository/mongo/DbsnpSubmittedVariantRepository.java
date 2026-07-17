package uk.ac.ebi.eva.ws.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.eva.ws.entity.mongo.DbsnpSubmittedVariantEntity;
import uk.ac.ebi.eva.ws.entity.mongo.SubmittedVariantEntity;

import java.util.List;

public interface DbsnpSubmittedVariantRepository extends MongoRepository<DbsnpSubmittedVariantEntity, String> {
    List<SubmittedVariantEntity> findByRsIdAndStudyId(Long rsId, String studyId);
}