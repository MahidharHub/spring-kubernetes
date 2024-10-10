package com.onebank.accounts.service.client;


import com.onebank.accounts.dto.LoansDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name= "loans", fallback = LoansFallBack.class)
@Service
public interface LoansFeignClient {

    @GetMapping(value = "/api/fetch",consumes = "application/json")
    public ResponseEntity<LoansDTO> fetchLoanDetails(@RequestHeader("onebank-correlation-id")
                                                         String correlationId,@RequestParam String mobileNumber);

}
