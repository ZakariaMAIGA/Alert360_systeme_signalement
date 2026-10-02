// SosService.java
package com.alert360.service.serviceInter;

import com.alert360.controller.dto.SosResponseDto;
import java.util.List;

public interface SosService {
    List<SosResponseDto> obtenirStructuresUrgenceProches(Double latitude, Double longitude, Double rayonKm);
}