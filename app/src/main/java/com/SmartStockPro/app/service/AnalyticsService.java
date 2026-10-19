package com.SmartStockPro.app.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class AnalyticsService {
        @Autowired
        private MongoTemplate mongoTemplate;

        public List<Document> getCategoryWiseInventoryReport() {
                // Convert categoryId String → ObjectId so $lookup can match
                // categories._id
                // Spring Data stores @Id String fields as ObjectId in MongoDB — types must
                // match.
                AggregationOperation toObjectIdStage = context -> new Document("$addFields",
                                new Document("categoryObjId", new Document("$toObjectId", "$categoryId")));

                // $lookup using the converted ObjectId field
                AggregationOperation lookupStage = context -> new Document("$lookup", new Document("from", "categories")
                                .append("localField", "categoryObjId")
                                .append("foreignField", "_id")
                                .append("as", "categoryDetails"));

                // Unwind (preserve products with no matched category)
                AggregationOperation unwindStage = context -> new Document("$unwind",
                                new Document("path", "$categoryDetails")
                                                .append("preserveNullAndEmptyArrays", true));

                // $group AFTER lookup — capture resolved categoryName
                AggregationOperation groupStage = context -> new Document("$group", new Document("_id", "$categoryId")
                                .append("categoryName", new Document("$first", new Document("$ifNull",
                                                List.of("$categoryDetails.categoryName", "Uncategorized"))))
                                .append("productCount", new Document("$sum", 1))
                                .append("totalQuantity", new Document("$sum", "$quantity")));

                // Sort by productCount descending
                AggregationOperation sortStage = context -> new Document("$sort", new Document("productCount", -1));

                // Shape the final output
                AggregationOperation projectStage = context -> new Document("$project",
                                new Document("categoryId", "$_id")
                                                .append("_id", 0)
                                                .append("categoryName", 1)
                                                .append("productCount", 1)
                                                .append("totalQuantity", 1));

                TypedAggregation<Document> aggregation = Aggregation.newAggregation(Document.class,
                                toObjectIdStage, lookupStage, unwindStage, groupStage, sortStage, projectStage);

                return mongoTemplate.aggregate(aggregation, "products", Document.class).getMappedResults();
        }

        public List<Document> getMonthlySalesReport() {
                AggregationOperation projectFieldsStage = context -> new Document("$project",
                                new Document("quantitySold", 1)
                                                .append("year", new Document("$year", "$saleDate"))
                                                .append("month", new Document("$month", "$saleDate")));

                AggregationOperation groupStage = context -> new Document("$group",
                                new Document("_id", new Document("year", "$year").append("month", "$month"))
                                                .append("totalQuantitySold", new Document("$sum", "$quantitySold"))
                                                .append("transactionCount", new Document("$sum", 1)));

                AggregationOperation sortStage = context -> new Document("$sort",
                                new Document("_id.year", -1).append("_id.month", -1));
                TypedAggregation<Document> aggregation = Aggregation.newAggregation(Document.class,
                                projectFieldsStage, groupStage, sortStage);
                return mongoTemplate.aggregate(aggregation, "sales", Document.class).getMappedResults();
        }

        public List<Document> getTopSellingProductsReport() {
                //  Group sales by productId and sum quantities
                AggregationOperation groupStage = context -> new Document("$group", new Document("_id", "$productId")
                                .append("totalQuantitySold", new Document("$sum", "$quantitySold")));

                // Convert the grouped String _id (productId) to an ObjectId
                AggregationOperation toObjectIdStage = context -> new Document("$addFields",
                                new Document("productObjId", new Document("$toObjectId", "$_id")));

                // $lookup using the newly converted ObjectId field
                AggregationOperation lookupStage = context -> new Document("$lookup", new Document("from", "products")
                                .append("localField", "productObjId") // Match using the ObjectId
                                .append("foreignField", "_id")
                                .append("as", "productDetails"));

                // Unwind the results
                AggregationOperation unwindStage = context -> new Document("$unwind",
                                new Document("path", "$productDetails")
                                                .append("preserveNullAndEmptyArrays", true));

                // Project fields, mapping $productDetails.name safely
                AggregationOperation projectStage = context -> new Document("$project",
                                new Document("productId", "$_id")
                                                .append("_id", 0)
                                                .append("totalQuantitySold", 1)
                                                .append("productName", new Document("$ifNull",
                                                                List.of("$productDetails.name", "Unknown Product"))));

                AggregationOperation sortStage = context -> new Document("$sort",
                                new Document("totalQuantitySold", -1));
                AggregationOperation limitStage = context -> new Document("$limit", 5);

                // Add the new toObjectIdStage right before the lookupStage
                TypedAggregation<Document> aggregation = Aggregation.newAggregation(Document.class,
                                groupStage, toObjectIdStage, lookupStage, unwindStage, projectStage, sortStage,
                                limitStage);

                return mongoTemplate.aggregate(aggregation, "sales", Document.class).getMappedResults();
        }

        public List<Document> getLowStockReport() {
                AggregationOperation matchStage = context -> new Document("$match", new Document("$expr",
                                new Document("$lt", List.of("$quantity", "$threshold"))));

                // Convert categoryId String → ObjectId to match categories._id type
                AggregationOperation toObjectIdStage = context -> new Document("$addFields",
                                new Document("categoryObjId", new Document("$toObjectId", "$categoryId")));

                AggregationOperation lookupStage = context -> new Document("$lookup", new Document("from", "categories")
                                .append("localField", "categoryObjId")
                                .append("foreignField", "_id")
                                .append("as", "categoryDetails"));

                AggregationOperation unwindStage = context -> new Document("$unwind",
                                new Document("path", "$categoryDetails")
                                                .append("preserveNullAndEmptyArrays", true));

                AggregationOperation projectStage = context -> new Document("$project", new Document("name", 1)
                                .append("quantity", 1)
                                .append("threshold", 1)
                                .append("categoryName", new Document("$ifNull",
                                                List.of("$categoryDetails.categoryName", "Uncategorized"))));

                TypedAggregation<Document> aggregation = Aggregation.newAggregation(Document.class,
                                matchStage, toObjectIdStage, lookupStage, unwindStage, projectStage);

                return mongoTemplate.aggregate(aggregation, "products", Document.class).getMappedResults();
        }

        public List<Document> getExpiringProductsReport() {
                LocalDate targetDate = LocalDate.now().plusDays(30);
                Date targetDateAsDate = Date.from(targetDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

                AggregationOperation matchStage = context -> new Document("$match", new Document("expiryDate",
                                new Document("$lte", targetDateAsDate)));

                // Convert categoryId String → ObjectId to match categories._id type
                AggregationOperation toObjectIdStage = context -> new Document("$addFields",
                                new Document("categoryObjId", new Document("$toObjectId", "$categoryId")));

                AggregationOperation lookupStage = context -> new Document("$lookup", new Document("from", "categories")
                                .append("localField", "categoryObjId")
                                .append("foreignField", "_id")
                                .append("as", "categoryDetails"));

                AggregationOperation unwindStage = context -> new Document("$unwind",
                                new Document("path", "$categoryDetails")
                                                .append("preserveNullAndEmptyArrays", true));

                AggregationOperation projectStage = context -> new Document("$project", new Document("name", 1)
                                .append("quantity", 1)
                                .append("expiryDate", 1)
                                .append("categoryName", new Document("$ifNull",
                                                List.of("$categoryDetails.categoryName", "Uncategorized"))));

                AggregationOperation sortStage = context -> new Document("$sort", new Document("expiryDate", 1));

                TypedAggregation<Document> aggregation = Aggregation.newAggregation(Document.class,
                                matchStage, toObjectIdStage, lookupStage, unwindStage, projectStage, sortStage);

                return mongoTemplate.aggregate(aggregation, "products", Document.class).getMappedResults();
        }
}
