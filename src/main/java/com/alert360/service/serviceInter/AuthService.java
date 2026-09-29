package com.alert360.service.serviceInter;

import com.alert360.controller.dto.AuthResponseDto;
import com.alert360.controller.dto.CitoyenRequestDto;
import com.alert360.controller.dto.LoginRequestDto;

public interface AuthService {

    AuthResponseDto inscrireCitoyen(CitoyenRequestDto dto);

    AuthResponseDto connecter(LoginRequestDto dto);
}
