package com.alert360.service.serviceInter;

import com.alert360.controller.dto.ActionCitoyenneRequestDto;
import com.alert360.controller.dto.ActionCitoyenneResponseDto;

import java.util.List;

public interface ActionCitoyenneService {

    ActionCitoyenneResponseDto creerAction(ActionCitoyenneRequestDto dto);

    ActionCitoyenneResponseDto modifierAction(Long idAction, ActionCitoyenneRequestDto dto);

    ActionCitoyenneResponseDto obtenirParId(Long idAction);

    List<ActionCitoyenneResponseDto> obtenirToutesLesActions();

    void supprimerAction(Long idAction);
}
