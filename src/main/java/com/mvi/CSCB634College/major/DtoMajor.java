package com.mvi.CSCB634College.major;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtoMajor {

    private Integer id;
    private String name;
    private Integer departmentId;

    
}
