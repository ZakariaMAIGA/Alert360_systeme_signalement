package com.alert360.repository;

import com.alert360.entity.ContenuEducatif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContenuEducatifRepository extends JpaRepository<ContenuEducatif, Long> {
}