package com.pettoyou.server.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class QueryDslUtil {



     public <T extends Comparable<?>> List<OrderSpecifier<T>> getOrderSpecifiers(Sort sort, Class<?> entity) {

         List<OrderSpecifier<T>> orders = new ArrayList<>();

         sort.stream().forEach(order -> {
             Order direction = order.isAscending()? Order.ASC: Order.DESC;
             //springframework order ==> queryDsl의 order로 변환.
             String prop = order.getProperty();
             PathBuilder pathBuilder = new PathBuilder(entity, prop);
             //정렬 기준  ex) Review.class의 created_at
             orders.add(new OrderSpecifier<>(direction, pathBuilder.get(prop)));

             //Unsafe 이슈가 있긴 한데 내부적으로 돌아가서
             System.out.println(direction);
             System.out.println(prop);
             System.out.println(pathBuilder);
         });
         return orders;
    }
}
