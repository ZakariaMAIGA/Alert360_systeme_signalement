package com.alert360.service.serviceInter;

import com.alert360.controller.dto.UtilisateurResponseDto;

import java.util.List;

public interface UtilisateurService {
    UtilisateurResponseDto obtenirParId(Long idUtilisateur);

    UtilisateurResponseDto obtenirParEmail(String email);

    List<UtilisateurResponseDto> obtenirTousLesUtilisateurs();
}
