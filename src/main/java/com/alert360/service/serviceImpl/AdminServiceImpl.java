package com.alert360.service.serviceImpl;


import com.alert360.controller.dto.UtilisateurResponseDto;
import com.alert360.entity.Utilisateur;
import com.alert360.mapper.Response.UtilisateurResponseMapper;
import com.alert360.repository.UtilisateurRepository;
import com.alert360.service.serviceInter.AdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurResponseMapper utilisateurResponseMapper;
    @Override
    public UtilisateurResponseDto obtenirUtilisateurParId(Long idUtilisateur) {
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + idUtilisateur));
        return utilisateurResponseMapper.toDto(utilisateur);
    }

    @Override
    public List<UtilisateurResponseDto> obtenirTousLesUtilisateurs() {
        return utilisateurRepository.findAll().stream()
                .map(utilisateurResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UtilisateurResponseDto changerStatutCompte(Long idUtilisateur, Boolean estActif) {
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + idUtilisateur));

        utilisateur.setEstActif(estActif);
        Utilisateur updatedUtilisateur = utilisateurRepository.save(utilisateur);

        return utilisateurResponseMapper.toDto(updatedUtilisateur);
    }
}
