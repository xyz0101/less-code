//package com.jenkin.sometools.btsearch.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.elasticsearch.client.transport.TransportClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
//import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
//import org.springframework.data.elasticsearch.config.AbstractReactiveElasticsearchConfiguration;
//
//import javax.annotation.PostConstruct;
//
///**
// * @author ：jenkin
// * @date ：Created at 2021/4/25 21:31
// * @description：
// *
// * @modified By：
// * @version: 1.0
// */
//@Configuration
//@Slf4j
//public class ElasticSearchConfig extends AbstractReactiveElasticsearchConfiguration {
//
//    @Value("${elasticsearch.address}")
//    public String[] address;
//
//    /**
//     * 例子55.客户端配置
//     * HttpHeaders httpHeaders = new HttpHeaders();
//     * //定义默认标题（如果需要自定义）自定义请求头
//     * httpHeaders.add("some-header", "on every request")
//     *
//     * ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//     * //使用构建器来提供集群地址，设置缺省值HttpHeaders或启用SSL。
//     *   .connectedTo("localhost:9200", "localhost:9291")
//     *   //（可选）启用SSL。
//     *   .usingSsl()
//     *   //（可选）设置代理。
//     *   .withProxy("localhost:8888")
//     *   //（可选）设置路径前缀，通常在不同的群集将某个反向代理置于后面时使用。
//     *   .withPathPrefix("ela")
//     *   //设置连接超时。默认值为10秒。
//     *   .withConnectTimeout(Duration.ofSeconds(5))
//     *   //设置套接字超时。默认值为5秒。
//     *   .withSocketTimeout(Duration.ofSeconds(3))
//     *   //（可选）设置请求头。
//     *   .withDefaultHeaders(defaultHeaders)
//     *   //添加基本​​身份验证。
//     *   .withBasicAuth(username, password)
//     *   //Supplier<Header>可以指定一个函数，该请求在每次将请求发送到Elasticsearch之前都会被调用-例如，此处，当前时间写在标头中。
//     *   .withHeaders(() -> {
//     *     HttpHeaders headers = new HttpHeaders();
//     *     headers.add("currentTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//     *     return headers;
//     *   })
//     *   //用于反应式设置的功能配置 WebClient
//     *   .withWebClientConfigurer(webClient -> {
//     *     //...
//     *     return webClient;
//     *   })
//     *   //对于非反应式设置，可配置REST客户端的功能
//     *   .withHttpClientConfigurer(clientBuilder -> {
//     *       //...
//     *       return clientBuilder;
//     *   })
//     *   . // ... other options
//     *   .build();
//     * @return
//     */
//    @Override
//    @Bean
//    public ReactiveElasticsearchClient reactiveElasticsearchClient() {
//        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo(address)
//                .build();
//        return ReactiveRestClients.create(clientConfiguration);
//    }
//}
