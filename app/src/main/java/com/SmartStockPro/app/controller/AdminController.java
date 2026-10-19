package com.SmartStockPro.app.controller;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/replication")
    public ResponseEntity<Map<String, Object>> getReplicationStatus() {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            Document status = mongoTemplate.getDb().runCommand(new Document("replSetGetStatus", 1));
            response.put("status", "SUCCESS");
            response.put("replicaSetDetails", status);
        } catch (Exception e) {
            response.put("status", "STANDALONE / NO REPLICA SET CONFIGURED");
            response.put("message", "The database is running in Standalone mode. Replica Sets require starting MongoDB with --replSet config.");
            response.put("error", e.getMessage());
            response.put("replicationConcept", Map.of(
                "primaryNode", "Handles all write operations and logs them to oplog.",
                "secondaryNodes", "Replicate primary's oplog and apply operations asynchronously. Used for read scaling & failover.",
                "failoverProcess", "If primary goes down, an election is held among secondaries to choose a new primary automatically."
            ));
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/sharding")
    public ResponseEntity<Map<String, Object>> getShardingStatus() {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            Document status = mongoTemplate.getDb().runCommand(new Document("listShards", 1));
            response.put("status", "SUCCESS");
            response.put("shardsDetails", status);
        } catch (Exception e) {
            response.put("status", "NOT SHARDED");
            response.put("message", "The database is not part of a Sharded cluster. Sharding requires a Config Server, Query Routers (mongos), and Shard Nodes.");
            response.put("error", e.getMessage());
            response.put("shardingConcept", Map.of(
                "shardKey", "categoryId (distributes products to shards based on category grouping)",
                "horizontalScaling", "Splits data across multiple replica set shards to share read/write load.",
                "mongos", "Routers that direct client API queries to the correct shard based on the shard key."
            ));
        }
        return ResponseEntity.ok(response);
    }
}
