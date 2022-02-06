//package com.weirblog.resource.mongo;
//
//import java.time.LocalDate;
//
//import org.bson.codecs.pojo.annotations.BsonProperty;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//
//import io.quarkus.mongodb.panache.MongoEntity;
//import io.quarkus.mongodb.panache.PanacheMongoEntity;
//
//@MongoEntity(collection = "Person")
//public class Person extends PanacheMongoEntity {
//    @JsonProperty
//    public String name;
//    // will be persisted as a 'birth' field in MongoDB
//    @BsonProperty("birth")
//    public LocalDate birthDate;
//    public String status;
//
//}