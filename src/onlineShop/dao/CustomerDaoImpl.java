package onlineShop.dao;

import onlineShop.model.Authorities;
import onlineShop.model.Cart;
import onlineShop.model.Customer;
import onlineShop.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.jws.soap.SOAPBinding;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class CustomerDaoImpl implements CustomerDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addCustomer(Customer customer) {
        customer.getUser().setEnabled(true);

        Authorities authorities = new Authorities();
        authorities.setEmailId(customer.getUser().getEmailId());
        authorities.setAuthorities("ROLE_USER");

        Cart cart = new Cart();
        cart.setCustomer(customer);
        customer.setCart(cart);

        Session session = null;
        try{
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(authorities);
            session.save(customer);
            session.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    @Override
    public Customer getCustomerByUserName(String userName) {
        User user = null;
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            criteriaQuery.select(root)
                    .where(builder.equal(root.get("emailId"), userName));
            // SELECT * FROM user WHERE emailID = userName
            user = session.createQuery(criteriaQuery).getSingleResult();
        } catch (Exception e){
            e.printStackTrace();
        }

        return user == null ? null : user.getCustomer();
    }
}
