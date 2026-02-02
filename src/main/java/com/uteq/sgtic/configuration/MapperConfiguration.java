package com.uteq.sgtic.configuration;

import com.uteq.sgtic.dtos.WorkProposalDto;
import com.uteq.sgtic.entities.WorkProposal;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
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

        PropertyMap<WorkProposal, WorkProposalDto> entityToDto = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setIdProposal(source.getIdProposal());
                map().setIdStudent(source.getStudent() != null ? source.getStudent().getIdStudent() : null);
                map().setTitle(source.getTitle());
                map().setDescription(source.getDescription());
                map().setRegistrationDate(source.getRegistrationDate());
                map().setStatus(source.getStatus());
            }
        };

        PropertyMap<WorkProposalDto, WorkProposal> dtoToEntity = new PropertyMap<>() {
            @Override
            protected void configure() {
                // For now only map simple fields; student needs to be set by service/controller if needed
                map().setTitle(source.getTitle());
                map().setDescription(source.getDescription());
                map().setRegistrationDate(source.getRegistrationDate());
                map().setStatus(source.getStatus());
            }
        };

        mapper.addMappings(entityToDto);
        mapper.addMappings(dtoToEntity);

        return mapper;
    }
}
