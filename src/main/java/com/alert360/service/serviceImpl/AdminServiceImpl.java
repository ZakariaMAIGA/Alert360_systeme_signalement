package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.AdminUtilisateurCreateDto;
import com.alert360.controller.dto.AdminUtilisateurUpdateDto;
import com.alert360.controller.dto.UtilisateurResponseDto;
import com.alert360.entity.AgentStructure;
import com.alert360.entity.Citoyen;
import com.alert360.entity.StructureCompetente;
import com.alert360.entity.Utilisateur;
import com.alert360.entity.enums.EnumRole;
import com.alert360.mapper.Response.UtilisateurResponseMapper;
import com.alert360.repository.AgentStructureRepository;
import com.alert360.repository.CitoyenRepository;
import com.alert360.repository.StructureCompetenteRepository;
import com.alert360.repository.UtilisateurRepository;
import com.alert360.service.serviceInter.AdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UtilisateurRepository utilisateurRepository;
    private final CitoyenRepository citoyenRepository;
    private final AgentStructureRepository agentStructureRepository;
    private final StructureCompetenteRepository structureCompetenteRepository;

    private final UtilisateurResponseMapper utilisateurResponseMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UtilisateurResponseDto obtenirUtilisateurParId(
            Long idUtilisateur) {

        Utilisateur utilisateur =
                utilisateurRepository.findById(idUtilisateur)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Utilisateur introuvable avec l'ID : "
                                                + idUtilisateur
                                )
                        );

        return utilisateurResponseMapper.toDto(utilisateur);
    }

    @Override
    public List<UtilisateurResponseDto> obtenirTousLesUtilisateurs() {

        return utilisateurRepository.findAll()
                .stream()
                .map(utilisateurResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UtilisateurResponseDto creerUtilisateur(
            AdminUtilisateurCreateDto dto) {

        verifierUnicite(
                dto.getTelephone(),
                dto.getEmail(),
                null
        );

        Utilisateur utilisateur;

        if (dto.getRole() == EnumRole.CITOYEN) {

            utilisateur = creerCitoyen(dto);

        } else if (dto.getRole() == EnumRole.STRUCTURE) {

            utilisateur = creerAgentStructure(dto);

        } else if (dto.getRole() == EnumRole.ADMIN) {

            utilisateur = creerAdmin(dto);

        } else {

            throw new RuntimeException(
                    "Rôle utilisateur non pris en charge : "
                            + dto.getRole()
            );
        }

        Utilisateur utilisateurSauvegarde =
                utilisateurRepository.save(utilisateur);

        return utilisateurResponseMapper.toDto(
                utilisateurSauvegarde
        );
    }

    private Citoyen creerCitoyen(
            AdminUtilisateurCreateDto dto) {

        Citoyen citoyen = new Citoyen();

        citoyen.setNom(dto.getNom());
        citoyen.setPrenom(dto.getPrenom());
        citoyen.setTelephone(dto.getTelephone());
        citoyen.setEmail(normaliserEmail(dto.getEmail()));

        citoyen.setMotDePasse(
                passwordEncoder.encode(dto.getMotDePasse())
        );

        citoyen.setRole(EnumRole.CITOYEN);
        citoyen.setEstActif(true);

        // Valeur initiale d'un nouveau citoyen
        citoyen.setPointScore(0);

        return citoyen;
    }

    private AgentStructure creerAgentStructure(
            AdminUtilisateurCreateDto dto) {

        if (dto.getMatriculeAgent() == null
                || dto.getMatriculeAgent().isBlank()) {

            throw new RuntimeException(
                    "Le matricule est obligatoire pour un agent de structure"
            );
        }

        if (dto.getIdStructure() == null) {

            throw new RuntimeException(
                    "La structure est obligatoire pour un agent de structure"
            );
        }

        if (agentStructureRepository
                .existsByMatriculeAgent(dto.getMatriculeAgent())) {

            throw new RuntimeException(
                    "Ce matricule agent est déjà utilisé"
            );
        }

        StructureCompetente structure =
                structureCompetenteRepository
                        .findById(dto.getIdStructure())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Structure introuvable avec l'ID : "
                                                + dto.getIdStructure()
                                )
                        );

        AgentStructure agent = new AgentStructure();

        agent.setNom(dto.getNom());
        agent.setPrenom(dto.getPrenom());
        agent.setTelephone(dto.getTelephone());
        agent.setEmail(normaliserEmail(dto.getEmail()));

        agent.setMotDePasse(
                passwordEncoder.encode(dto.getMotDePasse())
        );

        agent.setRole(EnumRole.STRUCTURE);
        agent.setEstActif(true);

        agent.setMatriculeAgent(
                dto.getMatriculeAgent()
        );

        agent.setStructure(structure);

        agent.setEstResponsable(
                dto.getEstResponsable() != null
                        && dto.getEstResponsable()
        );

        return agent;
    }

    private Utilisateur creerAdmin(
            AdminUtilisateurCreateDto dto) {

        Utilisateur admin = new Utilisateur();

        admin.setNom(dto.getNom());
        admin.setPrenom(dto.getPrenom());
        admin.setTelephone(dto.getTelephone());
        admin.setEmail(normaliserEmail(dto.getEmail()));

        admin.setMotDePasse(
                passwordEncoder.encode(dto.getMotDePasse())
        );

        admin.setRole(EnumRole.ADMIN);
        admin.setEstActif(true);

        return admin;
    }

    @Override
    public UtilisateurResponseDto modifierUtilisateur(
            Long idUtilisateur,
            AdminUtilisateurUpdateDto dto) {

        Utilisateur utilisateur =
                utilisateurRepository.findById(idUtilisateur)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Utilisateur introuvable avec l'ID : "
                                                + idUtilisateur
                                )
                        );

        /*
         * On ne change pas le type d'utilisateur ici.
         *
         * Exemple :
         * CITOYEN -> STRUCTURE
         *
         * nécessiterait de changer la sous-classe JPA.
         */
        if (utilisateur.getRole() != dto.getRole()) {

            throw new RuntimeException(
                    "Le changement de rôle n'est pas autorisé depuis cette interface. "
                            + "Veuillez conserver le rôle actuel."
            );
        }

        verifierUnicite(
                dto.getTelephone(),
                dto.getEmail(),
                idUtilisateur
        );

        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setTelephone(dto.getTelephone());
        utilisateur.setEmail(normaliserEmail(dto.getEmail()));

        /*
         * Informations spécifiques à un AgentStructure.
         */
        if (utilisateur instanceof AgentStructure agent) {

            if (dto.getMatriculeAgent() == null
                    || dto.getMatriculeAgent().isBlank()) {

                throw new RuntimeException(
                        "Le matricule est obligatoire pour un agent de structure"
                );
            }

            agentStructureRepository
                    .findByMatriculeAgent(dto.getMatriculeAgent())
                    .ifPresent(agentExistant -> {

                        if (!agentExistant.getIdUtilisateur()
                                .equals(idUtilisateur)) {

                            throw new RuntimeException(
                                    "Ce matricule agent est déjà utilisé"
                            );
                        }
                    });

            if (dto.getIdStructure() == null) {

                throw new RuntimeException(
                        "La structure est obligatoire pour un agent de structure"
                );
            }

            StructureCompetente structure =
                    structureCompetenteRepository
                            .findById(dto.getIdStructure())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Structure introuvable avec l'ID : "
                                                    + dto.getIdStructure()
                                    )
                            );

            agent.setMatriculeAgent(
                    dto.getMatriculeAgent()
            );

            agent.setStructure(structure);

            agent.setEstResponsable(
                    dto.getEstResponsable() != null
                            && dto.getEstResponsable()
            );
        }

        Utilisateur utilisateurMisAJour =
                utilisateurRepository.save(utilisateur);

        return utilisateurResponseMapper.toDto(
                utilisateurMisAJour
        );
    }

    @Override
    public UtilisateurResponseDto changerStatutCompte(
            Long idUtilisateur,
            Boolean estActif) {

        Utilisateur utilisateur =
                utilisateurRepository.findById(idUtilisateur)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Utilisateur introuvable avec l'ID : "
                                                + idUtilisateur
                                )
                        );

        utilisateur.setEstActif(estActif);

        Utilisateur updatedUtilisateur =
                utilisateurRepository.save(utilisateur);

        return utilisateurResponseMapper.toDto(
                updatedUtilisateur
        );
    }

    private void verifierUnicite(
            String telephone,
            String email,
            Long idUtilisateurExclu) {

        utilisateurRepository
                .findByTelephone(telephone)
                .ifPresent(utilisateur -> {

                    if (idUtilisateurExclu == null
                            || !utilisateur
                            .getIdUtilisateur()
                            .equals(idUtilisateurExclu)) {

                        throw new RuntimeException(
                                "Un utilisateur existe déjà avec ce numéro de téléphone"
                        );
                    }
                });

        if (email != null && !email.isBlank()) {

            utilisateurRepository
                    .findByEmail(email)
                    .ifPresent(utilisateur -> {

                        if (idUtilisateurExclu == null
                                || !utilisateur
                                .getIdUtilisateur()
                                .equals(idUtilisateurExclu)) {

                            throw new RuntimeException(
                                    "Un utilisateur existe déjà avec cette adresse email"
                            );
                        }
                    });
        }
    }

    private String normaliserEmail(String email) {

        if (email == null || email.isBlank()) {
            return null;
        }

        return email.trim();
    }
}