package com.pettoyou.server.domains.hospital.dto.request;

import com.pettoyou.server.domains.store.entity.Store;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link Store}
 */
public record HosptialSearchQueryInfo(@NotNull @Size(min = 2, message = "최소 2글자 이상 입력해주세요")  String storeName) implements Serializable {
}