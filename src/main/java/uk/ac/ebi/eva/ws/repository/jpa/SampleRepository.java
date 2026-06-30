package uk.ac.ebi.eva.ws.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.ac.ebi.eva.ws.entity.jpa.Sample;

import java.util.Collection;
import java.util.List;

public interface SampleRepository extends JpaRepository<Sample, Long> {

    @Query(value = """
            SELECT fs.name_in_file AS nameInVcf, s.biosample_accession AS biosampleAccession FROM evapro.sample s 
            join evapro.file_sample fs on s.sample_id=fs.sample_id
            join evapro.file f on f.file_id=fs.file_id
            join evapro.analysis_file af on af.file_id=f.file_id
            join evapro.project_analysis pa on pa.analysis_accession=af.analysis_accession
            WHERE pa.project_accession=:studyId and fs.name_in_file IN (:namesInVcf)
            """, nativeQuery = true)
    List<SampleBiosampleProjection> findBiosampleAccessions(@Param("studyId") String studyId,
                                                            @Param("namesInVcf") Collection<String> namesInVcf);
}
