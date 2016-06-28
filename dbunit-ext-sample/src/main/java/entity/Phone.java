package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Phone {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "RID")
  private long id;
  
  @ManyToOne
  @JoinColumn(name = "COSTOMER_RID", referencedColumnName = "RID")
  private Customer customer;

  @Column(name = "PHONE_NUMBER")
  private String phoneNumber;
  
  @Column(name = "PHONE_TYPE")
  private String phoneType;
  
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getPhoneType() {
    return phoneType;
  }

  public void setPhoneType(String phoneType) {
    this.phoneType = phoneType;
  }
  
}
