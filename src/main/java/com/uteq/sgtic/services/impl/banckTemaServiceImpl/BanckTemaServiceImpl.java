package com.uteq.sgtic.services.impl.banckTemaServiceImpl;

import com.uteq.sgtic.dtos.banckTemaDTO.BanckTemaDTO;
import com.uteq.sgtic.repository.banckTemaRepository.BanckTemaRepository;
import com.uteq.sgtic.services.IBanckTemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BanckTemaServiceImpl implements  IBanckTemaService{

    private final BanckTemaRepository repository;
    @Override
    public List<BanckTemaDTO> listarTemasPorCarrera(Integer idCarrera) {
        List<Object[]> resultados = repository.findTemasActivosRaw(idCarrera);
        return resultados.stream().map(row -> BanckTemaDTO.builder()
                .idTema((Integer) row[0])
                .titulo((String) row[1])
                .descripcion((String) row[2])
                .nombreOpcion((String) row[3])
                .idOpcion((Integer) row[4])
                .nombreComision((String) row[5])
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public void crearTemaConComision(BanckTemaDTO dto, Integer idCarrera) {
        repository.guardarTemaConComision(
                idCarrera,
                dto.getIdOpcion(),
                dto.getTitulo(),
                dto.getDescripcion(),
                dto.getNombreComision()
        );
    }

    @Transactional
    @Override
    public void actualizarTema(BanckTemaDTO temaDTO) {
        repository.editarTema(
                temaDTO.getIdTema(),
                temaDTO.getIdOpcion(),
                temaDTO.getTitulo(),
                temaDTO.getDescripcion(),
                temaDTO.getNombreComision()
        );
    }

    @Transactional
    @Override
    public void eliminar(Integer idTema) {
        repository.eliminar(idTema);
    }
}
