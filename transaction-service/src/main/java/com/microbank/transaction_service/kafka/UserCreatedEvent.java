package com.microbank.transaction_service.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreatedEvent {
    private String username;
    private String firstname;
    private String lastname;
    private String country;
    private String role;
}