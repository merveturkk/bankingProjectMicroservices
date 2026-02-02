package com.mycompany.service.customer.event;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerCreatedEvent {
    private String customerFirstName;
    private String customerEmail;

}
