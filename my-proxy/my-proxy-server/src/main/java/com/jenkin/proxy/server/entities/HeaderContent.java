package com.jenkin.proxy.server.entities;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.CaseInsensitiveMap;

import java.util.*;
import java.util.stream.Collectors;

import static com.jenkin.proxy.server.utils.SocketRequestToHttpRequestUtils.ENTER;

public class HeaderContent{

    private Map<String, List<String>> headers;
    private String key;
    private List<String> value;

    private HeaderContent(){
        this.headers=new CaseInsensitiveMap<>();
    }



    public static HeaderContent build(){
        return new HeaderContent();
    }

    public HeaderContent head(String key,String value){
        this.headers.put(key, Collections.singletonList(value));
        return this;
    }
    public HeaderContent head(String key,List<String> value){
        this.headers.put(key,value);
        return this;
    }
    public void remove(String key){
        this.headers.remove(key);
    }
    public String getValue(String key){
        List<String> strings = this.headers.get(key);
        if (!CollectionUtil.isEmpty(strings)) {
            return strings.stream().collect(Collectors.joining(","));
        }
        return null;
    }
    public List<String> getValueList(String key){
        return this.headers.get(key);
    }
    public Set<String> keys( ){
        return this.headers.keySet();
    }
    public boolean containsKey( String key){
        return this.headers.containsKey(key);
    }

    @Override
    public String toString() {
        StringBuilder content = new StringBuilder();
        for (String s : keys()) {
            content.append(s).append(":").append(getValue(s)).append(ENTER);
        }
        content.append(ENTER);
        return content.toString();
    }
}