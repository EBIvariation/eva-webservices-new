package uk.ac.ebi.eva.ws.entity.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clusteredVariantEntity")
public class ClusteredVariantEntity extends AbstractClusteredVariantEntity {
}
