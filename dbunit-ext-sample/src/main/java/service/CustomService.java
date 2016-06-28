package service;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import entity.Customer;
import entity.Phone;
import repository.CustomerRepository;

@Service
public class CustomService {
  
  @Autowired
  private CustomerRepository customerRepository;
  
  @Transactional
  public void addCustomerPhone(String lastName, String phoneNumber) {
    
    Assert.notNull(lastName);
    Assert.notNull(phoneNumber);
    
    for(Customer customer : customerRepository.findByLastName(lastName)) {
      Phone phone = new Phone();
      phone.setPhoneNumber(phoneNumber);
      phone.setCustomer(customer);
      
      if(phoneNumber.startsWith("02")) {
        phone.setPhoneType("TAIWAN");
      }
      
      customer.getPhones().add(phone);
      customer.setLastModifiedDate(new Date());
    }
  }
  
}
