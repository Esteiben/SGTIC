package com.uteq.sgtic.services;
import com.uteq.sgtic.dtos.banckTemaDTO.BanckTemaDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IBanckTemaService {
    List<BanckTemaDTO> listarTemasPorCarrera(Integer idCarrera);
    void crearTemaConComision(BanckTemaDTO dto, Integer idCarrera);

    @Transactional
    void actualizarTema(BanckTemaDTO temaDTO);

    @Transactional
    void eliminar(Integer idTema);
}