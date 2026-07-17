package uk.ac.ebi.eva.ws.entity.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dbsnpSubmittedVariantEntity")
public class DbsnpSubmittedVariantEntity extends SubmittedVariantEntity {
}
