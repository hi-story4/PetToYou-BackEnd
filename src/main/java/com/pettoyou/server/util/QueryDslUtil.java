package com.pettoyou.server.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QueryDslUtil {



     public <T extends Comparable<?>> List<OrderSpecifier<T>> getOrderSpecifiers(Sort sort, Class<?> entity) {

         List<OrderSpecifier<T>> orders = new ArrayList<>();

         sort.stream().forEach(order -> {
             Order direction = order.isAscending()? Order.ASC: Order.DESC;
             //springframework order ==> queryDsl의 order로 변환.
             String prop = order.getProperty();
             PathBuilder pathBuilder = new PathBuilder(entity, entity.getSimpleName().toLowerCase());
             //정렬 기준  ex) Review.class와 별칭.
             orders.add(new OrderSpecifier<>(direction, pathBuilder.get(prop)));
             //Unsafe 이슈가 있긴 한데 내부적으로 돌아가서
             System.out.println(direction);
             System.out.println(prop);
             System.out.println(entity.toString());
             System.out.println(pathBuilder);
             System.out.println(orders);
         });
         return orders;
    }
}
