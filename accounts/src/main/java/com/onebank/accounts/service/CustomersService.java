package com.onebank.accounts.service;


import com.onebank.accounts.dto.CustomerDetailsDTO;

public interface CustomersService {

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    CustomerDetailsDTO fetchCustomerDetails(String mobileNumber, String correlationId);
}
