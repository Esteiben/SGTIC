package com.uteq.sgtic.services;

import com.uteq.sgtic.dtos.pendingProposalDTO.PendingProposalDTO;
import java.util.List;
public interface ITopicProposedService {
    List<PendingProposalDTO> obtenerPropuestasPendientes(Integer idCoordinador);
    void procesarPropuesta(Integer idPropuesta, String estado, String motivo);}
