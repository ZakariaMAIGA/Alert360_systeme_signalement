package com.alert360.repository;

import com.alert360.entity.ContenuEducatif;
import com.alert360.entity.enums.EnumFormat;
import com.alert360.entity.enums.EnumThematique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContenuEducatifRepository extends JpaRepository<ContenuEducatif, Long> {
    List<ContenuEducatif> findByTheme(EnumThematique theme);

    List<ContenuEducatif> findByFormat(EnumFormat format);

    List<ContenuEducatif> findByThemeAndFormat(EnumThematique theme, EnumFormat format);

    List<ContenuEducatif> findAllByOrderByDatePublicationDesc();
}