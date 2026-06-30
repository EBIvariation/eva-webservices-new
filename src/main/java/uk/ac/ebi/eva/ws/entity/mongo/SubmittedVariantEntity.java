package uk.ac.ebi.eva.ws.entity.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "submittedVariantEntity")
public class SubmittedVariantEntity {

    @Id
    private String id;

    @Field("accession")
    private Long ssId;

    @Field("rs")
    private Long rsId;

    @Field("seq")
    private String assemblyAccession;

    @Field("tax")
    private Integer taxonomyId;

    @Field("contig")
    private String contig;

    @Field("start")
    private Long start;

    @Field("ref")
    private String reference;

    @Field("alt")
    private String alternate;

    @Field("study")
    private String studyId;

    @Field("remappedFrom")
    private String remappedFrom;

    @Field("createdDate")
    private String createdDate;

    @Field("type")
    private String type;

    public String getId() {
        return id;
    }

    public Long getSsId() {
        return ssId;
    }

    public Long getRsId() {
        return rsId;
    }

    public String getAssemblyAccession() {
        return assemblyAccession;
    }

    public Integer getTaxonomyId() {
        return taxonomyId;
    }

    public String getContig() {
        return contig;
    }

    public Long getStart() {
        return start;
    }

    public String getReference() {
        return reference;
    }

    public String getAlternate() {
        return alternate;
    }

    public String getStudyId() {
        return studyId;
    }

    public String getRemappedFrom() {
        return remappedFrom;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getType() {
        return type;
    }
}