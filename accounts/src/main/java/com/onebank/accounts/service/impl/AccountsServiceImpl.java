package com.onebank.accounts.service.impl;

import com.onebank.accounts.constants.AccountsConstants;
import com.onebank.accounts.dto.AccountsDTO;
import com.onebank.accounts.dto.AccountsMsgDto;
import com.onebank.accounts.dto.CustomerDTO;
import com.onebank.accounts.entity.Accounts;
import com.onebank.accounts.entity.Customer;
import com.onebank.accounts.exception.CustomerExistsException;
import com.onebank.accounts.exception.ResourceNotFoundException;
import com.onebank.accounts.mapper.AccountsMapper;
import com.onebank.accounts.mapper.CustomerMapper;
import com.onebank.accounts.repository.AccountsRepository;
import com.onebank.accounts.repository.CustomerRepository;
import com.onebank.accounts.service.AccountsService;
import lombok.AllArgsConstructor;
import org.hibernate.boot.archive.scan.spi.ScanOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
//import org.springframework.cloud.stream.function.StreamBridge;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements AccountsService {

    private static final Logger log = LoggerFactory.getLogger(AccountsServiceImpl.class);

    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private final StreamBridge streamBridge;

    /**
     *
     * @param customerDTO - CustomerDTO Object
     */
    @Override
    public void createAccount(CustomerDTO customerDTO) {

        Customer customer = CustomerMapper.mapToCustomer(customerDTO,new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDTO.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            throw new CustomerExistsException("Customer already registered with given mobileNumber "
                    +customerDTO.getMobileNumber());
        }

        Customer savedCustomer = customerRepository.save(customer);
        Accounts savedAccount = accountsRepository.save(createNewAccount(savedCustomer));
        sendCommunication(savedAccount, savedCustomer);


    }

    private void sendCommunication(Accounts account, Customer customer) {
        var accountsMsgDto = new AccountsMsgDto(account.getAccountNumber(), customer.getName(),
                customer.getEmail(), customer.getMobileNumber());
        log.info("Sending Communication request for the details: {}", accountsMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", accountsMsgDto);
        log.info("Is the Communication request successfully triggered ? : {}", result);
    }
    @Override
    public CustomerDTO fetchAccount(String mobileNumber) {
       Customer customer =  customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Cusotmer","Mobile Number", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDTO customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDTO());
        customerDto.setAccountsDTO(AccountsMapper.mapToAccountsDto(accounts, new AccountsDTO()));
        return customerDto;

    }

    /**
     *
     * @param customerDTO
     * @return
     */

    @Override
    public boolean updateAccount(CustomerDTO customerDTO) {
        boolean isUpdated = false;
        AccountsDTO accountsDto = customerDTO.getAccountsDTO();
        if(accountsDto !=null ){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDTO,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

    /**
     * @param accountNumber - Long
     * @return boolean indicating if the update of communication status is successful or not
     */
    @Override
    public boolean updateCommunicationStatus(Long accountNumber) {
        boolean isUpdated = false;
        if(accountNumber !=null ){
            Accounts accounts = accountsRepository.findById(accountNumber).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountNumber.toString())
            );
            accounts.setCommunicationSw(true);
            accountsRepository.save(accounts);
            isUpdated = true;
        }
        return  isUpdated;
    }


    /**
     * @param customer - Customer Object
     * @return the new account details
     */
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);

        return newAccount;
    }
}
