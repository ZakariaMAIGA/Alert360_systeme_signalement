package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.AuthResponseDto;
import com.alert360.controller.dto.CitoyenRequestDto;
import com.alert360.controller.dto.LoginRequestDto;
import com.alert360.entity.Admin;
import com.alert360.entity.AgentStructure;
import com.alert360.entity.Citoyen;
import com.alert360.entity.Utilisateur;
import com.alert360.entity.enums.EnumRole;
import com.alert360.exception.ForbiddenException;
import com.alert360.repository.CitoyenRepository;
import com.alert360.repository.UtilisateurRepository;
import com.alert360.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UtilisateurRepository utilisateurRepository;
    private final CitoyenRepository citoyenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;



    public AuthResponseDto seConnecter(LoginRequestDto loginRequest) {
        // Connexion réservée à l'application mobile
        return authentifierEtVerifierProfil(loginRequest, false);
    }

    public AuthResponseDto seConnecterWeb(LoginRequestDto loginRequest) {
        // Connexion réservée à l'application web
        return authentifierEtVerifierProfil(loginRequest, true);
    }

    private AuthResponseDto authentifierEtVerifierProfil(
            LoginRequestDto loginRequest,
            boolean connexionWeb) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getTelephone(),
                                loginRequest.getMotDePasse()
                        )
                );

        Utilisateur user = utilisateurRepository
                .findByTelephone(loginRequest.getTelephone())
                .orElseThrow(() -> new RuntimeException(
                        "Utilisateur introuvable avec le numéro : "
                                + loginRequest.getTelephone()
                ));

        if (!Boolean.TRUE.equals(user.getEstActif())) {
            throw new DisabledException("Votre compte est désactivé.");
        }

        boolean estAdmin = user instanceof Admin;

        boolean estResponsable =
                user instanceof AgentStructure agent
                        && Boolean.TRUE.equals(agent.getEstResponsable());

        boolean estCitoyen = user instanceof Citoyen;

        boolean estAgentDeTerrain =
                user instanceof AgentStructure agent
                        && Boolean.FALSE.equals(agent.getEstResponsable());

        boolean profilAutorise = connexionWeb
                ? (estAdmin || estResponsable)
                : (estCitoyen || estAgentDeTerrain);

        if (!profilAutorise) {
            throw new ForbiddenException(
                    connexionWeb
                            ? "Ce profil n'est pas autorisé à accéder à l'application web Alert'360."
                            : "Ce profil n'est pas autorisé à accéder à l'application mobile Alert'360."
            );
        }

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        AuthResponseDto.AuthResponseDtoBuilder builder =
                AuthResponseDto.builder()
                        .token(jwt)
                        .type("Bearer")
                        .idUtilisateur(user.getIdUtilisateur())
                        .nom(user.getNom())
                        .prenom(user.getPrenom())
                        .telephone(user.getTelephone())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .estActif(user.getEstActif());

        if (user instanceof Citoyen citoyen) {
            builder.quartier(citoyen.getQuartier());
        } else if (user instanceof AgentStructure agent) {
            builder.estResponsable(agent.getEstResponsable());

            if (agent.getStructure() != null) {
                builder.idStructure(
                        agent.getStructure().getIdStructure()
                );
            }
        }

        return builder.build();
    }


    @Transactional
    public AuthResponseDto inscrireCitoyen(CitoyenRequestDto request) {
        if (utilisateurRepository.existsByTelephone(request.getTelephone())) {
            throw new IllegalArgumentException("Erreur : Ce numéro de téléphone est déjà utilisé !");
        }

        if (request.getEmail() != null && !request.getEmail().isBlank() && utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Erreur : Cet email est déjà utilisé !");
        }

        Citoyen citoyen = new Citoyen();
        citoyen.setNom(request.getNom());
        citoyen.setPrenom(request.getPrenom());
        citoyen.setTelephone(request.getTelephone());
        citoyen.setEmail(request.getEmail());
        citoyen.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        citoyen.setQuartier(request.getQuartier());
        citoyen.setRole(EnumRole.CITOYEN);
        citoyen.setEstActif(true);
        citoyen.setPointScore(0);

        Citoyen citoyenSauve = citoyenRepository.save(citoyen);

        return AuthResponseDto.builder()
                .idUtilisateur(citoyenSauve.getIdUtilisateur())
                .nom(citoyenSauve.getNom())
                .prenom(citoyenSauve.getPrenom())
                .telephone(citoyenSauve.getTelephone())
                .email(citoyenSauve.getEmail())
                .role(citoyenSauve.getRole())
                .quartier(citoyenSauve.getQuartier())
                .estActif(citoyenSauve.getEstActif())
                .build();
    }
}