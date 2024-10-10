package com.onebank.accounts.service.client;

import com.onebank.accounts.dto.CardsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardsFallBack implements  CardsFeignClient{


    @Override
    public ResponseEntity<CardsDTO> fetchCardDetails(String correlationId, String mobileNumber) {
        return null;
    }
}
