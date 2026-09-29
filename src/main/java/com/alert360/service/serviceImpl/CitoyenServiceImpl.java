package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.CitoyenRequestDto;
import com.alert360.controller.dto.CitoyenResponseDto;
import com.alert360.entity.Citoyen;
import com.alert360.mapper.Request.CitoyenRequestMapper;
import com.alert360.mapper.Response.CitoyenResponseMapper;
import com.alert360.repository.CitoyenRepository;
import com.alert360.service.serviceInter.CitoyenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CitoyenServiceImpl implements CitoyenService {

    private final CitoyenRepository citoyenRepository;
    private final CitoyenRequestMapper requestMapper;
    private final CitoyenResponseMapper responseMapper;
    @Override
    public CitoyenResponseDto creerCitoyen(CitoyenRequestDto dto) {
        Citoyen citoyen = requestMapper.toEntity(dto);
        Citoyen savedCitoyen = citoyenRepository.save(citoyen);
        return responseMapper.toDto(savedCitoyen);
    }

    @Override
    public CitoyenResponseDto modifierCitoyen(Long idUtilisateur, CitoyenRequestDto dto) {

        Citoyen citoyen = citoyenRepository.findById(idUtilisateur)
                .orElseThrow(() -> new RuntimeException("Citoyen introuvable avec l'ID : " +idUtilisateur));
        citoyen.setNom(dto.getNom());
        citoyen.setPrenom(dto.getPrenom());
        citoyen.setTelephone(dto.getTelephone());
        citoyen.setEmail(dto.getEmail());
        citoyen.setMotDePasse(dto.getMotDePasse());
        citoyen.setQuartier(dto.getQuartier());

        Citoyen updateCitoyen = citoyenRepository.save(citoyen);
        return responseMapper.toDto(updateCitoyen);


    }

    @Override
    public CitoyenResponseDto obtenirParId(Long idUtilisateur) {
        Citoyen citoyen = citoyenRepository.findById(idUtilisateur)
                .orElseThrow(()-> new RuntimeException("Citoyen introuvable avec l'ID : "+idUtilisateur));
        return responseMapper.toDto(citoyen);

    }

    @Override
    public List<CitoyenResponseDto> obtenirTousLesCitoyens() {

        return citoyenRepository.findAll().stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());

    }
    @Override
    public List<CitoyenResponseDto> obtenirParQuartier(String quartier) {
        return citoyenRepository.findByQuartier(quartier).stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public CitoyenResponseDto ajouterBadgeCivique(Long idUtilisateur, String badgesCiviques) {

        Citoyen citoyen = citoyenRepository.findById(idUtilisateur)
                .orElseThrow(()-> new RuntimeException("Citoyen introuvable avec l'ID : "+idUtilisateur));

        if (!citoyen.getBadgesCiviques().contains(badgesCiviques)) {
            citoyen.getBadgesCiviques().add(badgesCiviques);
        }

        return responseMapper.toDto(citoyenRepository.save(citoyen));

    }

    @Override
    public void supprimerCitoyen(Long idUtilisateur) {
    if(!citoyenRepository.existsById(idUtilisateur)){
        throw new RuntimeException("Citoyen introuvable avec l'ID : " + idUtilisateur);
    }

    citoyenRepository.deleteById(idUtilisateur);
    }
}
