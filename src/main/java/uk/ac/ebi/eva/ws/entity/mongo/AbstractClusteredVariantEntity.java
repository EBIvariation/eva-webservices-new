package uk.ac.ebi.eva.ws.entity.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public abstract class AbstractClusteredVariantEntity {

    @Id
    private String id;

    @Field("accession")
    private Long accession;

    public String getId() {
        return id;
    }

    public Long getAccession() {
        return accession;
    }
}