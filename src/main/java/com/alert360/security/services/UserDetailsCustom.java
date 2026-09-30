package com.alert360.security.services;

import com.alert360.entity.Utilisateur;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
@Getter
public class UserDetailsCustom implements UserDetails {

    private Long id;
    private String telephone; // Remplace email par telephone
    private String email;
    private boolean estActif;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsCustom build(Utilisateur utilisateur) {
        // Ajout explicite du préfixe ROLE_ attendu par Spring Security
        String roleName = utilisateur.getRole() != null ? utilisateur.getRole().name() : "CITOYEN";
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleName);

        return new UserDetailsCustom(
                utilisateur.getIdUtilisateur(),
                utilisateur.getTelephone(),
                utilisateur.getEmail(),
                utilisateur.getEstActif(),
                utilisateur.getMotDePasse(),
                Collections.singletonList(authority)
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        // L'identifiant principal envoyé lors du login est le numéro de téléphone
        return telephone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Retourne le statut réel du compte (actif ou non)
        return estActif;
    }
}