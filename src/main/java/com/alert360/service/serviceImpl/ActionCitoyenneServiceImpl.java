package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.ActionCitoyenneRequestDto;
import com.alert360.controller.dto.ActionCitoyenneResponseDto;
import com.alert360.entity.ActionCitoyenne;
import com.alert360.entity.Signalement;
import com.alert360.mapper.Request.ActionCitoyenneRequestMapper;
import com.alert360.mapper.Response.ActionCitoyenneResponseMapper;
import com.alert360.repository.ActionCitoyenneRepository;
import com.alert360.repository.SignalementRepository;
import com.alert360.service.serviceInter.ActionCitoyenneService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ActionCitoyenneServiceImpl  implements ActionCitoyenneService {

    private final ActionCitoyenneRepository actionRepository;
    private final SignalementRepository signalementRepository;
    private final ActionCitoyenneRequestMapper requestMapper;
    private final ActionCitoyenneResponseMapper responseMapper;
    @Override
    public ActionCitoyenneResponseDto creerAction(ActionCitoyenneRequestDto dto) {
        Signalement signalement = null;

        // Recherche du signalement d'origine si l'ID est fourni dans le DTO
        if (dto.getIdSignalementOrigine() != null) {
            signalement = signalementRepository.findById(dto.getIdSignalementOrigine())
                    .orElseThrow(() -> new RuntimeException(
                            "Signalement introuvable avec l'ID : " + dto.getIdSignalementOrigine()));
        }

        // Conversion DTO -> Entité via le mapper personnalisé
        ActionCitoyenne action = requestMapper.toEntity(dto, signalement);

        ActionCitoyenne savedAction = actionRepository.save(action);
        return responseMapper.toDto(savedAction);
    }

    @Override
    public ActionCitoyenneResponseDto modifierAction(Long idAction, ActionCitoyenneRequestDto dto) {
        ActionCitoyenne action = actionRepository.findById(idAction)
                .orElseThrow(() -> new  RuntimeException(
                        "Action citoyenne introuvable avec l'ID : " + idAction));

        // Mise à jour des champs basiques
        action.setDateHeureRendezVous(dto.getDateHeureRendezVous());
        action.setLieuRassemblement(dto.getLieuRassemblement());

        // Mise à jour de la relation avec le signalement d'origine
        if (dto.getIdSignalementOrigine() != null) {
            Signalement signalement = signalementRepository.findById(dto.getIdSignalementOrigine())
                    .orElseThrow(() -> new RuntimeException(
                            "Signalement introuvable avec l'ID : " + dto.getIdSignalementOrigine()));
            action.setSignalementOrigine(signalement);
        } else {
            action.setSignalementOrigine(null);
        }

        ActionCitoyenne updatedAction = actionRepository.save(action);
        return responseMapper.toDto(updatedAction);
    }

    @Override
    public ActionCitoyenneResponseDto obtenirParId(Long idAction) {
        ActionCitoyenne action = actionRepository.findById(idAction)
                .orElseThrow(() -> new RuntimeException(
                        "Action citoyenne introuvable avec l'ID : " + idAction));
        return responseMapper.toDto(action);
    }

    @Override
    public List<ActionCitoyenneResponseDto> obtenirToutesLesActions() {
        return actionRepository.findAll().stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void supprimerAction(Long idAction) {
    if(!actionRepository.existsById(idAction)){
        throw new RuntimeException("Action citoyenne introuvable avec l'ID : " + idAction);
    }

    actionRepository.deleteById(idAction);
    }
}
