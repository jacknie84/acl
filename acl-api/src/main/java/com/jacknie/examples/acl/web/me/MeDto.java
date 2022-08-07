package com.jacknie.examples.acl.web.me;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeDto {
    private String email;
    private Instant lastModifiedDate;
}
