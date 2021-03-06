//package com.jenkin.sometools.btsearch.util;
//
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.alibaba.fastjson.util.TypeUtils;
//
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.update.UpdateRequest;
//import org.elasticsearch.common.xcontent.XContentType;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.Operator;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.aggregations.AggregationBuilders;
//import org.elasticsearch.search.aggregations.Aggregations;
//import org.elasticsearch.search.aggregations.BucketOrder;
//import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
//import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
//import org.elasticsearch.search.sort.FieldSortBuilder;
//import org.elasticsearch.search.sort.SortBuilders;
//import org.elasticsearch.search.sort.SortOrder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.data.elasticsearch.core.ResultsExtractor;
//import org.springframework.data.elasticsearch.core.ScrolledPage;
//import org.springframework.data.elasticsearch.core.SearchResultMapper;
//import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
//import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
//import org.springframework.data.elasticsearch.core.query.*;
//
//import java.lang.reflect.Field;
//import java.util.*;
//import java.util.concurrent.*;
//
///**
// * @author jenkin
// * @date 2019/4/24 11:04
// */
//public class EsQuery {
//    private static final String ORDER_ASC = "ASC";
//    private static final String CAUSE_AND = "and";
//    private static final String CAUSE_OR = "or";
//    private static final String LEFT = "(";
//    private static final String RIGHT = ")";
//
//    private static final String CONJUNCTION_AND = "conjunctionAnd";
//    private static final String CONJUNCTION_OR = "conjunctionOr";
//
//
//    private ElasticsearchTemplate elasticsearchTemplate;
//
//    private NativeSearchQueryBuilder builder;
//    private List<Object> qbList = new ArrayList<>();
////    private String sortField;
////    private String sortOrder = ORDER_ASC;
//
//    private LinkedList<FieldSortBuilder> order;
//
//    private QueryBuilder queryBuilder;
//
//    private Logger logger = LoggerFactory.getLogger(EsQuery.class);
//
//    public EsQuery() {
//        logger.debug("?????????ESQUERY");
//    }
//
//    public EsQuery buildEsQuery(ElasticsearchTemplate elasticsearchTemplate) {
//        this.builder = new NativeSearchQueryBuilder();
//        this.elasticsearchTemplate = elasticsearchTemplate;
//        elasticsearchTemplate.putMapping(FileVO.class);
//        return this;
//    }
//
//    /**
//     * ??????????????????AND,??????????????????and()??????or()
//     *
//     * @return this
//     */
//    public EsQuery and() {
//        qbList.add(CAUSE_AND);
//        return this;
//    }
//
//    /**
//     * ??????????????????OR,??????????????????and()??????or()
//     *
//     * @return this
//     */
//    public EsQuery or() {
//        qbList.add(CAUSE_OR);
//        return this;
//    }
//
//    /**
//     * ???????????????AND??????,??????????????????conjunctionAnd()??????conjunctionOr()
//     *
//     * @return this
//     */
//    public EsQuery conjunctionAnd() {
//        qbList.add(CONJUNCTION_AND);
//        return this;
//    }
//
//    /**
//     * ???????????????OR??????,??????????????????conjunctionAnd()??????conjunctionOr()
//     *
//     * @return this
//     */
//    public EsQuery conjunctionOr() {
//        qbList.add(CONJUNCTION_OR);
//        return this;
//    }
//
//
//    public EsQuery groupBegin() {
//        qbList.add(LEFT);
//        return this;
//    }
//
//    public EsQuery groupEnd() {
//        qbList.add(RIGHT);
//        return this;
//    }
//
//    /**
//     * ???????????????????????? k = v
//     *
//     * @return this
//     */
//    public EsQuery eq(String field, Object value) {
//        if (value == null||"".equals(value)) {
//            return this;
//        }
//
//        if ( !TypeUtils.isNumber (String.valueOf(value))) {
//            field += ".keyword";
//        }
//        qbList.add(QueryBuilders.termQuery(field, formatValue(value)));
//        return this;
//    }
//
//    /**
//     * ????????????????????? k != v
//     *
//     * @return this
//     */
//    public EsQuery ne(String field, Object value) {
//        if (value == null || "".equals(value)) {
//            return this;
//        }
//        if ( !TypeUtils.isNumber  (String.valueOf(value))) {
//            field += ".keyword";
//        }
//        qbList.add(QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery(field, formatValue(value))));
//        return this;
//    }
//
//    /**
//     * ????????????
//     *
//     * @return this
//     */
//    public EsQuery mq(String field, Object value) {
//        if (value == null || "".equals(value)) {
//            return this;
//        }
//        qbList.add(QueryBuilders.matchQuery(field, formatValue(value)).operator(Operator.AND));
//        return this;
//    }
//    /**
//     * ????????????
//     *
//     * @return this
//     */
//    public EsQuery pmq(String field, Object value) {
//        if (value == null || "".equals(value)) {
//            return this;
//        }
//        qbList.add(QueryBuilders.matchPhraseQuery(field, formatValue(value)));
//        return this;
//    }
//    /**
//     * ???????????? (?????????)
//     *
//     * @return this
//     */
//    public EsQuery notMq(String field, Object value) {
//        if (value == null || "".equals(value)) {
//            return this;
//        }
//        qbList.add(QueryBuilders.boolQuery().mustNot(QueryBuilders.matchQuery(field, formatValue(value))));
//        return this;
//    }
//
//    /**
//     * ?????????????????? (?????????????????????????????????)
//     *
//     * @return this
//     */
//    public EsQuery like(String field, Object value) {
//        if (value == null || "".equals(value)) {
//            return this;
//        }
//        wildcard(field, "*" + String.valueOf(value).toLowerCase() + "*");
//        return this;
//    }
//    /**
//     * ??????????????? (?????????????????????????????????)
//     *
//     * @return this
//     */
//    public EsQuery termLike(String field, Object value) {
//        if (value == null || "".equals(value)) {
//            return this;
//        }
//        wildcard(field+".case_text", "*" + String.valueOf(value).toLowerCase() + "*");
//        return this;
//    }
//
//    /**
//     * ???????????? ????????? (?????????????????????????????????)
//     *
//     * @return this
//     */
//    public EsQuery notLike(String field, String value) {
//        if (value == null || "".equals(value)) {
//            return this;
//        }
//        qbList.add(QueryBuilders.boolQuery().mustNot(QueryBuilders.wildcardQuery(field, "*" + value + "")));
//        return this;
//    }
//
//
//    /**
//     * ????????????
//     * <p>
//     * ??????????????????????????????????????????????????????????????????????????? foo ??? .foo ????????????????????????
//     *
//     * @param field ?????????
//     * @param regex ???????????????
//     * @return this
//     */
//    public EsQuery regex(String field, String regex) {
//        qbList.add(QueryBuilders.regexpQuery(field, regex));
//        return this;
//    }
//
//    /**
//     * ???????????????
//     * <p>
//     * ??????????????????????????????????????????????*??????0??????????????????.????????????????????????
//     * ??????????????????????????????????????????????????????????????????????????? foo ??? .foo ????????????????????????
//     *
//     * @param field ?????????
//     * @param regex ???????????????
//     * @return this
//     */
//    public EsQuery wildcard(String field, String regex) {
//        qbList.add(QueryBuilders.wildcardQuery(field, regex));
//        return this;
//    }
//
//    /**
//     * ?????????????????????
//     *
//     * @return this
//     */
//    public EsQuery startWith(String field, String value) {
//        if (value == null || "".equals(value)) {
//            return this;
//        }
//        wildcard(field, value + "*");
//        return this;
//    }
//
//    /**
//     * ????????????????????? (???????????????)
//     *
//     * @return this
//     */
//    public EsQuery endWith(String field, String value) {
//        if (value == null || "".equals(value)) {
//            return this;
//        }
//        wildcard(field, "*" + value);
//        return this;
//    }
//
//
//    /**
//     * ?????????null
//     *
//     * @return this
//     */
//    public EsQuery isNull(String field) {
//        qbList.add(QueryBuilders.existsQuery(field));
//        return this;
//    }
//
//    /**
//     * ????????????null
//     *
//     * @return this
//     */
//    public EsQuery isNotNull(String field) {
//        qbList.add(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(field)));
//        return this;
//    }
//
//    /**
//     * ?????????????????????
//     *
//     * @return this
//     */
//    public EsQuery gt(String field, Number value) {
//        if (value == null) {
//            return this;
//        }
//        qbList.add(QueryBuilders.rangeQuery(field).gt(getLongValue(value)));
//        return this;
//    }
//
//    /**
//     * ???????????????????????????
//     *
//     * @return this
//     */
//    public EsQuery gteq(String field, Number value) {
//        if (value == null) {
//            return this;
//        }
//        qbList.add(QueryBuilders.rangeQuery(field).gte(getLongValue(value)));
//        return this;
//    }
//
//    /**
//     * ?????????????????????
//     *
//     * @return this
//     */
//    public EsQuery lt(String field, Number value) {
//        if (value == null) {
//            return this;
//        }
//        qbList.add(QueryBuilders.rangeQuery(field).lt(getLongValue(value)));
//        return this;
//    }
//
//    /**
//     * ???????????????????????????
//     *
//     * @return this
//     */
//    public EsQuery lteq(String field, Number value) {
//        if (value == null) {
//            return this;
//        }
//        qbList.add(QueryBuilders.rangeQuery(field).lte(getLongValue(value)));
//        return this;
//    }
//
//    /**
//     * ???????????????start - end ???????????????
//     *
//     * @return this
//     */
//    public EsQuery between(String field, Object start, Object end) {
//        Long startNum = 0L;
//        Long endNum = 0L;
//        if (start == null || end == null) {
//            return this;
//        } else {
//            try {
//                startNum = getLongValue(start);
//                endNum = getLongValue(end);
//            } catch (Exception e) {
//                throw new MpaasBusinessException("????????????????????????????????????????????????????????????????????????????????????");
//            }
//        }
//        qbList.add(QueryBuilders.rangeQuery(field).from(startNum).to(endNum));
//        return this;
//    }
//
//    /**
//     * ???????????????long
//     *
//     * @param o
//     * @return
//     */
//    private Long getLongValue(Object o) {
//        Long num = 0L;
//        try {
//            num = (o instanceof Date) ? ((Date) o).getTime() : TypeUtils.castToLong(o);
//        } catch (Exception e) {
//            throw new MpaasBusinessException("????????????????????????????????????????????????????????????????????????????????????");
//        }
//        return num;
//    }
//
//    /**
//     * ????????????
//     *
//     * @return this
//     */
//    public EsQuery in(String field, Iterable list) {
//        return ins(field, list, true);
//    }
//
//    /**
//     * ???????????? ?????????
//     *
//     * @return this
//     */
//    public EsQuery notIn(String field, Iterable  list) {
//        return ins(field, list, false);
//    }
//
//    private EsQuery ins(String field, Iterable<Object> list, boolean in) {
//        if (list == null) {
//            return this;
//        }
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//         field=field+".keyword";
//        for (Object o : list) {
//
//            if (in) {
//                boolQueryBuilder.should(QueryBuilders.termQuery(field, formatValue(o)));
//            } else {
//                boolQueryBuilder.mustNot(QueryBuilders.termQuery(field, formatValue(o)));
//            }
//
//        }
//        qbList.add(boolQueryBuilder);
//        return this;
//    }
//
//
//    /**
//     * ??????
//     *
//     * @return this
//     */
//    public EsQuery order(String field, String order) {
//        this.order.add(SortBuilders.fieldSort(field).order(SortOrder.fromString(order)));
//        return this;
//    }
//
//    /**
//     * ??????
//     *
//     * @return this
//     */
//    public EsQuery order(LinkedHashMap<String, String> map) {
//        for (String field : map.keySet()) {
//            this.order.add(SortBuilders.fieldSort(field).order(SortOrder.fromString(map.get(field))));
//        }
//        return this;
//    }
//
//    /**
//     * ??????
//     *
//     * @return this
//     */
//    public EsQuery order(Sort sort) {
//        Iterator<Sort.Order> iterator = sort.iterator();
//        while (iterator.hasNext()) {
//            Sort.Order next = iterator.next();
//            this.order.add(SortBuilders.fieldSort(next.getProperty()).order(SortOrder.fromString(next.getDirection().name())));
//        }
//        return this;
//    }
//
//
//    /**
//     * ????????????
//     *
//     * @return ????????????
//     */
//    public <T> List<T> doQuery(Class<T> clazz) {
//        brackets(qbList);
//        logger.debug("????????????====>" + this.queryBuilder.toString());
//        return elasticsearchTemplate.queryForList(builder.build(), clazz);
//    }
//
//    /**
//     * ??????????????????
//     *
//     * @return ????????????
//     */
//    public <T> T doQueryFirst(Class<T> clazz) {
//        builder.withPageable(PageRequest.of(0, 1));
//
//        List<T> content = doQuery(clazz);
//        if (content.size() > 1) {
//            throw new RuntimeException("????????????????????????????????????????????????");
//        } else if (content.size() == 0) {
//            return null;
//        }
//        return content.get(0);
//    }
//
//
//    /**
//     * ??????????????????
//     *
//     * @return ????????????
//     */
//    public <T> Page<T> doPageQuery(Integer page, Integer pageSize, Class<T> clazz) {
//        if (page > 0) {
//            page--;
//        } else {
//            page = 0;
//        }
//        brackets(qbList);
//        builder.withPageable(PageRequest.of(page, pageSize));
//        logger.debug("??????es??????=====>" + queryBuilder);
//        return elasticsearchTemplate.queryForPage(builder.build(), clazz);
//    }
//
//    /**
//     * ???????????????????????????
//     *
//     * @return ????????????
//     */
//    public <T> Page<T> doPageSortQuery(PageRequest page, Class<T> clazz) {
//
//        brackets(qbList);
//        builder.withPageable(page);
//        logger.debug("??????es??????=====>" + queryBuilder);
//        return elasticsearchTemplate.queryForPage(builder.build(), clazz);
//    }
//
//    /**
//     * ?????????es?????????value???
//     *
//     * @param value
//     * @return
//     */
//    protected Object formatValue(Object value) {
//        if (value instanceof Date) {
//            return ((Date) value).getTime();
//        } else {
//            return value;
//        }
//    }
//
//
//    /**
//     * ????????????
//     * ???????????????
//     * () and ()
//     * (xxxx and (yyyy) => xxxx and (yyyy)
//     * xxxx) and ( yyyy ) => xxxx and (yyyy)
//     *
//     * @param list ????????????
//     */
//    private void brackets(List<Object> list, boolean... noOrder) {
//
//        Integer endLeft = null;
//
//        for (int i = 0; i < list.size(); i++) {
//            Object item = list.get(i);
//            if (item instanceof String) {
//                if (LEFT.equals(item)) {
//                    endLeft = i;
//                } else if (RIGHT.equals(item)) {
//                    if (endLeft == null) {
//                        continue;
//                    }
//                    List<Object> tempStart = new ArrayList<>();
//                    List<Object> tempEnd = new ArrayList<>();
//
//                    List<Object> qbList3 = new ArrayList<>();
//                    for (int i1 = 0; i1 < list.size(); i1++) {
//                        if (i1 >= endLeft && i1 <= i) {
//                            qbList3.add(list.get(i1));
//                        } else if (i1 < endLeft) {
//                            tempStart.add(list.get(i1));
//                        } else {
//                            tempEnd.add(list.get(i1));
//                        }
//                    }
//                    BoolQueryBuilder query = findQuery(qbList3, QueryBuilders.boolQuery());
//                    tempStart.add(query);
//                    tempStart.addAll(tempEnd);
//                    brackets(tempStart);
//                    return;
//                }
//            }
//        }
//
//        if (noOrder.length > 0 && noOrder[0] && order.size() > 0) {
//            for (FieldSortBuilder fieldSortBuilder : order) {
//                builder.withSort(fieldSortBuilder);
//            }
//        }
//        queryBuilder = findQuery(list, QueryBuilders.boolQuery());
//        builder.withQuery(queryBuilder);
//    }
//
//
//    /**
//     * ??????????????????
//     */
//    private BoolQueryBuilder findQuery(List<Object> list, BoolQueryBuilder boolQueryBuilder) {
//        List<Object> qbList2 = new ArrayList<>();
//
//        String condition = CAUSE_AND;
//        for (Object o : list) {
//            if (o instanceof String) {
//                if (CONJUNCTION_AND.equals(o)) {
//                    qbList2.add(boolQueryBuilder);
//                    boolQueryBuilder = QueryBuilders.boolQuery();
//                } else if (CONJUNCTION_OR.equals(o)) {
//                    qbList2.add(CAUSE_OR);
//                    qbList2.add(boolQueryBuilder);
//                    boolQueryBuilder = QueryBuilders.boolQuery();
//                } else {
//                    condition = (String) o;
//                }
//            } else {
//                if (CAUSE_AND.equals(condition)) {
//                    boolQueryBuilder.must((QueryBuilder) o);
//                } else if (CAUSE_OR.equals(condition)) {
//                    boolQueryBuilder.should((QueryBuilder) o);
//                }
//            }
//        }
//
//        if (qbList2.size() > 0) {
//            qbList2.add(boolQueryBuilder);
//            return findQuery(qbList2, QueryBuilders.boolQuery());
//        }
//
//        return boolQueryBuilder;
//
//    }
//
//    /**
//     * ??????????????????
//     */
//    private void generateQuery(List<Object> list, BoolQueryBuilder queryBuilder) {
//        List<Object> qbList2 = new ArrayList<>();
//
//        String condition = CAUSE_AND;
//        BoolQueryBuilder bq = new BoolQueryBuilder();
//        for (int i = 0; i < list.size(); i++) {
//            Object item = list.get(i);
//
//
//            if (CAUSE_OR.equals(item)) {
//                i++;
//                item = list.get(i);
//                if (item instanceof BoolQueryBuilder) {
//                    bq = bq.should((BoolQueryBuilder) item);
//                }
//            } else if (CAUSE_AND.equals(item)) {
//                i++;
//                item = list.get(i);
//                if (item instanceof BoolQueryBuilder) {
//                    bq = bq.must((BoolQueryBuilder) item);
//                }
//            } else if (CONJUNCTION_AND.equals(item)) {
//                BoolQueryBuilder bqand = new BoolQueryBuilder();
//
//                bqand.must(bq);
//
//            } else {
//                if (item instanceof BoolQueryBuilder) {
//                    bq = bq.must((BoolQueryBuilder) item);
//                }
//            }
//
//            if (CONJUNCTION_AND.equals(item)) {
//                BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//                boolQueryBuilder.should();
//            }
//
//
//        }
//
//    }
//
//
//    /**
//     * ????????????
//     */
//    public <T> int doInsert(T t) {
//        ArrayList<Object> objects = new ArrayList<>();
//        objects.add(t);
//        return doInsert(objects);
//    }
//
//    /**
//     * ??????????????????
//     */
//    public <T> int doInsert(Iterable<T> list) {
//        Class<?> clazz = getClass(list);
//        if (clazz == null) {
//            return 0;
//        }
//        int counter = 0;
//        try {
//            ESDocument esDocument = getESDocument(clazz);
//
//            //????????????????????????
////            if (!elasticsearchTemplate.indexExists(esDocument.getIndexName())) {
////                elasticsearchTemplate.createIndex(esDocument.getIndexName());
////            }
//
//            List queries = new ArrayList();
//            for (T t : list) {
//                IndexQuery index = new IndexQueryBuilder().withId(getIdStr(esDocument.getIdField(), t)).withObject(t).build();
//                queries.add(index);
//                //??????????????????
//                if (counter > 0 && counter % 500 == 0) {
//                    elasticsearchTemplate.bulkIndex(queries);
//                    queries.clear();
//                }
//                counter++;
//            }
//            //??????????????????????????????????????????
//            if (queries.size() > 0) {
//                elasticsearchTemplate.bulkIndex(queries);
//            }
//            elasticsearchTemplate.refresh(esDocument.getIndexName());
//            logger.debug("bulkIndex completed ==>" + counter);
//            return counter;
//        } catch (Exception e) {
//            logger.debug("IndexerService.bulkIndex e;" + e.getMessage());
//            throw e;
//        }
//    }
//
//    /**
//     * ??????Id????????????
//     * ??????id?????????
//     */
//    public <T> void doDeleteByIds(Collection ids,Class clazz) {
//        if (ids!=null) {
//            ids.forEach(item->{
//                elasticsearchTemplate.delete(clazz, String.valueOf(item));
//                refresh(clazz);
//            });
//        }
//
//    }
//    /**
//     * ??????Id????????????
//     * ??????id?????????
//     */
//    public <T> void doDeleteByIdsThread(Collection ids,Class clazz) {
//        int singleSize = 500;
//        int maxThread = 40;
//        int coreSize = 20;
//        List allId = new ArrayList(ids);
//        int size = allId.size();
//        int num = size / singleSize;
//        int mod = size%singleSize;
//        num=num+(mod>0?1:0);
//        List<List> allIdList = CommonUtils.splitList(allId, num);
//        final CountDownLatch count = new CountDownLatch(allIdList.size());
//        ExecutorService executorService = new ThreadPoolExecutor(coreSize,maxThread,120, TimeUnit.SECONDS,
//                new ArrayBlockingQueue<>(1000),new ThreadPoolExecutor.AbortPolicy());
//        try {
//            for (List idList : allIdList) {
//                executorService.submit(()->{
//                    doDeleteByIds(idList,clazz);
//                    count.countDown();
//                });
//            }
//            count.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            executorService.shutdown();
//        }
//
//    }
//    /**
//     * ??????Id????????????
//     * ??????id?????????
//     */
//    public  void doDeleteById(Object id,Class clazz) {
//        elasticsearchTemplate.delete(clazz, String.valueOf(id));
//        refresh(clazz);
//    }
//
//    /**
//     * ??????Id????????????
//     * ??????id?????????
//     */
//    public <T> void doDeleteById(T t) {
//        ESDocument esDocument = getESDocument(t.getClass());
//        String idStr;
//        if (esDocument.getIdField() == null) {
//            idStr = t.toString();
//        } else {
//            idStr = getIdStr(esDocument.getIdField(), t);
//        }
//        elasticsearchTemplate.delete(t.getClass(), idStr);
//        refresh(t.getClass());
//    }
//
//    /**
//     * ????????????????????????
//     */
//    public <T> void doDelete(Class<T> clazz) {
//        DeleteQuery deleteQuery = new DeleteQuery();
//        brackets(qbList);
//        deleteQuery.setQuery(queryBuilder);
//        elasticsearchTemplate.delete(deleteQuery, clazz);
//        refresh(clazz);
//    }
//
//    private <T> void refresh(Class<T> clazz) {
//        ESDocument esDocument = getESDocument(clazz);
//        elasticsearchTemplate.refresh(esDocument.getIndexName());
//    }
//
//    /**
//     * ??????Id????????????????????????
//     */
//    public <T> void doUpdateById(T t) {
//        ArrayList<Object> objects = new ArrayList<>();
//        objects.add(t);
//        upDate(objects, false);
//    }
//
//    /**
//     * ??????Id????????????????????????
//     */
//    public <T> int doUpdateById(Iterable<T> list) {
//        return upDate(list, false);
//    }
//
//    /**
//     * ??????Id???????????????????????????
//     */
//    public <T> int doUpdateNotNullById(Iterable<T> list) {
//        return upDate(list, true);
//    }
//
//    /**
//     * ??????Id???????????????????????????
//     */
//    public <T> void doUpdateNotNullById(T t) {
//        ArrayList<Object> objects = new ArrayList<>();
//        objects.add(t);
//        upDate(objects, true);
//    }
//
//    /**
//     * ????????????????????????null????????????
//     */
//    public <T> void doUpdateNotNull(T t) {
//        doUpdate(t, true);
//    }
//
//    /**
//     * ????????????????????????????????????
//     */
//    public <T> void doUpdate(T t) {
//        doUpdate(t, false);
//    }
//
//    private <T> void doUpdate(T t, boolean notNullField) {
//        // ????????????
//        brackets(qbList);
//        upDateByPage(queryBuilder, t, notNullField);
//
//    }
//
//    private <T> int upDateByPage(QueryBuilder queryBuilder, T t, boolean notNullField) {
//        ESDocument esDocument = getESDocument(t.getClass());
//
//        String indexName = esDocument.getIndexName();
//        String typeName = esDocument.getType();
//        Integer pageSize = 1000;
//        Long scrollTimeInMillis = 10000L;
//
//        builder.withQuery(queryBuilder).withIndices(indexName).withTypes(typeName).withPageable(PageRequest.of(0, pageSize)).build();
//
////        SearchQuery searchQuery = (new NativeSearchQueryBuilder()).withQuery(queryBuilder).withIndices(indexName).withTypes(typeName).withPageable(PageRequest.of(page, pageSize)).build();
//        SearchResultMapper onlyIdResultMapper = new SearchResultMapper() {
//            @Override
//            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
//                List<String> result = new ArrayList<>();
//                SearchHit[] var5 = response.getHits().getHits();
//                int var6 = var5.length;
//
//                for (int var7 = 0; var7 < var6; ++var7) {
//                    SearchHit searchHit = var5[var7];
//                    String id = searchHit.getId();
//                    result.add(id);
//                }
//
//                return result.size() > 0 ? new AggregatedPageImpl(result, response.getScrollId()) : new AggregatedPageImpl(Collections.EMPTY_LIST, response.getScrollId());
//            }
//        };
//        Page<String> scrolledResult = elasticsearchTemplate.startScroll(scrollTimeInMillis, (SearchQuery) builder, String.class, onlyIdResultMapper);
//        List<String> ids = new ArrayList<>();
//
//        do {
//            ids.addAll(scrolledResult.getContent());
//            scrolledResult = elasticsearchTemplate.continueScroll(((ScrolledPage) scrolledResult).getScrollId(), scrollTimeInMillis, String.class, onlyIdResultMapper);
//        } while (scrolledResult.getContent().size() != 0);
//
//        Iterator<String> var12 = ids.iterator();
//
//        HashMap<String, String> map = new HashMap<>();
//
//        while (var12.hasNext()) {
//            try {
//                Object id = getStringToId(var12.next(), esDocument.getIdField().getType());
//                esDocument.getIdField().set(t, id);
//
//                String docJson = ObjToJson(notNullField, t);
//                map.put(id + "", docJson);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//        upDate(map, t.getClass());
//        return scrolledResult.getTotalPages();
//    }
//
//    private int upDate(Map<String, String> map, Class<?> clazz) {
//        int counter = 0;
//        try {
//            ESDocument esDocument = getESDocument(clazz);
//
//            //????????????????????????
//            if (!elasticsearchTemplate.indexExists(esDocument.getIndexName())) {
//                throw new RuntimeException("ES???????????????!");
//            }
//
//            List<UpdateQuery> queries = new ArrayList();
//            for (String id : map.keySet()) {
//
//                UpdateQuery updateQuery = new UpdateQuery();
//                updateQuery.setIndexName(esDocument.getIndexName());
//                updateQuery.setType(esDocument.getType());
//                updateQuery.setId(id);
//                UpdateRequest doc = new UpdateRequest(
//                        updateQuery.getIndexName(),
//                        updateQuery.getType(),
//                        updateQuery.getId()).doc(map.get(id), XContentType.JSON);
//                updateQuery.setUpdateRequest(doc);
//                updateQuery.setDoUpsert(true);
//                updateQuery.setClazz(clazz);
//                queries.add(updateQuery);
//                //??????????????????
//                if (counter % 500 == 0) {
//                    elasticsearchTemplate.bulkUpdate(queries);
//                    queries.clear();
//                }
//                counter++;
//            }
//            //??????????????????????????????????????????
//            if (queries.size() > 0) {
//                elasticsearchTemplate.bulkUpdate(queries);
//            }
//            elasticsearchTemplate.refresh(esDocument.getIndexName());
//            logger.debug("bulkIndex completed ==>" + counter);
//            return counter;
//        } catch (Exception e) {
//            logger.debug("IndexerService.bulkIndex e;" + e.getMessage());
//            throw e;
//        }
//    }
//
//    private <T> int upDate(Iterable<T> list, boolean notNullField) {
//
//        Map<String, String> stringStringMap = new HashMap<>();
//        Class<?> clazz = getClass(list);
//        if (clazz == null) {
//            return 0;
//        }
//        try {
//            ESDocument esDocument = getESDocument(clazz);
//
//            //????????????????????????
//            if (!elasticsearchTemplate.indexExists(esDocument.getIndexName())) {
//                throw new RuntimeException("ES???????????????!");
//            }
//
//            for (T t : list) {
//                String docJson = ObjToJson(notNullField, t);
//                stringStringMap.put(getIdStr(esDocument.getIdField(), t), docJson);
//            }
//            return upDate(stringStringMap, clazz);
//        } catch (Exception e) {
//            logger.debug(e.getMessage());
//            throw e;
//        }
//    }
//
//    /**
//     * ??????
//     *
//     * @param field ???????????????
//     * @return
//     */
//    public Object groupBy(String field, Class<?> clazz) {
//        ESDocument esDocument = getESDocument(clazz);
//
//        String indexName = esDocument.getIndexName();
//        String typeName = esDocument.getType();
//
//        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms(indexName)
//                .field(field).size(1000000000);
//
//        if (order.size() > 0) {
//            List<BucketOrder> list = new LinkedList<>();
//            for (FieldSortBuilder fieldSortBuilder : order) {
//                list.add(BucketOrder.aggregation(fieldSortBuilder.getFieldName(), ORDER_ASC.equalsIgnoreCase(fieldSortBuilder.order().name())));
//            }
//            termsAggregationBuilder.order(list);
//        }
//        brackets(qbList, true);
//        System.out.println(queryBuilder);
//        builder
//                .withIndices(indexName)
//                .withTypes(typeName)
//                .addAggregation(termsAggregationBuilder).build();
//        Aggregations aggregations = elasticsearchTemplate.query(builder.build(),
//                new ResultsExtractor<Aggregations>() {
//                    @Override
//                    public Aggregations extract(SearchResponse response) {
//                        return response.getAggregations();
//                    }
//                });
//        StringTerms aggregation = aggregations.get(indexName);
//        List<StringTerms.Bucket> buckets = aggregation.getBuckets();
//        Map<String, Object> map = new LinkedHashMap<>();
//        for (StringTerms.Bucket bucket : buckets) {
//            long docCount = bucket.getDocCount();
//            map.put(bucket.getKeyAsString(), docCount);
//        }
//        return map;
//    }
//
//    private <T> String ObjToJson(boolean notNullField, T t) {
//        if (notNullField) {
//            return JSONObject.toJSONString(t);
//        } else {
//            return JSONObject.toJSONString(t, SerializerFeature.WriteMapNullValue);
//        }
//    }
//
//    private ESDocument getESDocument(Class<?> clazz) {
//        ESDocument esDocument = new ESDocument();
//
//        esDocument.setIndexName(elasticsearchTemplate.getPersistentEntityFor(clazz).getIndexName());
//        esDocument.setType(elasticsearchTemplate.getPersistentEntityFor(clazz).getIndexType());
//        // ???????????????/????????????
//        Document annotation = clazz.getAnnotation(Document.class);
//        if (annotation != null) {
//            esDocument.setIndexName(annotation.indexName());
//            esDocument.setType(annotation.type());
//        }
//        for (Field field : clazz.getDeclaredFields()) {
//            if (field.isAnnotationPresent(Id.class)) {
//                field.setAccessible(true);
//                esDocument.setIdField(field);
//                break;
//            }
//        }
//        return esDocument;
//    }
//
//    private <T> String getIdStr(Field field, T t) {
//        try {
//            Object o = field.get(t);
//            if (o != null) {
//                return o.toString();
//            }
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return "";
//
//    }
//
//    private <T> Class<?> getClass(Iterable<T> list) {
//        for (T t : list) {
//            return t.getClass();
//        }
//        return null;
//    }
//
//
//    private Object getStringToId(String id, Class<?> clazz) {
//        if (clazz == String.class) {
//            return id;
//        } else if (clazz == Integer.class) {
//            return Integer.parseInt(id);
//        } else if (clazz == Long.class || clazz == long.class) {
//            return Long.parseLong(id);
//        } else if (clazz == Short.class || clazz == short.class) {
//            return Short.parseShort(id);
//        }
//        return id;
//    }
//
//
//}