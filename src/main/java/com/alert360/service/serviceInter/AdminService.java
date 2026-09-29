package com.alert360.service.serviceInter;

import com.alert360.controller.dto.UtilisateurResponseDto;

import java.util.List;

public interface AdminService {
    UtilisateurResponseDto obtenirUtilisateurParId(Long idUtilisateur);

    List<UtilisateurResponseDto> obtenirTousLesUtilisateurs();

    UtilisateurResponseDto changerStatutCompte(Long idUtilisateur, Boolean estActif);
}
