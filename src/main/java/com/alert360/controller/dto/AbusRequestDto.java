package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumNiveauGraviteAbus;
import com.alert360.entity.enums.EnumTypeAbus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AbusRequestDto {
    @NotNull
    private Long idSignalement;

    @NotNull
    private EnumTypeAbus typeAbus;

    @Size(max = 200)
    private String typeAbusPersonnalise;

    @NotNull
    private EnumNiveauGraviteAbus niveauGravite;

    @NotBlank
    @Size(max = 5000)
    private String justification;
}
