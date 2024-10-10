package com.onebank.accounts.controller;


import com.onebank.accounts.dto.CustomerDetailsDTO;
import com.onebank.accounts.dto.ErrorResponseDTO;
import com.onebank.accounts.service.CustomersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "REST API for Customers in OneBank",
        description = "REST APIs in OneBank to FETCH customer details"
)
@RestController
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CustomerController {

    private final CustomersService customersService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    public CustomerController(CustomersService customersService){
        this.customersService = customersService;
    }

    @Operation(
            summary = "Fetch Customer Details REST API",
            description = "REST API to fetch Customer details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    }
    )
    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailsDTO> fetchCustomerDetails(@RequestHeader("onebank-correlation-id")
                                                                       String correlationId,@RequestParam
                                                                   @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                                   String mobileNumber){
        logger.debug("fetchCustomerDetails Method Start");
        CustomerDetailsDTO customerDetailsDto = customersService.fetchCustomerDetails(mobileNumber,correlationId);
        logger.debug("fetchCustomerDetails Method End");
        return ResponseEntity.status(HttpStatus.SC_OK).body(customerDetailsDto);

    }


}
