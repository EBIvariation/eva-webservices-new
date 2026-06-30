package uk.ac.ebi.eva.ws.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.ac.ebi.eva.ws.entity.jpa.AccessionedAssembly;

import java.util.Optional;

public interface AssemblyRepository extends JpaRepository<AccessionedAssembly, String> {

    @Query(value = """
            SELECT distinct ast.assembly_code AS assemblyCode, t.taxonomy_code AS taxonomyCode
            FROM evapro.accessioned_assembly aa
            JOIN evapro.assembly_set ast ON aa.assembly_set_id = ast.assembly_set_id
            JOIN evapro.taxonomy t ON ast.taxonomy_id = t.taxonomy_id
            WHERE t.taxonomy_id = :taxonomyAccession AND aa.assembly_accession = :assemblyAccession
            """, nativeQuery = true)
    Optional<AssemblyInfoProjection> findAssemblyInfo(@Param("taxonomyAccession") Integer taxonomyAccession,
                                                      @Param("assemblyAccession") String assemblyAccession);
}