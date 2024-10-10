package com.onebank.accounts.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Accounts extends BaseEntity {


    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "account_number")
    @Id
    private Long accountNumber;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "branch_address")
    private String branchAddress;

    @Column(name = "communication_sw")
    private Boolean communicationSw;
}
