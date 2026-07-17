package uk.ac.ebi.eva.ws.entity.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sample", schema = "evapro")
public class Sample {

    @Id
    private Long sampleId;
}