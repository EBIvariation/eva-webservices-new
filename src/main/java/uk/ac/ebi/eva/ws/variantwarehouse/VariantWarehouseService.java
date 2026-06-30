package uk.ac.ebi.eva.ws.variantwarehouse;


import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import uk.ac.ebi.eva.ws.entity.mongo.FileDocument;
import uk.ac.ebi.eva.ws.entity.mongo.VariantDocument;

import java.util.List;
import java.util.Optional;

@Service
public class VariantWarehouseService {

    private final VariantWarehouseTemplateFactory templateFactory;

    public VariantWarehouseService(VariantWarehouseTemplateFactory templateFactory) {
        this.templateFactory = templateFactory;
    }

    public Optional<VariantDocument> findVariant(String taxonomyCode, String assemblyCode,
                                                 String chr, long start, String ref, String alt,
                                                 Long ssId) {
        MongoTemplate template = templateFactory.getTemplate(taxonomyCode, assemblyCode);
        String id = "%s_%d_%s_%s".formatted(chr, start, ref, alt);

        VariantDocument variantDocument = template.findById(id, VariantDocument.class, "variants_2_0");
        if (variantDocument != null) {
            return Optional.of(variantDocument);
        }

        if (ssId != null) {
            Query query = Query.query(Criteria.where("ids").is("ss"+ ssId));
            return Optional.ofNullable(template.findOne(query, VariantDocument.class, "variants_2_0"));
        }

        return Optional.empty();
    }

    public List<FileDocument> findFiles(String taxonomyCode, String assemblyCode, String fileId, String studyId) {
        MongoTemplate template = templateFactory.getTemplate(taxonomyCode, assemblyCode);
        Query query = Query.query(Criteria.where("fid").is(fileId).and("sid").is(studyId));
        return template.find(query, FileDocument.class, "files_2_0");
    }
}