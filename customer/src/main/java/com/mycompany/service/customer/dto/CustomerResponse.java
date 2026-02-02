package com.mycompany.service.customer.dto;


import com.mycompany.service.customer.entity.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private UUID id;
    private CustomerStatus customerStatus;
    private Instant createdAt;
}
