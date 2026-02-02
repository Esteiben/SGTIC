package com.uteq.sgtic.dtos;

import lombok.Data;

@Data
public class CouncilMemberDto {
    private Integer id;
    private Integer idCouncil;
    private Integer idUser;
    private String position;
}
