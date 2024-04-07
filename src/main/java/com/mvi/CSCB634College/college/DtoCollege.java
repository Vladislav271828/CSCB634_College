package com.mvi.CSCB634College.college;

import com.mvi.CSCB634College.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtoCollege {
    private Long id;
    private String name;
    private String address;
    private User rector;
}
