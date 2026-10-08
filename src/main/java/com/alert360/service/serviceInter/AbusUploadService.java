package com.alert360.service.serviceInter;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

public interface AbusUploadService {
    String enregistrer(MultipartFile fichier);
    Resource charger(String urlFichier);
}
