package com.alert360.service.serviceInter;

import com.alert360.controller.dto.AbusRequestDto;
import com.alert360.controller.dto.AbusResponseDto;
import com.alert360.entity.enums.EnumStatutAbus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.util.List;

public interface AbusService {
    AbusResponseDto classerCommeAbus(AbusRequestDto dto, MultipartFile pieceJointe);
    List<AbusResponseDto> obtenirLesAbus();
    AbusResponseDto changerStatut(Long idAbus, EnumStatutAbus statut);
    Resource obtenirPieceJointe(Long idAbus);
}
