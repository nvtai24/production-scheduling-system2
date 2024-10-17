package org.nvtai.ProductionSchedulingSystem.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserModel {
    Long uid;

    Long eid;

    String username;

    String password;

    boolean active;

    String email;
}