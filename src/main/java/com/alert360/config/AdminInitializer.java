package com.alert360.config;

import com.alert360.entity.Admin;
import com.alert360.entity.enums.EnumRole;
import com.alert360.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        String adminPhone = "77335620"; // Numéro de téléphone par défaut de l'Admin
        String adminEmail = "admin@alert360.com";

        // Vérifier si l'admin existe déjà (par téléphone ou email)
        if (!utilisateurRepository.existsByTelephone(adminPhone)) {
            Admin admin = new Admin();
            admin.setNom("Diallo");
            admin.setPrenom("Alfousseny");
            admin.setTelephone(adminPhone);
            admin.setEmail(adminEmail);
            admin.setMotDePasse(passwordEncoder.encode("Admin@2026")); // Pensez à sécuriser le mot de passe
            admin.setRole(EnumRole.ADMIN);
            admin.setEstActif(true);
            admin.setDateCreation(LocalDateTime.now());

            utilisateurRepository.save(admin);
            log.info("Compte Administrateur initialisé avec succès ! Téléphone: {}", adminPhone);
        } else {
            log.info("Le compte Administrateur existe déjà en base de données.");
        }
    }
}