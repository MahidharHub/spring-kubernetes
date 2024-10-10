package com.onebank.accounts.service.impl;


import com.onebank.accounts.dto.AccountsDTO;
import com.onebank.accounts.dto.CardsDTO;
import com.onebank.accounts.dto.CustomerDetailsDTO;
import com.onebank.accounts.dto.LoansDTO;
import com.onebank.accounts.entity.Accounts;
import com.onebank.accounts.entity.Customer;
import com.onebank.accounts.exception.ResourceNotFoundException;
import com.onebank.accounts.mapper.AccountsMapper;
import com.onebank.accounts.mapper.CustomerMapper;
import com.onebank.accounts.repository.AccountsRepository;
import com.onebank.accounts.repository.CustomerRepository;
import com.onebank.accounts.service.CustomersService;
import com.onebank.accounts.service.client.CardsFeignClient;
import com.onebank.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements CustomersService {


    private AccountsRepository accountsRepository;

    private CustomerRepository customerRepository;

    private CardsFeignClient cardsFeignClient;

    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDTO fetchCustomerDetails(String mobileNumber,String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDTO customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDTO());
        customerDetailsDto.setAccountsDTO(AccountsMapper.mapToAccountsDto(accounts, new AccountsDTO()));

        ResponseEntity<LoansDTO> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId,mobileNumber);
        if(loansDtoResponseEntity != null){
            customerDetailsDto.setLoansDTO(loansDtoResponseEntity.getBody());
        }


        ResponseEntity<CardsDTO> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId,mobileNumber);
        if (cardsDtoResponseEntity != null) {

            customerDetailsDto.setCardsDTO(cardsDtoResponseEntity.getBody());
        }
        return customerDetailsDto;

    }
}
