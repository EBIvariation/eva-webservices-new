package uk.ac.ebi.eva.ws.dto;

import java.util.List;

public record SourceWithSamples(String analysis, List<SampleGenotype> samples) {
}
