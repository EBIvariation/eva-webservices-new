package uk.ac.ebi.eva.ws.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.eva.ws.entity.mongo.ClusteredVariantEntity;

public interface ClusteredVariantRepository extends MongoRepository<ClusteredVariantEntity, String>,
        AbstractClusteredVariantRepository {
}
