package com.alert360.service.serviceInter;

import com.alert360.controller.dto.AssignationSignalementRequestDto;
import com.alert360.controller.dto.SignalementRequestDto;
import com.alert360.controller.dto.SignalementResponseDto;
import com.alert360.entity.enums.EnumStatut;

import java.util.List;

public interface SignalementService {

    // ==========================================================
    // CRÉATION ET MODIFICATION
    // ==========================================================

    // Créer un signalement (avec routage spatial PostGIS)
    SignalementResponseDto creerSignalement(SignalementRequestDto dto);

    // Modifier un signalement
    SignalementResponseDto modifierSignalement(Long idSignalement, SignalementRequestDto dto);

    // Changer le statut d'un signalement
    SignalementResponseDto changerStatut(Long idSignalement, EnumStatut nouveauStatut);

    // Supprimer un signalement
    void supprimerSignalement(Long idSignalement);

    // ==========================================================
    // ASSIGNATIONS (STRUCTURE ET AGENT)
    // ==========================================================

    // Assigner une structure compétente (recalcul manuel)
    SignalementResponseDto assignerStructure(Long idSignalement, Long idStructure);

    // Assigner un agent terrain par le Responsable de structure
    SignalementResponseDto assignerAgent(Long idSignalement, AssignationSignalementRequestDto dto);

    // ==========================================================
    // LECTURE ET RECHERCHE
    // ==========================================================

    // Obtenir un signalement par son ID
    SignalementResponseDto obtenirParId(Long idSignalement);

    // Obtenir tous les signalements
    List<SignalementResponseDto> obtenirTousLesSignalements();

    // Obtenir les signalements d'un citoyen
    List<SignalementResponseDto> obtenirParCitoyen(Long idCitoyen);

    // Obtenir les signalements attribués à une structure compétente
    List<SignalementResponseDto> obtenirParStructure(Long idStructure);

    // Obtenir les signalements assignés spécifiquement à un agent de terrain
    List<SignalementResponseDto> obtenirParAgentAssigne(Long idAgent);

    // Obtenir les signalements filtrés par statut
    List<SignalementResponseDto> obtenirParStatut(EnumStatut statut);
}