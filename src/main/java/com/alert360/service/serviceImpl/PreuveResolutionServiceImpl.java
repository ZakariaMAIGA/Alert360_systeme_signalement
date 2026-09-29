package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.PreuveResolutionRequestDto;
import com.alert360.controller.dto.PreuveResolutionResponseDto;
import com.alert360.entity.PreuveResolution;
import com.alert360.entity.Signalement;
import com.alert360.mapper.Request.PreuveResolutionRequestMapper;
import com.alert360.mapper.Response.PreuveResolutionResponseMapper;
import com.alert360.repository.PreuveResolutionRepository;
import com.alert360.repository.SignalementRepository;
import com.alert360.service.serviceInter.PreuveResolutionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class PreuveResolutionServiceImpl implements PreuveResolutionService {

    private final PreuveResolutionRepository preuveResolutionRepository;
    private final SignalementRepository signalementRepository;
    private final PreuveResolutionRequestMapper requestMapper;
    private final PreuveResolutionResponseMapper responseMapper;
    @Override
    public PreuveResolutionResponseDto ajouterPreuveResolution(PreuveResolutionRequestDto dto) {
        Signalement signalement = signalementRepository.findById(dto.getIdSignalement())
                .orElseThrow(()-> new RuntimeException("Signalement introuvable avec l'ID : " + dto.getIdSignalement()));

        if (preuveResolutionRepository.findBySignalementIdSignalement(dto.getIdSignalement()).isPresent()) {
            throw new RuntimeException("Une preuve de résolution existe déjà pour le signalement n° " + dto.getIdSignalement());
        }
        PreuveResolution preuve = requestMapper.toEntity(dto, signalement);
        PreuveResolution savedPreuve = preuveResolutionRepository.save(preuve);

        // Liaison bidirectionnelle
        signalement.setPreuveResolution(savedPreuve);
        signalementRepository.save(signalement);

        return responseMapper.toDto(savedPreuve);

    }

    @Override
    public PreuveResolutionResponseDto modifierPreuveResolution(Long idPreuve, PreuveResolutionRequestDto dto) {
        PreuveResolution preuve = preuveResolutionRepository.findById(idPreuve)
                .orElseThrow(() -> new RuntimeException("Preuve de résolution introuvable avec l'ID : " + idPreuve));

        preuve.setTypePreuve(dto.getTypePreuve());
        preuve.setPhotoApresUrl(dto.getPhotoApresUrl());
        preuve.setRapportTexte(dto.getRapportTexte());

        PreuveResolution updatedPreuve = preuveResolutionRepository.save(preuve);
        return responseMapper.toDto(updatedPreuve);
    }

    @Override
    public PreuveResolutionResponseDto obtenirParId(Long idPreuve) {
        PreuveResolution preuve = preuveResolutionRepository.findById(idPreuve)
                .orElseThrow(() -> new RuntimeException("Preuve de résolution introuvable avec l'ID : " + idPreuve));
        return responseMapper.toDto(preuve);
    }

    @Override
    public PreuveResolutionResponseDto obtenirParSignalement(Long idSignalement) {
        PreuveResolution preuve = preuveResolutionRepository.findBySignalementIdSignalement(idSignalement)
                .orElseThrow(() -> new RuntimeException("Aucune preuve de résolution enregistrée pour le signalement ID : " + idSignalement));
        return responseMapper.toDto(preuve);
    }

    @Override
    public List<PreuveResolutionResponseDto> obtenirToutesLesPreuves() {
        return preuveResolutionRepository.findAll().stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void supprimerPreuve(Long idPreuve) {
        PreuveResolution preuve = preuveResolutionRepository.findById(idPreuve)
                .orElseThrow(() -> new RuntimeException("Preuve de résolution introuvable avec l'ID : " + idPreuve));

        // Rompre la relation OneToOne avec le Signalement avant suppression
        if (preuve.getSignalement() != null) {
            preuve.getSignalement().setPreuveResolution(null);
        }

        preuveResolutionRepository.delete(preuve);
    }
}
