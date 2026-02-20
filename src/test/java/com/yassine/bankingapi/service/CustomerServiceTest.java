package com.yassine.bankingapi.service;

import com.yassine.bankingapi.dto.CustomerDTO;
import com.yassine.bankingapi.dto.CustomerResponse;
import com.yassine.bankingapi.exception.BadRequestException;
import com.yassine.bankingapi.exception.ResourceNotFoundException;
import com.yassine.bankingapi.model.Customer;
import com.yassine.bankingapi.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerService Unit Tests")
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;
    private CustomerDTO testCustomerDTO;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setFirstName("Ahmed");
        testCustomer.setLastName("Ben Ali");
        testCustomer.setEmail("ahmed@example.com");
        testCustomer.setPhoneNumber("12345678");
        testCustomer.setAddress("Tunis");
        testCustomer.setCreatedAt(LocalDateTime.now());

        testCustomerDTO = new CustomerDTO();
        testCustomerDTO.setFirstName("Ahmed");
        testCustomerDTO.setLastName("Ben Ali");
        testCustomerDTO.setEmail("ahmed@example.com");
        testCustomerDTO.setPhoneNumber("12345678");
        testCustomerDTO.setAddress("Tunis");
    }

    @Test
    @DisplayName("Should create customer successfully")
    void createCustomer_Success() {
        // Arrange
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // Act
        CustomerResponse result = customerService.createCustomer(testCustomerDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Ahmed", result.getFirstName());
        assertEquals("Ben Ali", result.getLastName());
        assertEquals("ahmed@example.com", result.getEmail());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void createCustomer_EmailExists_ThrowsException() {
        // Arrange
        when(customerRepository.existsByEmail("ahmed@example.com")).thenReturn(true);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> customerService.createCustomer(testCustomerDTO));

        assertTrue(exception.getMessage().contains("Email already exists"));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should get all customers")
    void getAllCustomers_Success() {
        // Arrange
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setFirstName("Mohamed");
        customer2.setLastName("Salah");
        customer2.setEmail("mohamed@example.com");
        customer2.setPhoneNumber("87654321");
        customer2.setAddress("Sousse");
        customer2.setCreatedAt(LocalDateTime.now());

        when(customerRepository.findAll()).thenReturn(Arrays.asList(testCustomer, customer2));

        // Act
        List<CustomerResponse> result = customerService.getAllCustomers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get customer by ID")
    void getCustomerById_Success() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        // Act
        CustomerResponse result = customerService.getCustomerById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Ahmed", result.getFirstName());
        assertEquals(1L, result.getId());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when customer not found")
    void getCustomerById_NotFound_ThrowsException() {
        // Arrange
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerService.getCustomerById(99L));

        assertTrue(exception.getMessage().contains("Customer not found"));
    }

    @Test
    @DisplayName("Should update customer successfully")
    void updateCustomer_Success() {
        // Arrange
        CustomerDTO updateDTO = new CustomerDTO();
        updateDTO.setFirstName("Ahmed Updated");
        updateDTO.setLastName("Ben Ali Updated");
        updateDTO.setEmail("ahmed@example.com");
        updateDTO.setPhoneNumber("99999999");
        updateDTO.setAddress("Sfax");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(1L);
        updatedCustomer.setFirstName("Ahmed Updated");
        updatedCustomer.setLastName("Ben Ali Updated");
        updatedCustomer.setEmail("ahmed@example.com");
        updatedCustomer.setPhoneNumber("99999999");
        updatedCustomer.setAddress("Sfax");
        updatedCustomer.setCreatedAt(LocalDateTime.now());

        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        // Act
        CustomerResponse result = customerService.updateCustomer(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Ahmed Updated", result.getFirstName());
        assertEquals("Sfax", result.getAddress());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should delete customer successfully")
    void deleteCustomer_Success() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        doNothing().when(customerRepository).delete(testCustomer);

        // Act
        customerService.deleteCustomer(1L);

        // Assert
        verify(customerRepository, times(1)).delete(testCustomer);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent customer")
    void deleteCustomer_NotFound_ThrowsException() {
        // Arrange
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> customerService.deleteCustomer(99L));

        verify(customerRepository, never()).delete(any(Customer.class));
    }

    @Test
    @DisplayName("Should search customers by keyword")
    void searchCustomers_Success() {
        // Arrange
        when(customerRepository.searchByKeyword("Ahmed")).thenReturn(Arrays.asList(testCustomer));

        // Act
        List<CustomerResponse> result = customerService.searchCustomers("Ahmed");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Ahmed", result.get(0).getFirstName());
    }
}
