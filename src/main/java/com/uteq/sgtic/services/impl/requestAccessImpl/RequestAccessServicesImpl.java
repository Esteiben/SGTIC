package com.uteq.sgtic.services.impl.requestAccessImpl;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.uteq.sgtic.dtos.requestAccess.RequestAccessDTO;
import com.uteq.sgtic.repository.requestAccess.RequestAccessRepository;
import jakarta.transaction.Transactional;
import com.uteq.sgtic.services.requestAccess.IRequestAccessServices;

@Service
@RequiredArgsConstructor
public class RequestAccessServicesImpl implements IRequestAccessServices {

    private final RequestAccessRepository requestAccessRepository;

    @Override // <-- Buena práctica
    @Transactional
    public void requestAccess(RequestAccessDTO dto) {
        requestAccessRepository.requestAccess(
                dto.getIdentificacion(),
                dto.getCorreo(),
                dto.getNombres(),
                dto.getApellidos(),
                dto.getIdFacultad(),
                dto.getIdCarrera()
        );
    }
}
