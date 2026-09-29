package com.alert360.mapper.Request;

import com.alert360.controller.dto.ActionCitoyenneRequestDto;
import com.alert360.entity.ActionCitoyenne;
import com.alert360.entity.Signalement;
import com.alert360.entity.enums.EnumStatutAction;
import org.springframework.stereotype.Component;

@Component

public class ActionCitoyenneRequestMapper {

    public ActionCitoyenne toEntity(ActionCitoyenneRequestDto dto, Signalement signalement){
        if(dto==null) return null;

        ActionCitoyenne action = new ActionCitoyenne();
        action.setDateHeureRendezVous(dto.getDateHeureRendezVous());
        action.setLieuRassemblement(dto.getLieuRassemblement());
        action.setStatutAction(EnumStatutAction.PLANIFIEE);
        action.setNombreParticipantsInscrits(0);
        action.setSignalementOrigine(signalement);

        return action;

    }
}
