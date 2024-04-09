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
public class DtoCollegeResponse {
    private College college;
    private User rector;
}
