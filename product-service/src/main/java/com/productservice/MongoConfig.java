package com.productservice;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
//@EnableMongoRepositories(basePackages = "org.spring.mongo.demo")
public class MongoConfig {

//    private final List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();

//    @Override
//    protected String getDatabaseName() {
//        return "test";
//    }
//    @Bean
//    public Mongo mongoClientSettings() {
//        //默认空置一个小时重置一次
//        return MongoClientSettings.builder().maxConnectionIdleTime(3600000).build();
//    }


//    @Override
    public MongoClient mongoClient() {
        final ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/product-services");
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToSocketSettings(builder ->
                        builder.connectTimeout(10,TimeUnit.HOURS))
                .build();
        return MongoClients.create(mongoClientSettings);
    }

//    @Override
//    public Collection<String> getMappingBasePackages() {
//        return Collections.singleton("org.spring.mongo.demo");
//    }
}