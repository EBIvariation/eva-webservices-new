package uk.ac.ebi.eva.ws.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "uk.ac.ebi.eva.ws.repository.mongo")
public class MongoConfiguration {

    @Bean
    public MongoClient mongoClient(@Value("${eva.mongo.uri}") String mongoUri) {
        return MongoClients.create(mongoUri);
    }

    @Bean
    public MongoDatabaseFactory accessioningMongoDatabaseFactory(
            MongoClient mongoClient,
            @Value("${eva.mongo.accessioning-database}") String accessioningDatabase) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, accessioningDatabase);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory accessioningMongoDatabaseFactory) {
        return new MongoTemplate(accessioningMongoDatabaseFactory);
    }
}
