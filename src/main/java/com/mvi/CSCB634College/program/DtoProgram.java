package com.mvi.CSCB634College.program;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtoProgram {

    private Long id;
    private Long courseId;
    private Integer year;
    private Boolean autumn;
    private Integer educationYear;
    private List<Integer> professorIds;
}