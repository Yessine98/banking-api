package com.yassine.bankingapi.service;

import com.yassine.bankingapi.dto.CustomerDTO;
import com.yassine.bankingapi.dto.CustomerResponse;
import com.yassine.bankingapi.dto.PageResponse;
import com.yassine.bankingapi.exception.BadRequestException;
import com.yassine.bankingapi.exception.ResourceNotFoundException;
import com.yassine.bankingapi.model.Customer;
import com.yassine.bankingapi.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Créer un nouveau client
     */
    public CustomerResponse createCustomer(CustomerDTO dto) {
        // Vérifier si l'email existe déjà
        if (customerRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exists: " + dto.getEmail());
        }

        // Créer le client
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setAddress(dto.getAddress());

        Customer savedCustomer = customerRepository.save(customer);

        return mapToResponse(savedCustomer);
    }

    /**
     * Récupérer tous les clients
     */
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer un client par ID
     */
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return mapToResponse(customer);
    }

    /**
     * Mettre à jour un client
     */
    public CustomerResponse updateCustomer(Long id, CustomerDTO dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        // Vérifier si le nouvel email existe déjà (sauf si c'est le même)
        if (!customer.getEmail().equals(dto.getEmail()) && customerRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exists: " + dto.getEmail());
        }

        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setAddress(dto.getAddress());

        Customer updatedCustomer = customerRepository.save(customer);

        return mapToResponse(updatedCustomer);
    }

    /**
     * Supprimer un client
     */
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customerRepository.delete(customer);
    }

    /**
     * Récupérer tous les clients avec pagination
     */
    public PageResponse<CustomerResponse> getAllCustomersPaginated(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        
        List<CustomerResponse> responses = customerPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return PageResponse.from(customerPage, responses);
    }

    /**
     * Rechercher des clients par mot-clé (nom ou email)
     */
    public List<CustomerResponse> searchCustomers(String keyword) {
        return customerRepository.searchByKeyword(keyword).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Rechercher des clients par mot-clé avec pagination
     */
    public PageResponse<CustomerResponse> searchCustomersPaginated(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastName").ascending());
        Page<Customer> customerPage = customerRepository.searchByKeyword(keyword, pageable);
        
        List<CustomerResponse> responses = customerPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return PageResponse.from(customerPage, responses);
    }

    /**
     * Mapper Customer vers CustomerResponse
     */
    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.fromCustomer(customer);
    }
}
