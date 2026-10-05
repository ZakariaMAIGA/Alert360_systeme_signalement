package com.alert360.service.serviceInter;

import com.alert360.controller.dto.AdminUtilisateurCreateDto;
import com.alert360.controller.dto.AdminUtilisateurUpdateDto;
import com.alert360.controller.dto.UtilisateurResponseDto;

import java.util.List;

public interface AdminService {

    UtilisateurResponseDto obtenirUtilisateurParId(Long idUtilisateur);

    List<UtilisateurResponseDto> obtenirTousLesUtilisateurs();

    UtilisateurResponseDto creerUtilisateur(
            AdminUtilisateurCreateDto dto
    );

    UtilisateurResponseDto modifierUtilisateur(
            Long idUtilisateur,
            AdminUtilisateurUpdateDto dto
    );

    UtilisateurResponseDto changerStatutCompte(
            Long idUtilisateur,
            Boolean estActif
    );
}