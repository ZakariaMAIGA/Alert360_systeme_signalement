package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.ContenuEducatifRequestDto;
import com.alert360.controller.dto.ContenuEducatifResponseDto;
import com.alert360.entity.Admin;
import com.alert360.entity.ContenuEducatif;
import com.alert360.mapper.ContenuEducatifRequestMapper;
import com.alert360.mapper.Response.ContenuEducatifResponseMapper;
import com.alert360.repository.AdminRepository;
import com.alert360.repository.ContenuEducatifRepository;
import com.alert360.security.services.UserDetailsCustom;
import com.alert360.service.serviceInter.ContenuEducatifService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContenuEducatifServiceImpl implements ContenuEducatifService {

    private final ContenuEducatifRepository contenuEducatifRepository;
    private final AdminRepository adminRepository;
    private final ContenuEducatifRequestMapper contenuEducatifRequestMapper;
    private final ContenuEducatifResponseMapper contenuEducatifResponseMapper;

    // ==========================================
    // RECUPERER L'ADMIN CONNECTE
    // ==========================================
    private Admin getAdminConnecte() {
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return adminRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Administrateur introuvable avec l'id : " + userDetails.getId()
                ));
    }

    // ==========================================
    // CREER UN CONTENU EDUCATIF
    // ==========================================
    @Override
    @Transactional
    public ContenuEducatifResponseDto creerContenu(ContenuEducatifRequestDto dto) {

        // 1. Récupérer l'administrateur connecté via JWT
        Admin auteur = getAdminConnecte();

        // 2. Transformer le DTO en Entity avec l'auteur
        ContenuEducatif contenu = contenuEducatifRequestMapper.toEntity(dto, auteur);
        contenu.setAuteur(auteur); // Assure l'association de l'auteur

        // 3. Enregistrer en base de données
        ContenuEducatif contenuEnregistre = contenuEducatifRepository.save(contenu);

        // 4. Transformer Entity -> Response DTO (avec nomAuteur et auteurId)
        return contenuEducatifResponseMapper.toDto(contenuEnregistre);
    }

    // ==========================================
    // MODIFIER UN CONTENU EDUCATIF
    // ==========================================
    @Override
    @Transactional
    public ContenuEducatifResponseDto modifierContenu(Long idContenu, ContenuEducatifRequestDto dto) {

        // 1. Rechercher le contenu
        ContenuEducatif contenu = contenuEducatifRepository.findById(idContenu)
                .orElseThrow(() -> new RuntimeException(
                        "Contenu éducatif introuvable avec l'id : " + idContenu
                ));

        // 2. Récupérer l'administrateur effectuant la modification
        Admin auteur = getAdminConnecte();

        // 3. Modifier les informations
        contenu.setTitre(dto.getTitre());
        contenu.setTheme(dto.getTheme());
        contenu.setFormat(dto.getFormat());
        contenu.setMediaUrl(dto.getMediaUrl());
        contenu.setAuteur(auteur);

        // 4. Enregistrer les modifications
        ContenuEducatif contenuModifie = contenuEducatifRepository.save(contenu);

        // 5. Retourner le DTO
        return contenuEducatifResponseMapper.toDto(contenuModifie);
    }

    // ==========================================
    // OBTENIR UN CONTENU PAR SON ID
    // ==========================================
    @Override
    @Transactional(readOnly = true)
    public ContenuEducatifResponseDto obtenirParId(Long idContenu) {

        // 1. Rechercher le contenu
        ContenuEducatif contenu = contenuEducatifRepository.findById(idContenu)
                .orElseThrow(() -> new RuntimeException(
                        "Contenu éducatif introuvable avec l'id : " + idContenu
                ));

        // 2. Transformer Entity -> DTO
        return contenuEducatifResponseMapper.toDto(contenu);
    }

    // ==========================================
    // OBTENIR TOUS LES CONTENUS
    // ==========================================
    @Override
    @Transactional(readOnly = true)
    public List<ContenuEducatifResponseDto> obtenirTousLesContenus() {

        return contenuEducatifRepository.findAll()
                .stream()
                .map(contenuEducatifResponseMapper::toDto)
                .toList();
    }

    // ==========================================
    // SUPPRIMER UN CONTENU
    // ==========================================
    @Override
    @Transactional
    public void supprimerContenu(Long idContenu) {

        // 1. Vérifier que le contenu existe
        if (!contenuEducatifRepository.existsById(idContenu)) {
            throw new RuntimeException(
                    "Contenu éducatif introuvable avec l'id : " + idContenu
            );
        }

        // 2. Supprimer
        contenuEducatifRepository.deleteById(idContenu);
    }
}