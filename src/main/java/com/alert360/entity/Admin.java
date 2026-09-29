package com.alert360.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "admin")
@Getter
@Setter
public class  Admin extends Utilisateur {
    @OneToMany(mappedBy = "auteur", fetch = FetchType.LAZY)
    private List<Actualite> actualitesPubliees = new ArrayList<>();

    @OneToMany(mappedBy = "auteur", fetch = FetchType.LAZY)
    private List<ContenuEducatif> contenusEducatifsPublies = new ArrayList<>();
}
