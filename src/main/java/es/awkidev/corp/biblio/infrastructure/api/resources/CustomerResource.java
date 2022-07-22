package es.awkidev.corp.biblio.infrastructure.api.resources;

import es.awkidev.corp.biblio.domain.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

import static es.awkidev.corp.biblio.infrastructure.api.resources.CustomerResource.CUSTOMERS;

@Slf4j
@RestController
//@PreAuthorize("hasRole('ADMIN')")
//@SecurityRequirement(name = "bearerAuth")
@RequestMapping(CUSTOMERS)
public class CustomerResource {

    static final String CUSTOMERS = "/library/customers";
    static final String NUMBER_MEMBERSHIP = "/{numberMembership}";

    private CustomerService customerService;

    @Autowired
    public CustomerResource(CustomerService customerService){
        this.customerService = customerService;
    }

    @GetMapping(NUMBER_MEMBERSHIP)
    public Mono<List<String>> searchByNumberMembership(@PathVariable String numberMembership) {
        return this.customerService.searchByNumberMembership(numberMembership)
                .collectList();
    }
}
