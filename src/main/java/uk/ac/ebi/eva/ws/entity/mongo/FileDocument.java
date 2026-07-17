package uk.ac.ebi.eva.ws.entity.mongo;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

public class FileDocument {

    @Field("fid")
    private String fileId;

    @Field("sid")
    private String studyId;

    @Field("sname")
    private String studyName;

    @Field("stype")
    private String studyType;

    @Field("samp")
    private Map<String, Integer> samplePositions;

    public String getFileId() {
        return fileId;
    }

    public String getStudyId() {
        return studyId;
    }

    public String getStudyName() {
        return studyName;
    }

    public String getStudyType() {
        return studyType;
    }

    public Map<String, Integer> getSamplePositions() {
        return samplePositions;
    }
}
