//package com.jenkin.common.config;
//
//import com.github.dozermapper.core.DozerConverter;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.ZoneOffset;
//
//public class LocalDateTimeConverter extends DozerConverter<LocalDateTime, LocalDateTime> {
//
//  public LocalDateTimeConverter() {
//    super(LocalDateTime.class, LocalDateTime.class);
//  }
//
//  @Override
//  public LocalDateTime convertFrom(LocalDateTime source, LocalDateTime destination) {
//    if (source==null) {
//      return null;
//    }
//    return LocalDateTime.ofInstant(source.toInstant(ZoneOffset.MAX), ZoneId.of("Asia/Shanghai"));
//  }
//
//  @Override
//  public LocalDateTime convertTo(LocalDateTime source, LocalDateTime destination) {
//    if (source==null) {
//      return null;
//    }
//    return LocalDateTime.from(source.atZone( ZoneId.of("Asia/Shanghai")).toInstant());
//  }
//
//}