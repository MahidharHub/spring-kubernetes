package com.onebank.accounts.service.client;


import com.onebank.accounts.dto.LoansDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoansFallBack implements LoansFeignClient {

    @Override
    public ResponseEntity<LoansDTO> fetchLoanDetails(String correlationId, String mobileNumber) {
        return null;
    }
}
