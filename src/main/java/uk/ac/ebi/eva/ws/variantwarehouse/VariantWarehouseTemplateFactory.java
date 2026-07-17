package uk.ac.ebi.eva.ws.variantwarehouse;

import com.mongodb.client.MongoClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class VariantWarehouseTemplateFactory {

    private final MongoClient mongoClient;
    private final ConcurrentMap<String, MongoTemplate> templateCache = new ConcurrentHashMap<>();

    public VariantWarehouseTemplateFactory(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public MongoTemplate getTemplate(String taxonomyCode, String assemblyCode) {
        String dbName = "eva_%s_%s".formatted(taxonomyCode, assemblyCode);
        return templateCache.computeIfAbsent(dbName, db -> new MongoTemplate(mongoClient, db));
    }
}
