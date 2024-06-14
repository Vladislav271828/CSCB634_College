package com.mvi.CSCB634College.absence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtoAbsence {
    private Long id;
    private Long enrollmentId;

    private Date date;
    private String note;
}
