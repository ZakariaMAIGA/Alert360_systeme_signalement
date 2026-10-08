package com.alert360.service.serviceImpl;

import com.alert360.service.serviceInter.AbusUploadService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class AbusUploadServiceImpl implements AbusUploadService {
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp", "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    private final Path uploadDirectory;

    public AbusUploadServiceImpl(@Value("${app.upload-dir:uploads}") String uploadDirectory) {
        this.uploadDirectory = Paths.get(uploadDirectory).toAbsolutePath().normalize().resolve("abus");
    }

    @Override
    public String enregistrer(MultipartFile fichier) {
        if (fichier.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("La pièce jointe ne doit pas dépasser 10 Mo.");
        }
        String contentType = fichier.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Format de pièce jointe non accepté (PNG, JPG, WEBP, PDF ou DOCX).");
        }

        String extension = extension(contentType);
        String nomFichier = UUID.randomUUID() + extension;
        try {
            Files.createDirectories(uploadDirectory);
            try (var inputStream = fichier.getInputStream()) {
                Files.copy(inputStream, uploadDirectory.resolve(nomFichier));
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Impossible d’enregistrer la pièce jointe de l’abus.", exception);
        }
        return "/uploads/abus/" + nomFichier;
    }

    @Override
    public Resource charger(String urlFichier) {
        String prefix = "/uploads/abus/";
        if (urlFichier == null || !urlFichier.startsWith(prefix)) {
            throw new EntityNotFoundException("Pièce justificative introuvable.");
        }
        String nomFichier = urlFichier.substring(prefix.length());
        if (nomFichier.isBlank() || !nomFichier.matches("[a-f0-9-]+\\.(jpg|png|webp|pdf|doc|docx)")) {
            throw new EntityNotFoundException("Pièce justificative introuvable.");
        }
        Path fichier = uploadDirectory.resolve(nomFichier).normalize();
        if (!fichier.startsWith(uploadDirectory)) {
            throw new EntityNotFoundException("Pièce justificative introuvable.");
        }
        try {
            Resource resource = new UrlResource(fichier.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new EntityNotFoundException("Pièce justificative introuvable.");
            }
            return resource;
        } catch (IOException exception) {
            throw new IllegalStateException("Impossible de lire la pièce justificative.", exception);
        }
    }

    private String extension(String contentType) {
        return switch (contentType.toLowerCase(Locale.ROOT)) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "application/pdf" -> ".pdf";
            case "application/msword" -> ".doc";
            default -> ".docx";
        };
    }
}
