package com.jenkin.sometools.btsearch.util;

import java.util.List;
import java.util.Map;
/**
 * @author jenkin
 * @date 2019/4/24 11:04
 */
public class Group {
    /**
     * "doc_count_error_upper_bound": 0,
     *     "sum_other_doc_count": 0,
     *     "buckets"
     */
    private Long doc_count_error_upper_bound;
    private Long sum_other_doc_count;
    private List<Map<Object, Object>> buckets;

    public Long getDoc_count_error_upper_bound() {
        return doc_count_error_upper_bound;
    }

    public void setDoc_count_error_upper_bound(Long doc_count_error_upper_bound) {
        this.doc_count_error_upper_bound = doc_count_error_upper_bound;
    }

    public Long getSum_other_doc_count() {
        return sum_other_doc_count;
    }

    public void setSum_other_doc_count(Long sum_other_doc_count) {
        this.sum_other_doc_count = sum_other_doc_count;
    }

    public List<Map<Object, Object>> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Map<Object, Object>> buckets) {
        this.buckets = buckets;
    }

}
