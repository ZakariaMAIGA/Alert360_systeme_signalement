package com.alert360.service.serviceInter;

import com.alert360.controller.dto.SignalementRequestDto;
import com.alert360.controller.dto.SignalementResponseDto;
import com.alert360.entity.enums.EnumStatut;

import java.util.List;

public interface SignalementService {

    // Créer un signalement
    SignalementResponseDto creerSignalement(
            SignalementRequestDto dto
    );

    // Modifier un signalement
    SignalementResponseDto modifierSignalement(
            Long idSignalement,
            SignalementRequestDto dto
    );

    // Changer le statut d'un signalement
    SignalementResponseDto changerStatut(
            Long idSignalement,
            EnumStatut nouveauStatut
    );

    // Assigner une structure compétente
    SignalementResponseDto assignerStructure(
            Long idSignalement,
            Long idStructure
    );

    // Obtenir un signalement par son ID
    SignalementResponseDto obtenirParId(
            Long idSignalement
    );

    // Obtenir tous les signalements
    List<SignalementResponseDto> obtenirTousLesSignalements();

    // Obtenir les signalements d'un citoyen
    List<SignalementResponseDto> obtenirParCitoyen(
            Long idCitoyen
    );

    // Obtenir les signalements d'une structure
    List<SignalementResponseDto> obtenirParStructure(
            Long idStructure
    );

    // Obtenir les signalements par statut
    List<SignalementResponseDto> obtenirParStatut(
            EnumStatut statut
    );

    // Supprimer un signalement
    void supprimerSignalement(
            Long idSignalement
    );
}