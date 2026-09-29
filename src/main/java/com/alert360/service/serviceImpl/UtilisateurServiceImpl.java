package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.UtilisateurResponseDto;
import com.alert360.entity.Utilisateur;
import com.alert360.mapper.Response.UtilisateurResponseMapper;
import com.alert360.repository.UtilisateurRepository;
import com.alert360.service.serviceInter.UtilisateurService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurResponseMapper utilisateurResponseMapper;
    @Override
    public UtilisateurResponseDto obtenirParId(Long idUtilisateur) {
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + idUtilisateur));
        return utilisateurResponseMapper.toDto(utilisateur);
    }

    @Override
    public UtilisateurResponseDto obtenirParEmail(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'adresse email : " + email));
        return utilisateurResponseMapper.toDto(utilisateur);
    }

    @Override
    public List<UtilisateurResponseDto> obtenirTousLesUtilisateurs() {
        return utilisateurRepository.findAll().stream()
                .map(utilisateurResponseMapper::toDto)
                .collect(Collectors.toList());
    }
}
