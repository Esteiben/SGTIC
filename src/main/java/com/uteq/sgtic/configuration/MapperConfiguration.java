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
}
