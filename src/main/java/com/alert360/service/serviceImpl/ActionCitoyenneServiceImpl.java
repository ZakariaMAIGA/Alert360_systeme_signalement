package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.ActionCitoyenneRequestDto;
import com.alert360.controller.dto.ActionCitoyenneResponseDto;
import com.alert360.mapper.Request.ActionCitoyenneRequestMapper;
import com.alert360.mapper.Response.ActionCitoyenneResponseMapper;
import com.alert360.repository.ActionCitoyenneRepository;
import com.alert360.service.serviceInter.ActionCitoyenneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionCitoyenneServiceImpl  implements ActionCitoyenneService {

    private final ActionCitoyenneRepository actionCitoyenneRepository;
    private final ActionCitoyenneRequestMapper actionCitoyenneRequestMapper;
    private final ActionCitoyenneResponseMapper actionCitoyenneResponseMapper;
    @Override
    public ActionCitoyenneResponseDto creerAction(ActionCitoyenneRequestDto dto) {
        return null;
    }

    @Override
    public ActionCitoyenneResponseDto modifierAction(Long idAction, ActionCitoyenneRequestDto dto) {
        return null;
    }

    @Override
    public ActionCitoyenneResponseDto obtenirParId(Long idAction) {
        return null;
    }

    @Override
    public List<ActionCitoyenneResponseDto> obtenirToutesLesActions() {
        return List.of();
    }

    @Override
    public void supprimerAction(Long idAction) {

    }
}
