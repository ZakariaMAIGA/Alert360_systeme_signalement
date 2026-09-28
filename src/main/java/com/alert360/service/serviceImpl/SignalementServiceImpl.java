package com.alert360.service;

import com.alert360.controller.dto.SignalementRequestDto;
import com.alert360.controller.dto.SignalementResponseDto;
import com.alert360.entity.Categorie;
import com.alert360.entity.Citoyen;
import com.alert360.entity.Signalement;
import com.alert360.mapper.SignalementRequestMapper;
import com.alert360.mapper.SignalementResponseMapper;
import com.alert360.repository.CategorieRepository;
import com.alert360.repository.CitoyenRepository;
import com.alert360.repository.SignalementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SignalementServiceImpl implements SignalementService {

    private final SignalementRepository signalementRepository;
    private final CitoyenRepository citoyenRepository;
    private final CategorieRepository categorieRepository;

    private final SignalementRequestMapper signalementRequestMapper;
    private final SignalementResponseMapper signalementResponseMapper;


    @Override
    public SignalementResponseDto creerSignalement(
            SignalementRequestDto dto
    ) {

        Citoyen citoyen = citoyenRepository.findById(dto.getCitoyenId())
                .orElseThrow(() ->
                        new RuntimeException("Citoyen introuvable")
                );

        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() ->
                        new RuntimeException("Catégorie introuvable")
                );

        Signalement signalement =
                signalementRequestMapper.toEntity(
                        dto,
                        citoyen,
                        categorie
                );

        signalement.setCodeTrackingUnique(
                UUID.randomUUID().toString()
        );

        Signalement signalementEnregistre =
                signalementRepository.save(signalement);

        return signalementResponseMapper.toDto(signalementEnregistre);
    }


    @Override
    public List<SignalementResponseDto> getAllSignalements() {

        return signalementRepository.findAll()
                .stream()
                .map(signalementResponseMapper::toDto)
                .toList();
    }


    @Override
    public SignalementResponseDto getSignalementById(Long id) {

        Signalement signalement =
                signalementRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Signalement introuvable avec l'id : " + id
                                )
                        );

        return signalementResponseMapper.toDto(signalement);
    }


    @Override
    public SignalementResponseDto modifierSignalement(
            Long id,
            SignalementRequestDto dto
    ) {

        Signalement signalement =
                signalementRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Signalement introuvable avec l'id : " + id
                                )
                        );

        Citoyen citoyen =
                citoyenRepository.findById(dto.getCitoyenId())
                        .orElseThrow(() ->
                                new RuntimeException("Citoyen introuvable")
                        );

        Categorie categorie =
                categorieRepository.findById(dto.getCategorieId())
                        .orElseThrow(() ->
                                new RuntimeException("Catégorie introuvable")
                        );

        signalement.setTypeUrgence(dto.getTypeUrgence());
        signalement.setPhotoAvantUrl(dto.getPhotoAvantUrl());
        signalement.setAudioUrl(dto.getAudioUrl());
        signalement.setDescription(dto.getDescription());
        signalement.setLatitudeGPS(dto.getLatitudeGPS());
        signalement.setLongitudeGPS(dto.getLongitudeGPS());
        signalement.setRepereVisuel(dto.getRepereVisuel());

        signalement.setCitoyen(citoyen);
        signalement.setCategorie(categorie);

        Signalement signalementModifie =
                signalementRepository.save(signalement);

        return signalementResponseMapper.toDto(signalementModifie);
    }


    @Override
    public void supprimerSignalement(Long id) {

        if (!signalementRepository.existsById(id)) {
            throw new RuntimeException(
                    "Signalement introuvable avec l'id : " + id
            );
        }

        signalementRepository.deleteById(id);
    }
}