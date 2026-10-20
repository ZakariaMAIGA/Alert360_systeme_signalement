package com.alert360.service.serviceInter;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface MediaStorageService {

    String stockerPhoto(MultipartFile fichier);

    String stockerAudio(MultipartFile fichier);

    Resource chargerPhoto(String nomFichier);

    Resource chargerAudio(String nomFichier);
}