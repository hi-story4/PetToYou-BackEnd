package com.pettoyou.server.domains.pet.dto.response;

public record PetRegisterRespDto(
        String petName
) {
    public static PetRegisterRespDto from(String petName) {
        return new PetRegisterRespDto(petName);
    }
}
