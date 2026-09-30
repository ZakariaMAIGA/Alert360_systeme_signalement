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
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
    // CREER UN SIGNALEMENT
    // ==========================================================

    @Override
    public SignalementResponseDto creerSignalement(
            SignalementRequestDto dto
    ) {

        // 1. Vérifier que le citoyen existe
        Citoyen citoyen = citoyenRepository
                .findById(dto.getCitoyenId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Citoyen introuvable avec l'ID : "
                                        + dto.getCitoyenId()
                        )
                );


        // 2. Vérifier que la catégorie existe
        Categorie categorie = categorieRepository
                .findById(dto.getCategorieId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Catégorie introuvable avec l'ID : "
                                        + dto.getCategorieId()
                        )
                );

        // 3. La structure est facultative lors de la création
        StructureCompetente structure = null;

        if (dto.getIdStructure() != null) {

            structure = structureCompetenteRepository
                    .findById(dto.getIdStructure())
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Structure compétente introuvable avec l'ID : "
                                            + dto.getIdStructure()
                            )
                    );
        }

        // 4. Transformer le DTO en Entity
        Signalement signalement =
                requestMapper.toEntity(
                        dto,
                        citoyen,
                        categorie,
                        structure
                );

        // 5. Générer le code de suivi unique
        signalement.setCodeTrackingUnique(
                UUID.randomUUID().toString()
        );

        // 6. Enregistrer le signalement
        Signalement signalementEnregistre =
                signalementRepository.save(signalement);

        // 7. Transformer Entity -> Response DTO
        return responseMapper.toDto(signalementEnregistre);
    }



    // MODIFIER UN SIGNALEMENT


    @Override
    public SignalementResponseDto modifierSignalement(
            Long idSignalement,
            SignalementRequestDto dto
    ) {

        // 1. Rechercher le signalement
        Signalement signalement =
                signalementRepository
                        .findById(idSignalement)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Signalement introuvable avec l'ID : "
                                                + idSignalement
                                )
                        );

        // 2. Mettre à jour les informations du signalement
        signalement.setTypeUrgence(dto.getTypeUrgence());
        signalement.setPhotoAvantUrl(dto.getPhotoAvantUrl());
        signalement.setAudioUrl(dto.getAudioUrl());
        signalement.setDescription(dto.getDescription());
        signalement.setLatitudeGPS(dto.getLatitudeGPS());
        signalement.setLongitudeGPS(dto.getLongitudeGPS());
        signalement.setRepereVisuel(dto.getRepereVisuel());


        // 3. Mettre à jour le citoyen
        Citoyen citoyen =
                citoyenRepository
                        .findById(dto.getCitoyenId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Citoyen introuvable avec l'ID : "
                                                + dto.getCitoyenId()
                                )
                        );

        signalement.setCitoyen(citoyen);


        // 4. Mettre à jour la catégorie
        Categorie categorie =
                categorieRepository
                        .findById(dto.getCategorieId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Catégorie introuvable avec l'ID : "
                                                + dto.getCategorieId()
                                )
                        );

        signalement.setCategorie(categorie);


        // 5. Mettre à jour la structure compétente
        if (dto.getIdStructure() != null) {

            StructureCompetente structure =
                    structureCompetenteRepository
                            .findById(dto.getIdStructure())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Structure compétente introuvable avec l'ID : "
                                                    + dto.getIdStructure()
                                    )
                            );

            signalement.setStructureAssignee(structure);

        } else {

            signalement.setStructureAssignee(null);
        }


        // 6. Enregistrer les modifications
        Signalement signalementModifie =
                signalementRepository.save(signalement);

        return responseMapper.toDto(signalementModifie);
    }


    // ==========================================================
    // CHANGER LE STATUT
    // ==========================================================

    @Override
    public SignalementResponseDto changerStatut(
            Long idSignalement,
            EnumStatut nouveauStatut
    ) {

        Signalement signalement =
                signalementRepository
                        .findById(idSignalement)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Signalement introuvable avec l'ID : "
                                                + idSignalement
                                )
                        );

        signalement.setStatut(nouveauStatut);

        Signalement signalementModifie =
                signalementRepository.save(signalement);

        return responseMapper.toDto(signalementModifie);
    }


    // ==========================================================
    // ASSIGNER UNE STRUCTURE COMPÉTENTE
    // ==========================================================

    @Override
    public SignalementResponseDto assignerStructure(
            Long idSignalement,
            Long idStructure
    ) {

        // 1. Rechercher le signalement
        Signalement signalement =
                signalementRepository
                        .findById(idSignalement)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Signalement introuvable avec l'ID : "
                                                + idSignalement
                                )
                        );

        // 2. Rechercher la structure
        StructureCompetente structure =
                structureCompetenteRepository
                        .findById(idStructure)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Structure compétente introuvable avec l'ID : "
                                                + idStructure
                                )
                        );

        // 3. Assigner la structure
        signalement.setStructureAssignee(structure);

        // 4. Enregistrer
        Signalement signalementModifie =
                signalementRepository.save(signalement);

        return responseMapper.toDto(signalementModifie);
    }


    // ==========================================================
    // OBTENIR UN SIGNALEMENT PAR ID
    // ==========================================================

    @Override
    public SignalementResponseDto obtenirParId(
            Long idSignalement
    ) {

        Signalement signalement =
                signalementRepository
                        .findById(idSignalement)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Signalement introuvable avec l'ID : "
                                                + idSignalement
                                )
                        );

        return responseMapper.toDto(signalement);
    }


    // ==========================================================
    // OBTENIR TOUS LES SIGNALEMENTS
    // ==========================================================

    @Override
    public List<SignalementResponseDto> obtenirTousLesSignalements() {

        return signalementRepository
                .findAll()
                .stream()
                .map(responseMapper::toDto)
                .toList();
    }


    // ==========================================================
    // OBTENIR LES SIGNALEMENTS D'UN CITOYEN
    // ==========================================================

    @Override
    public List<SignalementResponseDto> obtenirParCitoyen(
            Long idCitoyen
    ) {

        return signalementRepository
                .findByCitoyen_IdUtilisateur(idCitoyen)
                .stream()
                .map(responseMapper::toDto)
                .toList();
    }


    // ==========================================================
    // OBTENIR LES SIGNALEMENTS D'UNE STRUCTURE
    // ==========================================================

    @Override
    public List<SignalementResponseDto> obtenirParStructure(
            Long idStructure
    ) {

        return signalementRepository
                .findByStructureAssignee_IdStructure(idStructure)
                .stream()
                .map(responseMapper::toDto)
                .toList();
    }


    // ==========================================================
    // OBTENIR LES SIGNALEMENTS PAR STATUT
    // ==========================================================

    @Override
    public List<SignalementResponseDto> obtenirParStatut(
            EnumStatut statut
    ) {

        return signalementRepository
                .findByStatut(statut)
                .stream()
                .map(responseMapper::toDto)
                .toList();
    }


    // ==========================================================
    // SUPPRIMER UN SIGNALEMENT
    // ==========================================================

    @Override
    public void supprimerSignalement(
            Long idSignalement
    ) {

        if (!signalementRepository.existsById(idSignalement)) {

            throw new RuntimeException(
                    "Signalement introuvable avec l'ID : "
                            + idSignalement
            );
        }

        signalementRepository.deleteById(idSignalement);
    }
}