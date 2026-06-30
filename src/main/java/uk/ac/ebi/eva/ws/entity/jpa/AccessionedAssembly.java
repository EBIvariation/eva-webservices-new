package uk.ac.ebi.eva.ws.entity.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "accessioned_assembly", schema = "evapro")
public class AccessionedAssembly {
    @Id
    private String assemblyAccession;
}