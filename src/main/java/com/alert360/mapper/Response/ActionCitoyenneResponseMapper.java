package com.alert360.mapper.Response;


import com.alert360.controller.dto.ActionCitoyenneResponseDto;
import com.alert360.entity.ActionCitoyenne;
import org.springframework.stereotype.Component;

@Component
public class ActionCitoyenneResponseMapper {

    public ActionCitoyenneResponseDto toDto(ActionCitoyenne actionCitoyenne){
        if(actionCitoyenne==null) return null;
        ActionCitoyenneResponseDto dto = new ActionCitoyenneResponseDto();
        dto.setIdAction(actionCitoyenne.getIdAction());
        dto.setDateHeureRendezVous(actionCitoyenne.getDateHeureRendezVous());
        dto.setLieuRassemblement(actionCitoyenne.getLieuRassemblement());
        dto.setNombreParticipantsInscrits(actionCitoyenne.getNombreParticipantsInscrits());
        dto.setStatutAction(actionCitoyenne.getStatutAction());

        if(actionCitoyenne.getSignalementOrigine() != null){
            dto.setIdSignalementOrigine(actionCitoyenne.getSignalementOrigine().getIdSignalement());
        }

        return dto;
    }
}
