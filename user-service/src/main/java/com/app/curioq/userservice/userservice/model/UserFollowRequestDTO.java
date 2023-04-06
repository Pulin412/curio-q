package com.app.curioq.userservice.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowRequestDTO {
    private Long followerId;
    private Long followeeId;
}
