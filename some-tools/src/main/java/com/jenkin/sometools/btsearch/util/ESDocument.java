package com.jenkin.sometools.btsearch.util;

import java.lang.reflect.Field;

/**
 * @author jenkin
 * @date 2019/4/24 11:04
 */
public class ESDocument {

    private String indexName = "";
    private String type = "";
    private Field idField;

    public ESDocument() {
    }

    public ESDocument(String indexName, String type, Field idField) {
        this.indexName = indexName;
        this.type = type;
        this.idField = idField;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Field getIdField() {
        return idField;
    }

    public void setIdField(Field idField) {
        this.idField = idField;
    }
}
