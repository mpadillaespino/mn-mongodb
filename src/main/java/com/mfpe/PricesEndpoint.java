package com.mfpe;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.reactivex.Flowable;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/prices")
public class PricesEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(PricesEndpoint.class);
    private final MongoClient mongoClient;

    public PricesEndpoint(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }
    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("prices").getCollection("example");
    }

    @Get("/")
    public Flowable<Document> fetch(){
        var collection = getCollection();
        return Flowable.fromPublisher(collection.find());
    }

    @Post("/")
    public Flowable<InsertOneResult> insert(@Body ObjectNode json){
        var collection = getCollection();
        Document document = Document.parse(json.toString());
        LOG.info("Insert {}", document);
        return Flowable.fromPublisher(collection.insertOne(document));
    }

}
