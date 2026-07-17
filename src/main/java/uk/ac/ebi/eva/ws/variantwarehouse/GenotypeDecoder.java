package uk.ac.ebi.eva.ws.variantwarehouse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GenotypeDecoder {

    private GenotypeDecoder() {
    }

    public static Map<Integer, String> decode(Map<String, Object> samp, int sampleCount) {
        Map<Integer, String> genotypeByIndex = new HashMap<>();
        if (samp == null) {
            return genotypeByIndex;
        }

        String defaultGenotype = null;
        for (Map.Entry<String, Object> entry : samp.entrySet()) {
            if ("def".equals(entry.getKey())) {
                defaultGenotype = String.valueOf(entry.getValue());
                continue;
            }
            String genotype = entry.getKey();
            List<?> indices = entry.getValue() instanceof List<?> list ? list : List.of(entry.getValue());
            for (Object indexValue : indices) {
                genotypeByIndex.put(((Number) indexValue).intValue(), genotype);
            }
        }

        if (defaultGenotype != null) {
            for (int i = 0; i < sampleCount; i++) {
                genotypeByIndex.putIfAbsent(i, defaultGenotype);
            }
        }

        return genotypeByIndex;
    }
}
