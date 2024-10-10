package com.onebank.accounts.service;

import com.onebank.accounts.dto.CustomerDTO;

public interface AccountsService {

    /**
     *
     * @param customerDTO - CustomerDTO Object
     */
    void createAccount(CustomerDTO customerDTO);

    /**
     *
     * @param mobileNumber
     * @return
     */
    CustomerDTO fetchAccount(String mobileNumber);

    /**
     *
     * @param customerDTO
     * @return
     */
    boolean updateAccount(CustomerDTO customerDTO);

    /**
     *
     * @param mobileNumber
     * @return
     */
    boolean deleteAccount(String mobileNumber);

    /**
     *
     * @param accountNumber - Long
     * @return boolean indicating if the update of communication status is successful or not
     */
    boolean updateCommunicationStatus(Long accountNumber);
}
