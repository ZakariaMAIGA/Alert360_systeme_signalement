package com.alert360.mapper.Response;

import com.alert360.controller.dto.AbusResponseDto;
import com.alert360.entity.Abus;
import com.alert360.entity.Categorie;
import com.alert360.entity.Citoyen;
import com.alert360.entity.Signalement;
import com.alert360.entity.StructureCompetente;
import com.alert360.entity.enums.EnumNiveauGraviteAbus;
import com.alert360.entity.enums.EnumStatutAbus;
import com.alert360.entity.enums.EnumTypeAbus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AbusResponseMapperTest {
    private final AbusResponseMapper mapper = new AbusResponseMapper();

    @Test
    void mapsClassificationAndRelatedReportInformation() {
        Citoyen citoyen = mock(Citoyen.class);
        when(citoyen.getPrenom()).thenReturn("Moussa");
        when(citoyen.getNom()).thenReturn("Traoré");
        Categorie categorie = mock(Categorie.class);
        when(categorie.getNom()).thenReturn("Voirie / Route");
        StructureCompetente structure = mock(StructureCompetente.class);
        when(structure.getNomStructure()).thenReturn("Mairie de Bamako");
        Signalement signalement = mock(Signalement.class);
        when(signalement.getIdSignalement()).thenReturn(12L);
        when(signalement.getCodeTrackingUnique()).thenReturn("ALT-12AB34CD");
        when(signalement.getCitoyen()).thenReturn(citoyen);
        when(signalement.getCategorie()).thenReturn(categorie);

        Abus abus = new Abus();
        abus.setIdAbus(5L);
        abus.setSignalement(signalement);
        abus.setStructure(structure);
        abus.setTypeAbus(EnumTypeAbus.AUTRE);
        abus.setTypeAbusPersonnalise("Usurpation de signalement");
        abus.setNiveauGravite(EnumNiveauGraviteAbus.MOYEN);
        abus.setStatut(EnumStatutAbus.A_VERIFIER);
        abus.setJustification("Photo et description incompatibles.");

        AbusResponseDto result = mapper.toDto(abus);

        assertEquals(5L, result.getIdAbus());
        assertEquals(12L, result.getIdSignalement());
        assertEquals("ALT-12AB34CD", result.getCodeTrackingUnique());
        assertEquals("Moussa Traoré", result.getCitoyenNomComplet());
        assertEquals("Voirie / Route", result.getCategorie());
        assertEquals("Mairie de Bamako", result.getNomStructure());
        assertEquals("Usurpation de signalement", result.getTypeAbusPersonnalise());
    }
}
