package com.uteq.sgtic.configuration;

import com.uteq.sgtic.dtos.WorkProposalDto;
import com.uteq.sgtic.entities.WorkProposal;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean("defaultMapper")
    public ModelMapper defaultMapper() {
        return new ModelMapper();
    }

    @Bean("workProposalMapper")
    public ModelMapper workProposalMapper() {
        ModelMapper mapper = new ModelMapper();

        Converter<WorkProposal, WorkProposalDto> toDto = context -> {
            WorkProposal source = context.getSource();
            if (source == null) return null;
            WorkProposalDto dto = new WorkProposalDto();
            dto.setIdProposal(source.getIdProposal());
            dto.setIdStudent(source.getStudent() != null ? source.getStudent().getIdStudent() : null);
            dto.setTitle(source.getTitle());
            dto.setDescription(source.getDescription());
            dto.setRegistrationDate(source.getRegistrationDate());
            dto.setStatus(source.getStatus());
            return dto;
        };

        Converter<WorkProposalDto, WorkProposal> toEntity = context -> {
            WorkProposalDto source = context.getSource();
            if (source == null) return null;
            WorkProposal entity = new WorkProposal();
            entity.setTitle(source.getTitle());
            entity.setDescription(source.getDescription());
            entity.setRegistrationDate(source.getRegistrationDate());
            entity.setStatus(source.getStatus());
            return entity;
        };

        mapper.addConverter(toDto, WorkProposal.class, WorkProposalDto.class);
        mapper.addConverter(toEntity, WorkProposalDto.class, WorkProposal.class);

        return mapper;
    }
}
