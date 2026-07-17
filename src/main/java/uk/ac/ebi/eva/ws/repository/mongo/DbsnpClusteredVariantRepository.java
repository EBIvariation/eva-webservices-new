package uk.ac.ebi.eva.ws.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.eva.ws.entity.mongo.DbsnpClusteredVariantEntity;

public interface DbsnpClusteredVariantRepository extends MongoRepository<DbsnpClusteredVariantEntity, String>,
        AbstractClusteredVariantRepository {

}
