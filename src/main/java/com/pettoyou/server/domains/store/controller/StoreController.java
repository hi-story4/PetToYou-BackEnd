package com.pettoyou.server.domains.store.controller;

import com.pettoyou.server.constant.dto.ApiResponse;
import com.pettoyou.server.domains.store.dto.response.StorePhotoDto;
import com.pettoyou.server.domains.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store")
public class StoreController {

    private final StoreService storeService;
    @GetMapping("/{storeId}/photo")
    public ResponseEntity<ApiResponse<List<StorePhotoDto>>> getStorePhotoList(
            @PathVariable Long storeId
    ){
        List<StorePhotoDto> response = storeService.getAllStorePhoto(storeId);

        return ApiResponse.createSuccessWithOk(response);
    }
}
