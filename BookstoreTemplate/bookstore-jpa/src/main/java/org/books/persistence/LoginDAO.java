package org.books.persistence;


import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.books.data.dto.LoginDTO;
import org.books.data.entity.CustomerEntity;
import org.books.data.entity.LoginEntity;

@Stateless
public class LoginDAO extends GenericDAO<LoginEntity> {

    private static final Logger logger = Logger.getLogger(LoginDAO.class.getName());
    
    public LoginDTO find(String name) {
        
        LoginDTO login = new LoginDTO();
        login.setUserName(name);
        LoginEntity entity = find(login);
        if (entity==null) return null;
        login.setPassword(entity.getPassword());
        return login;
    }
    protected LoginEntity find(LoginDTO login){
        TypedQuery<LoginEntity> query = this.entityManager.createNamedQuery("LoginEntity.ByUserName", LoginEntity.class);
        query.setParameter("userName", login.getUserName());
        LoginEntity entity = null;
        try {
            entity = query.getSingleResult();
        }
        catch (NoResultException ex){
            return null;
        }
        return entity;
    }
    public LoginDTO copyEntitytoDTO(LoginEntity entity){
        LoginDTO login = new LoginDTO();
        login.setUserName(entity.getUserName());
        login.setPassword(entity.getPassword());
        return login;
    }
    

    public LoginDTO update(LoginDTO login) {
        LoginEntity entity =  find(login);
        if (entity == null) return null;
        log("update entity: " + entity.getClass().toString());
        entity.setPassword(login.getPassword());
        entity = super.update(entity);
        return copyEntitytoDTO(entity);
    }
    
    public LoginDTO create(LoginDTO login) {
       
        LoginEntity entity = new LoginEntity();
        entity.setUserName(login.getUserName());
        entity.setPassword(login.getPassword());
        entity = super.create(entity);
        return copyEntitytoDTO(entity);
    }
    
    public void delete(LoginDTO login) {
        LoginEntity entity = find(login);
        log("remove entity: " + entity.getClass().toString());
        super.delete(entity);
    }

}
