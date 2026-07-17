package uk.ac.ebi.eva.ws.entity.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

public class VariantDocument {

    @Id
    private String id;

    @Field("chr")
    private String chromosome;

    @Field("start")
    private Long start;

    @Field("end")
    private Long end;

    @Field("ref")
    private String reference;

    @Field("alt")
    private String alternate;

    @Field("type")
    private String type;

    @Field("ids")
    private List<String> ids;

    @Field("files")
    private List<VariantFileEntry> files;

    public String getId() {
        return id;
    }

    public String getChromosome() {
        return chromosome;
    }

    public Long getStart() {
        return start;
    }

    public Long getEnd() {
        return end;
    }

    public String getReference() {
        return reference;
    }

    public String getAlternate() {
        return alternate;
    }

    public String getType() {
        return type;
    }

    public List<String> getIds() {
        return ids;
    }

    public List<VariantFileEntry> getFiles() {
        return files;
    }
}
