package com.uteq.sgtic.dtos;

import lombok.Data;

@Data
public class CommitteeMemberDto {
    private Integer id;
    private Integer idCommittee;
    private Integer idTeacher;
    private String role;
}
