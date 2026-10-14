package com.alert360.service.serviceImpl;

import com.alert360.service.serviceInter.MediaStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
public class MediaStorageServiceImpl implements MediaStorageService {

    private static final long TAILLE_MAX_PHOTO = 10L * 1024 * 1024;
    private static final long TAILLE_MAX_AUDIO = 10L * 1024 * 1024;

    private static final Map<String, String> EXTENSIONS_PHOTO = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp"
    );

    private static final Map<String, String> EXTENSIONS_AUDIO = Map.of(
            "audio/mpeg", ".mp3",
            "audio/mp4", ".m4a",
            "audio/aac", ".aac",
            "audio/wav", ".wav",
            "audio/x-wav", ".wav",
            "audio/ogg", ".ogg",
            "audio/webm", ".webm",
            "audio/3gpp", ".3gp"
    );

    private final Path dossierRacine;

    public MediaStorageServiceImpl(
            @Value("${alert360.media.storage-dir:uploads/alert360}")
            String dossierStockage
    ) {
        this.dossierRacine = Paths.get(dossierStockage)
                .toAbsolutePath()
                .normalize();
    }

    @Override
    public String stockerPhoto(MultipartFile fichier) {
        return stocker(fichier, "photos", EXTENSIONS_PHOTO, TAILLE_MAX_PHOTO);
    }

    @Override
    public String stockerAudio(MultipartFile fichier) {
        return stocker(fichier, "audios", EXTENSIONS_AUDIO, TAILLE_MAX_AUDIO);
    }

    private String stocker(
            MultipartFile fichier,
            String sousDossier,
            Map<String, String> typesAutorises,
            long tailleMax
    ) {
        if (fichier == null || fichier.isEmpty()) {
            throw new ResponseStatusException(
                    BAD_REQUEST, "Le fichier envoyé est vide."
            );
        }

        if (fichier.getSize() > tailleMax) {
            throw new ResponseStatusException(
                    BAD_REQUEST, "Le fichier dépasse la taille maximale autorisée."
            );
        }

        String typeMime = fichier.getContentType();

        if (typeMime == null || !typesAutorises.containsKey(typeMime.toLowerCase())) {
            throw new ResponseStatusException(
                    BAD_REQUEST, "Le format du fichier n'est pas autorisé."
            );
        }

        String extension = typesAutorises.get(typeMime.toLowerCase());
        String nomFichier = UUID.randomUUID() + extension;
        Path dossier = dossierRacine.resolve(sousDossier).normalize();
        Path destination = dossier.resolve(nomFichier).normalize();

        if (!destination.startsWith(dossierRacine)) {
            throw new ResponseStatusException(
                    BAD_REQUEST, "Chemin de fichier invalide."
            );
        }

        try {
            Files.createDirectories(dossier);
            fichier.transferTo(destination);
            return sousDossier + "/" + nomFichier;
        } catch (IOException e) {
            throw new ResponseStatusException(
                    INTERNAL_SERVER_ERROR,
                    "Impossible d'enregistrer le fichier sur le serveur."
            );
        }
    }
}