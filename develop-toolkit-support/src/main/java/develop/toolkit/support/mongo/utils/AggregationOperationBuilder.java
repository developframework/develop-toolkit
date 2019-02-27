package develop.toolkit.support.mongo.utils;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * AggregationOperation构建器
 *
 * @author qiushui on 2019-02-25.
 */
public class AggregationOperationBuilder {

    private final static String REF_SUFFIX = "Id";

    private List<AggregationOperation> aggregationOperations;

    public AggregationOperationBuilder() {
        this.aggregationOperations = new LinkedList<>();
    }

    public AggregationOperationBuilder(List<AggregationOperation> aggregationOperations) {
        this.aggregationOperations = new LinkedList<>(aggregationOperations);
    }

    /**
     * 构建
     *
     * @return
     */
    public List<AggregationOperation> build() {
        return aggregationOperations;
    }

    /**
     * 合并
     *
     * @param otherAggregationOperations
     * @return
     */
    public AggregationOperationBuilder merge(List<AggregationOperation> otherAggregationOperations) {
        aggregationOperations.addAll(otherAggregationOperations);
        return this;
    }

    /**
     * 关联
     *
     * @param localField
     * @param lookupAs
     * @param foreignDocClass
     * @param joinType
     * @return
     */
    public AggregationOperationBuilder join(String localField, String lookupAs, Class<?> foreignDocClass, JoinType joinType) {

        /*

         等价于
         {$addFields:{"fieldId":{$let:{vars:{myVar:{$arrayElemAt:[{$objectToArray:"$field"},1]}},in:"$$myVar.v"}}}},
         {$lookup:{from: "foreignDocClass", localField:"fieldId", foreignField:"_id", as:"lookupAs"}},
         {$unwind: {path: "$lookupAs", preserveNullAndEmptyArrays: true}}

         */

        final String localFieldId = localField + REF_SUFFIX;
        final String from = AggregationOperationUtils.collectionNameFormDocumentAnnotation(foreignDocClass);
        aggregationOperations.add(AggregationOperationUtils.addRefFields(localFieldId, localField, joinType));
        aggregationOperations.add(Aggregation.lookup(from, localFieldId, "_id", lookupAs));
        aggregationOperations.add(Aggregation.unwind(lookupAs, true));
        return this;
    }

    /**
     * 过滤
     *
     * @param query
     * @return
     */
    public AggregationOperationBuilder match(Query query) {
        aggregationOperations.add(context -> new Document("$match", query.getQueryObject()));
        return this;
    }

    /**
     * 添加字段
     *
     * @param expressions
     * @return
     */
    public AggregationOperationBuilder addFields(String... expressions) {
        aggregationOperations.add(AggregationOperationUtils.addFields(expressions));
        return this;
    }

    /**
     * 整理字段
     *
     * @param fields
     * @return
     */
    public AggregationOperationBuilder project(String... fields) {
        aggregationOperations.add(AggregationOperationUtils.project(fields));
        return this;
    }

    /**
     * 复杂AggregationOperation
     *
     * @param aggregationOperations
     * @return
     */
    public AggregationOperationBuilder complex(AggregationOperation... aggregationOperations) {
        this.aggregationOperations.addAll(Arrays.asList(aggregationOperations));
        return this;
    }
}
