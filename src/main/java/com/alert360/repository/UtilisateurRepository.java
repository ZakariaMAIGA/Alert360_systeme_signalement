package com.alert360.repository;

import com.alert360.entity.Utilisateur;
import com.alert360.entity.enums.EnumRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByTelephone(String telephone);

    Optional<Utilisateur> findByEmail(String email);

    boolean existsByTelephone(String telephone);

    boolean existsByEmail(String email);

    List<Utilisateur> findByRole(EnumRole role);

    List<Utilisateur> findByEstActifTrue();
}
