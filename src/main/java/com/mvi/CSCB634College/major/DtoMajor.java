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

    private Long id;
    private String name;
    private Long departmentId;

    
}
