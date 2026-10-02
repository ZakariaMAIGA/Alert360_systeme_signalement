package com.alert360.controller;

import com.alert360.controller.dto.SosResponseDto;
import com.alert360.service.serviceInter.SosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sos")
@RequiredArgsConstructor
public class SosController {

    private final SosService sosService;

    @GetMapping("/proches")
    public ResponseEntity<List<SosResponseDto>> obtenirStructuresProches(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam(value = "rayonKm", required = false, defaultValue = "15") Double rayonKm) {

        List<SosResponseDto> structures = sosService.obtenirStructuresUrgenceProches(latitude, longitude, rayonKm);
        return ResponseEntity.ok(structures);
    }
}