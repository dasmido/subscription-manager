package com.mo.application.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mo.application.data.Customer;
import com.mo.application.data.CustomerRepository;

@Service
public class CustomerService {
	private final CustomerRepository customerRepository;
	
	// Constructor
	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	public Optional<Customer> get(Long id) {
		return customerRepository.findById(id);
	}
	
	public Customer update(Customer entity) {
		return customerRepository.save(entity);
	}
	
	public void delete(Long id) {
		customerRepository.deleteById(id);
    }

    public Page<Customer> list(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public Page<Customer> list(Pageable pageable, Specification<Customer> filter) {
        return customerRepository.findAll(filter, pageable);
    }

    public int count() {
        return (int) customerRepository.count();
    }

}
