package com.pettoyou.server.domains.store.repository.custom;

import com.pettoyou.server.domains.store.entity.StorePhoto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.pettoyou.server.store.entity.QStorePhoto.storePhoto1;


@Repository
@RequiredArgsConstructor
public class StorePhotoCustomRepositoryImpl implements StorePhotoCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;


   public List<StorePhoto> getStorePhotosOrderByPhotoOrder(Long storeId){

      return jpaQueryFactory.selectFrom(storePhoto1)
               .where(storePhoto1.store.storeId.eq(storeId))
               .orderBy(storePhoto1.photoOrder.asc())
               .fetch();
   }
}
