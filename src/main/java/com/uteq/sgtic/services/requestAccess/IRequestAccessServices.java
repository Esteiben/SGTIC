package com.uteq.sgtic.services.requestAccess;

import com.uteq.sgtic.dtos.requestAccess.RequestAccessDTO;

public interface IRequestAccessServices {
    void createRequest(RequestAccessDTO dto);
}