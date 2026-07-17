package uk.ac.ebi.eva.ws.dto;

import java.util.List;

public record RsIdStudySamplesEntry(
        Long rsId,
        String type,
        String assemblyAccession,
        String chromosome,
        Long start,
        String reference,
        String alternate,
        List<EvidenceWithSamples> evidences
) {
}
