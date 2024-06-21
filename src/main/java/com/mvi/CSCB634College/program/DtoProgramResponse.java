package com.mvi.CSCB634College.program;

import com.mvi.CSCB634College.user.ResponseUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtoProgramResponse {
    private Long id;
    private Long courseId;
    private Integer year;
    private Boolean autumn;
    private Integer educationYear;
    private List<ResponseUser> professors;
}
