package uk.ac.ebi.eva.ws.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uk.ac.ebi.eva.ws.dto.EvidenceWithSamples;
import uk.ac.ebi.eva.ws.dto.RsIdListResponse;
import uk.ac.ebi.eva.ws.dto.RsIdStudySamplesEntry;
import uk.ac.ebi.eva.ws.dto.SampleGenotype;
import uk.ac.ebi.eva.ws.dto.SourceWithSamples;
import uk.ac.ebi.eva.ws.entity.mongo.ClusteredVariantEntity;
import uk.ac.ebi.eva.ws.entity.mongo.FileDocument;
import uk.ac.ebi.eva.ws.entity.mongo.SubmittedVariantEntity;
import uk.ac.ebi.eva.ws.entity.mongo.VariantDocument;
import uk.ac.ebi.eva.ws.entity.mongo.VariantFileEntry;
import uk.ac.ebi.eva.ws.repository.jpa.AssemblyInfoProjection;
import uk.ac.ebi.eva.ws.repository.jpa.SampleBiosampleProjection;
import uk.ac.ebi.eva.ws.repository.jpa.SampleRepository;
import uk.ac.ebi.eva.ws.repository.mongo.AbstractClusteredVariantRepository;
import uk.ac.ebi.eva.ws.repository.mongo.AccessionProjection;
import uk.ac.ebi.eva.ws.repository.mongo.ClusteredVariantRepository;
import uk.ac.ebi.eva.ws.repository.mongo.DbsnpClusteredVariantRepository;
import uk.ac.ebi.eva.ws.repository.mongo.DbsnpSubmittedVariantRepository;
import uk.ac.ebi.eva.ws.repository.mongo.SubmittedVariantRepository;
import uk.ac.ebi.eva.ws.variantwarehouse.GenotypeDecoder;
import uk.ac.ebi.eva.ws.variantwarehouse.VariantWarehouseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EVAWSService {
    private static final long NO_CURSOR = -1L;
    private static final long EVA_RANGE_START = 3_000_000_000L;
    private static final int OVER_FETCH_MULTIPLIER = 2;

    private final ClusteredVariantRepository clusteredVariantRepository;
    private final DbsnpClusteredVariantRepository dbsnpClusteredVariantRepository;
    private final SubmittedVariantRepository submittedVariantRepository;
    private final DbsnpSubmittedVariantRepository dbsnpSubmittedVariantRepository;
    private final AssemblyInfoResolver assemblyInfoResolver;
    private final VariantWarehouseService variantWarehouseService;
    private final SampleRepository sampleRepository;

    public EVAWSService(ClusteredVariantRepository clusteredVariantRepository,
                        DbsnpClusteredVariantRepository dbsnpClusteredVariantRepository,
                        SubmittedVariantRepository submittedVariantRepository,
                        DbsnpSubmittedVariantRepository dbsnpSubmittedVariantRepository,
                        AssemblyInfoResolver assemblyInfoResolver,
                        VariantWarehouseService variantWarehouseService,
                        SampleRepository sampleRepository) {
        this.clusteredVariantRepository = clusteredVariantRepository;
        this.dbsnpClusteredVariantRepository = dbsnpClusteredVariantRepository;
        this.submittedVariantRepository = submittedVariantRepository;
        this.dbsnpSubmittedVariantRepository = dbsnpSubmittedVariantRepository;
        this.assemblyInfoResolver = assemblyInfoResolver;
        this.variantWarehouseService = variantWarehouseService;
        this.sampleRepository = sampleRepository;
    }

    public RsIdListResponse getRsIds(int pageSize, Long searchAfter) {
        long cursor = searchAfter != null ? searchAfter : NO_CURSOR;
        List<Long> result = new ArrayList<>(pageSize);

        if (cursor < EVA_RANGE_START - 1) {
            result.addAll(fetchDistinctAccessions(dbsnpClusteredVariantRepository, cursor, pageSize));
        }

        if (result.size() < pageSize) {
            long evaCursor = Math.max(cursor, EVA_RANGE_START - 1);
            List<Long> evaAccessions = fetchDistinctAccessions(
                    clusteredVariantRepository, evaCursor, pageSize - result.size());
            result.addAll(evaAccessions);
        }

        Long nextPageAfter = result.size() == pageSize ? result.get(result.size() - 1) : null;

        return new RsIdListResponse(result, nextPageAfter, pageSize);
    }

    private List<Long> fetchDistinctAccessions(AbstractClusteredVariantRepository repository, long cursor, int pageSize) {
        List<Long> distinct = new ArrayList<>(pageSize);
        long currentCursor = cursor;
        int fetchSize = pageSize * OVER_FETCH_MULTIPLIER;

        while (distinct.size() < pageSize) {
            List<Long> batch = repository
                    .findByAccessionGreaterThanOrderByAccessionAsc(currentCursor, PageRequest.of(0, fetchSize))
                    .stream()
                    .map(AccessionProjection::getAccession)
                    .toList();

            if (batch.isEmpty()) {
                break;
            }
            for (Long accession : batch) {
                if (distinct.size() == pageSize) {
                    break;
                }
                if (distinct.isEmpty() || !distinct.get(distinct.size() - 1).equals(accession)) {
                    distinct.add(accession);
                }
            }
            currentCursor = batch.get(batch.size() - 1);
            if (batch.size() < fetchSize) {
                break;
            }
        }
        return distinct;
    }


    public List<RsIdStudySamplesEntry> getSamplesForRSIDAndStudyID(long rsId, String studyId) {
        // Check if the given RSID exists
        List<ClusteredVariantEntity> clusteredVariantEntityList = new ArrayList<>();
        clusteredVariantEntityList.addAll(clusteredVariantRepository.findByAccession(rsId));
        clusteredVariantEntityList.addAll(dbsnpClusteredVariantRepository.findByAccession(rsId));

        if (clusteredVariantEntityList.isEmpty()) {
            return List.of();
        }

        // Get all Submitted Variants for the given RSID and StudyID
        List<SubmittedVariantEntity> submittedVariantList = new ArrayList<>();
        submittedVariantList.addAll(submittedVariantRepository.findByRsIdAndStudyId(rsId, studyId));
        submittedVariantList.addAll(dbsnpSubmittedVariantRepository.findByRsIdAndStudyId(rsId, studyId));

        if (submittedVariantList.isEmpty()) {
            return List.of();
        }
        // Group all Submitted variants by Assembly Accession
        Map<String, List<SubmittedVariantEntity>> submittedVariantsByAssembly = submittedVariantList.stream()
                .collect(Collectors.groupingBy(SubmittedVariantEntity::getAssemblyAccession));

        List<RsIdStudySamplesEntry> entries = new ArrayList<>();
        for (Map.Entry<String, List<SubmittedVariantEntity>> entrySet : submittedVariantsByAssembly.entrySet()) {
            buildEntry(rsId, entrySet.getValue().get(0).getTaxonomyId(), entrySet.getKey(), entrySet.getValue()).ifPresent(entries::add);
        }

        return entries;
    }

    private Optional<RsIdStudySamplesEntry> buildEntry(long rsId, Integer taxonomyAccession, String assemblyAccession,
                                                       List<SubmittedVariantEntity> submittedVariantList) {
        // Get the taxonomy code and assembly code that helps in figuring out the variant warehouse db name
        Optional<AssemblyInfoProjection> assemblyInfo = assemblyInfoResolver.resolve(taxonomyAccession, assemblyAccession);
        if (assemblyInfo.isEmpty()) {
            return Optional.empty();
        }

        List<EvidenceWithSamples> evidences = new ArrayList<>();
        // TODO: build evidence should be called for list of submitted variants
        for (SubmittedVariantEntity ss : submittedVariantList) {
            buildEvidence(assemblyInfo.get(), ss).ifPresent(evidences::add);
        }
        if (evidences.isEmpty()) {
            return Optional.empty();
        }

        SubmittedVariantEntity submittedVariantEntity = submittedVariantList.get(0);
        return Optional.of(new RsIdStudySamplesEntry(
                rsId,
                submittedVariantEntity.getType(),
                assemblyAccession,
                submittedVariantEntity.getContig(),
                submittedVariantEntity.getStart(),
                submittedVariantEntity.getReference(),
                submittedVariantEntity.getAlternate(),
                evidences));
    }

    private Optional<EvidenceWithSamples> buildEvidence(AssemblyInfoProjection assemblyInfo,
                                                        SubmittedVariantEntity submittedVariantEntity) {
        Optional<VariantDocument> variant = variantWarehouseService.findVariant(
                assemblyInfo.getTaxonomyCode(), assemblyInfo.getAssemblyCode(),
                submittedVariantEntity.getContig(), submittedVariantEntity.getStart(),
                submittedVariantEntity.getReference(), submittedVariantEntity.getAlternate(),
                submittedVariantEntity.getSsId());
        if (variant.isEmpty()) {
            return Optional.empty();
        }

        List<VariantFileEntry> matchingVariantFiles = variant.get().getFiles().stream()
                .filter(f -> submittedVariantEntity.getStudyId().equals(f.getStudyId()))
                .toList();
        if (matchingVariantFiles.isEmpty()) {
            return Optional.empty();
        }

        List<SourceWithSamples> sources = new ArrayList<>();
        for (VariantFileEntry variantFileEntry : matchingVariantFiles) {
            buildSource(assemblyInfo, submittedVariantEntity, variantFileEntry).ifPresent(sources::add);
        }
        if (sources.isEmpty()) {
            return Optional.empty();
        }

        // TODO: fetch it once before and pass it down to the other methods
        FileDocument fileDocument = variantWarehouseService
                .findFiles(assemblyInfo.getTaxonomyCode(), assemblyInfo.getAssemblyCode(),
                        matchingVariantFiles.get(0).getFileId(), submittedVariantEntity.getStudyId())
                .get(0);

        return Optional.of(new EvidenceWithSamples(
                submittedVariantEntity.getSsId(),
                submittedVariantEntity.getStudyId(),
                submittedVariantEntity.getRemappedFrom(),
                submittedVariantEntity.getTaxonomyId(),
                submittedVariantEntity.getCreatedDate(),
                fileDocument.getStudyName(),
                fileDocument.getStudyType(),
                sources));
    }

    private Optional<SourceWithSamples> buildSource(AssemblyInfoProjection assemblyInfo,
                                                    SubmittedVariantEntity submittedVariantEntity,
                                                    VariantFileEntry variantFileEntry) {
        List<FileDocument> fileDocs = variantWarehouseService.findFiles(
                assemblyInfo.getTaxonomyCode(), assemblyInfo.getAssemblyCode(),
                variantFileEntry.getFileId(), submittedVariantEntity.getStudyId());

        if (fileDocs.isEmpty()) {
            return Optional.empty();
        }

        if (fileDocs.size() > 1) {
            throw new UnsupportedOperationException(
                    "%d files_2_0 documents share fid=%s sid=%s - cannot determine which one this "
                            + "variant file entry belongs to rsId=%d ssId=%d"
                            .formatted(fileDocs.size(), variantFileEntry.getFileId(),
                                    submittedVariantEntity.getStudyId(),
                                    submittedVariantEntity.getRsId(), submittedVariantEntity.getSsId()));
        }

        FileDocument fileDoc = fileDocs.get(0);

        Map<Integer, String> sampleNameByIndex = new HashMap<>();
        for (Map.Entry<String, Integer> entry : fileDoc.getSamplePositions().entrySet()) {
            sampleNameByIndex.put(entry.getValue(), entry.getKey());
        }

        Map<Integer, String> genotypeByIndex = GenotypeDecoder.decode(variantFileEntry.getSamp(), sampleNameByIndex.size());

        Map<String, String> biosampleByName = sampleRepository
                .findBiosampleAccessions(submittedVariantEntity.getStudyId(), sampleNameByIndex.values())
                .stream()
                .collect(Collectors.toMap(
                        SampleBiosampleProjection::getNameInVcf,
                        SampleBiosampleProjection::getBiosampleAccession));

        List<SampleGenotype> samples = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : sampleNameByIndex.entrySet()) {
            String genotype = genotypeByIndex.get(entry.getKey());
            if (genotype == null) {
                continue;
            }
            String sampleName = entry.getValue();
            samples.add(new SampleGenotype(sampleName, genotype, biosampleByName.get(sampleName)));
        }

        return Optional.of(new SourceWithSamples(fileDoc.getFileId(), samples));
    }

}