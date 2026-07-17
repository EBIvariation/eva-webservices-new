package uk.ac.ebi.eva.ws.entity.mongo;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

public class VariantFileEntry {

    @Field("fid")
    private String fileId;

    @Field("sid")
    private String studyId;

    @Field("samp")
    private Map<String, Object> samp;

    public String getFileId() {
        return fileId;
    }

    public String getStudyId() {
        return studyId;
    }

    public Map<String, Object> getSamp() {
        return samp;
    }
}