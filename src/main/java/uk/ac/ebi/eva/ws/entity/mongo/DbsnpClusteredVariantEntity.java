package uk.ac.ebi.eva.ws.entity.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dbsnpClusteredVariantEntity")
public class DbsnpClusteredVariantEntity extends AbstractClusteredVariantEntity {
}
