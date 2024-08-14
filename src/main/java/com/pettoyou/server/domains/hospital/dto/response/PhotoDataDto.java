package com.pettoyou.server.domains.hospital.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pettoyou.server.domains.photo.entity.PhotoData;

import java.io.Serializable;

/**
 * DTO for {@link PhotoData}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PhotoDataDto(String photoUrl) implements Serializable {
}