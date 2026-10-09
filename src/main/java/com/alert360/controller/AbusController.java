package com.alert360.controller;

import com.alert360.controller.dto.AbusRequestDto;
import com.alert360.controller.dto.AbusResponseDto;
import com.alert360.entity.enums.EnumStatutAbus;
import com.alert360.service.serviceInter.AbusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/abus")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AbusController {
    private final AbusService abusService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AbusResponseDto> classerCommeAbus(
            @Valid @RequestPart("abus") AbusRequestDto dto,
            @RequestPart(value = "pieceJointe", required = false) MultipartFile pieceJointe
    ) {
        return ResponseEntity.status(201).body(abusService.classerCommeAbus(dto, pieceJointe));
    }

    @GetMapping
    public ResponseEntity<List<AbusResponseDto>> obtenirLesAbus() {
        return ResponseEntity.ok(abusService.obtenirLesAbus());
    }

    @PatchMapping("/{idAbus}/statut")
    public ResponseEntity<AbusResponseDto> changerStatut(
            @PathVariable Long idAbus,
            @RequestParam EnumStatutAbus statut
    ) {
        return ResponseEntity.ok(abusService.changerStatut(idAbus, statut));
    }

    @GetMapping("/{idAbus}/piece-jointe")
    public ResponseEntity<Resource> obtenirPieceJointe(@PathVariable Long idAbus) {
        Resource pieceJointe = abusService.obtenirPieceJointe(idAbus);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + pieceJointe.getFilename() + "\"")
                .contentType(MediaTypeFactory.getMediaType(pieceJointe).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(pieceJointe);
    }
}
