package com.pettoyou.server.domains.pet.dto.request;

public record PetProfilePhotoDto(
        String bucket,
        String object,
        String url
) {
}
