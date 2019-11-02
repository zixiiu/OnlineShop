package onlineShop.dao;

import onlineShop.model.Customer;
// data access object
public interface CustomerDao {

    void addCustomer(Customer customer);

    Customer getCustomerByUserName(String userName);
}

