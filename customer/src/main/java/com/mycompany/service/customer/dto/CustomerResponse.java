package com.mycompany.service.customer.dto;


import com.mycompany.service.customer.entity.CustomerStatus;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private UUID id;
    private CustomerStatus customerStatus;
    private Instant createdAt;
}
