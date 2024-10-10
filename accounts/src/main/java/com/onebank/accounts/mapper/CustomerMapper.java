package com.onebank.accounts.mapper;

import com.onebank.accounts.dto.CustomerDTO;
import com.onebank.accounts.dto.CustomerDetailsDTO;
import com.onebank.accounts.entity.Customer;

public class CustomerMapper {

    public static CustomerDTO mapToCustomerDto(Customer customer, CustomerDTO customerDto) {
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setMobileNumber(customer.getMobileNumber());
        return customerDto;
    }

    public static Customer mapToCustomer(CustomerDTO customerDto, Customer customer) {
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setMobileNumber(customerDto.getMobileNumber());
        return customer;
    }

    public static CustomerDetailsDTO mapToCustomerDetailsDto(Customer customer, CustomerDetailsDTO customerDetailsDto) {
        customerDetailsDto.setName(customer.getName());
        customerDetailsDto.setEmail(customer.getEmail());
        customerDetailsDto.setMobileNumber(customer.getMobileNumber());
        return customerDetailsDto;
    }
}
