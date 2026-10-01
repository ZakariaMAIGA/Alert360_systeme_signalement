package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.SignalementRequestDto;
import com.alert360.controller.dto.SignalementResponseDto;
import com.alert360.entity.Categorie;
import com.alert360.entity.Citoyen;
import com.alert360.entity.Signalement;
import com.alert360.entity.StructureCompetente;
import com.alert360.entity.enums.EnumStatut;
import com.alert360.mapper.Request.SignalementRequestMapper;
import com.alert360.mapper.Response.SignalementResponseMapper;
import com.alert360.repository.CategorieRepository;
import com.alert360.repository.CitoyenRepository;
import com.alert360.repository.SignalementRepository;
import com.alert360.repository.StructureCompetenteRepository;
import com.alert360.service.serviceInter.SignalementService;
import com.alert360.util.GeometryUtil;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SignalementServiceImpl implements SignalementService {

    private final SignalementRepository signalementRepository;
    private final CitoyenRepository citoyenRepository;
    private final CategorieRepository categorieRepository;
    private final StructureCompetenteRepository structureCompetenteRepository;

    private final SignalementRequestMapper requestMapper;
    private final SignalementResponseMapper responseMapper;

    // ==========================================================
    // CREER UN SIGNALEMENT (AVEC ROUTAGE POSTGIS AUTOMATIQUE)
    // ==========================================================

    @Override
    public SignalementResponseDto creerSignalement(SignalementRequestDto dto) {

        // 1. Vérifier que le citoyen existe
        Citoyen citoyen = citoyenRepository.findById(dto.getCitoyenId())
                .orElseThrow(() -> new RuntimeException("Citoyen introuvable avec l'ID : " + dto.getCitoyenId()));

        // 2. Vérifier que la catégorie existe
        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable avec l'ID : " + dto.getCategorieId()));

        // 3. Transformer le DTO en Entity (inclut la conversion de la géolocalisation PostGIS)
        Signalement signalement = requestMapper.toEntity(dto, citoyen, categorie);

        // 4. Routage spatial automatique PostGIS : recherche de la structure la plus proche
        if (signalement.getLocalisation() != null) {
            structureCompetenteRepository.findNearestStructure(signalement.getLocalisation())
                    .ifPresent(signalement::setStructureAssignee);
        }

        // 5. Enregistrer le signalement (codeTrackingUnique & dateHeureAlerte générés via @PrePersist)
        Signalement signalementEnregistre = signalementRepository.save(signalement);

        // 6. Transformer Entity -> Response DTO
        return responseMapper.toDto(signalementEnregistre);
    }

    // ==========================================================
    // MODIFIER UN SIGNALEMENT
    // ==========================================================

    @Override
    public SignalementResponseDto modifierSignalement(Long idSignalement, SignalementRequestDto dto) {

        // 1. Rechercher le signalement
        Signalement signalement = signalementRepository.findById(idSignalement)
                .orElseThrow(() -> new RuntimeException("Signalement introuvable avec l'ID : " + idSignalement));

        // 2. Vérifier l'existence des relations associées
        Citoyen citoyen = citoyenRepository.findById(dto.getCitoyenId())
                .orElseThrow(() -> new RuntimeException("Citoyen introuvable avec l'ID : " + dto.getCitoyenId()));

        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable avec l'ID : " + dto.getCategorieId()));

        // 3. Mettre à jour l'entité via le mapper
        requestMapper.updateEntityFromDto(signalement, dto, citoyen, categorie);

        // 4. Réévaluer automatiquement l'assignation de la structure selon la nouvelle position
        if (signalement.getLocalisation() != null) {
            structureCompetenteRepository.findNearestStructure(signalement.getLocalisation())
                    .ifPresent(signalement::setStructureAssignee);
        } else {
            signalement.setStructureAssignee(null);
        }

        // 5. Enregistrer les modifications
        Signalement signalementModifie = signalementRepository.save(signalement);

        return responseMapper.toDto(signalementModifie);
    }

    // ==========================================================
    // CHANGER LE STATUT
    // ==========================================================

    @Override
    public SignalementResponseDto changerStatut(Long idSignalement, EnumStatut nouveauStatut) {

        Signalement signalement = signalementRepository.findById(idSignalement)
                .orElseThrow(() -> new RuntimeException("Signalement introuvable avec l'ID : " + idSignalement));

        signalement.setStatut(nouveauStatut);

        Signalement signalementModifie = signalementRepository.save(signalement);

        return responseMapper.toDto(signalementModifie);
    }

    // ==========================================================
    // ASSIGNER UNE STRUCTURE COMPÉTENTE (REASSIGNATION MANUELLE)
    // ==========================================================

    @Override
    public SignalementResponseDto assignerStructure(Long idSignalement, Long idStructure) {

        Signalement signalement = signalementRepository.findById(idSignalement)
                .orElseThrow(() -> new RuntimeException("Signalement introuvable avec l'ID : " + idSignalement));

        StructureCompetente structure = structureCompetenteRepository.findById(idStructure)
                .orElseThrow(() -> new RuntimeException("Structure compétente introuvable avec l'ID : " + idStructure));

        signalement.setStructureAssignee(structure);

        Signalement signalementModifie = signalementRepository.save(signalement);

        return responseMapper.toDto(signalementModifie);
    }

    // ==========================================================
    // OBTENIR UN SIGNALEMENT PAR ID
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public SignalementResponseDto obtenirParId(Long idSignalement) {

        Signalement signalement = signalementRepository.findById(idSignalement)
                .orElseThrow(() -> new RuntimeException("Signalement introuvable avec l'ID : " + idSignalement));

        return responseMapper.toDto(signalement);
    }

    // ==========================================================
    // OBTENIR TOUS LES SIGNALEMENTS
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SignalementResponseDto> obtenirTousLesSignalements() {

        return signalementRepository.findAll()
                .stream()
                .map(responseMapper::toDto)
                .toList();
    }

    // ==========================================================
    // OBTENIR LES SIGNALEMENTS D'UN CITOYEN
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SignalementResponseDto> obtenirParCitoyen(Long idCitoyen) {

        return signalementRepository.findByCitoyen_IdUtilisateur(idCitoyen)
                .stream()
                .map(responseMapper::toDto)
                .toList();
    }

    // ==========================================================
    // OBTENIR LES SIGNALEMENTS D'UNE STRUCTURE
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SignalementResponseDto> obtenirParStructure(Long idStructure) {

        return signalementRepository.findByStructureAssignee_IdStructure(idStructure)
                .stream()
                .map(responseMapper::toDto)
                .toList();
    }

    // ==========================================================
    // OBTENIR LES SIGNALEMENTS PAR STATUT
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SignalementResponseDto> obtenirParStatut(EnumStatut statut) {

        return signalementRepository.findByStatut(statut)
                .stream()
                .map(responseMapper::toDto)
                .toList();
    }

    // ==========================================================
    // SUPPRIMER UN SIGNALEMENT
    // ==========================================================

    @Override
    public void supprimerSignalement(Long idSignalement) {

        if (!signalementRepository.existsById(idSignalement)) {
            throw new RuntimeException("Signalement introuvable avec l'ID : " + idSignalement);
        }

        signalementRepository.deleteById(idSignalement);
    }
}