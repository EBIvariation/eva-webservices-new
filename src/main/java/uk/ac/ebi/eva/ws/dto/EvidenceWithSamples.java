package uk.ac.ebi.eva.ws.dto;

import java.util.List;

public record EvidenceWithSamples(
        Long ssId,
        String studyId,
        String remappedFrom,
        Integer taxonomyId,
        String createdDate,
        String studyName,
        String studyType,
        List<SourceWithSamples> sources
) {
}