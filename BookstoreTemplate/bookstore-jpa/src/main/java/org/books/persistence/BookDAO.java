package org.books.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;
import org.books.data.entity.BookEntity;
import org.books.data.entity.BookEntity_;

@Stateless
public class BookDAO extends GenericDAO<BookEntity> {

    
    public List<BookInfo> search(String keywordStr) {
        
        // Search using space separated keywords. Each keyword must occur at least once in either the title, authors or publisher fields 
        // The example in comments shows 3 keyword parameters: 'ee' and 'burke' and 'java'
        // The keywords should be separated by one or more space characters.
        
        /*//JPQL
         select b from BookEntity b where (upper(b.publisher) like upper('%ee%') or upper(b.authors) like upper('%ee%') or upper(b.title) like upper('%ee%'))
         and (upper(b.publisher) like upper('%burke%') or upper(b.authors) like upper('%burke%') or upper(b.title) like upper('%burke%'))
         and (upper(b.publisher) like upper('%java%') or upper(b.authors) like upper('%java%') or upper(b.title) like upper('%java%'))
         */
        
        // Separate out the keywords, add them to an array, make uppercase, and concatenate % at the front and back of each keyword
        StringTokenizer strTkn = new StringTokenizer(keywordStr, " ");
        List<String> keywords = new ArrayList<String>(keywordStr.length());

        while (strTkn.hasMoreTokens()) {
            StringBuilder nextKeyword = new StringBuilder("%");
            nextKeyword.append(strTkn.nextToken());
            nextKeyword.append("%");
            keywords.add(nextKeyword.toString().toUpperCase());
        }

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<BookInfo> c = cb.createQuery(BookInfo.class);
        Root<BookEntity> b = c.from(BookEntity.class);
        
        // use multiselect because BookInfo objects will be returned
        c.multiselect(b.get("isbn"), b.get("title"), b.get("price"));

        List<Predicate> andConditions = new ArrayList<>();
        
        // for each keyowrd build OR condition for title, publisher, and authors
        for (int i = 0; i < keywords.size(); i++) {
            Predicate subConditionOr = cb.or(cb.like(cb.upper(b.get(BookEntity_.authors)), keywords.get(i)),
                    cb.like(cb.upper(b.get(BookEntity_.title)), keywords.get(i)),
                    cb.like(cb.upper(b.get(BookEntity_.publisher)), keywords.get(i)));
            
            // Chain the OR conditions together with AND conditions, to make sure each keyword occurs at least once
            andConditions.add(subConditionOr);
        }
        if (andConditions.size() == 1) {
            c.where(andConditions.get(0));
        } else {
            c.where(cb.and(andConditions.toArray(new Predicate[0])));
        }
        TypedQuery<BookInfo> q = getEntityManager().createQuery(c);
        return q.getResultList();
        
    }
    public BookDTO find(String isbn){
        BookEntity bookEntity = queryBook(isbn);
        if (bookEntity == null) return null;
        return BookDTO.copyEntitytoDTO(bookEntity);
    }
    
    public BookEntity find(BookDTO book){
        TypedQuery<BookEntity> q = entityManager.createNamedQuery("Book.FindByIsbn", BookEntity.class);
        BookEntity bookEntity = null;
        q.setParameter("isbn", book.getIsbn()); 
        try  {
            bookEntity =  q.getSingleResult();
        }
        catch(NoResultException ex){
            return null;
 
        }
        return bookEntity;
    }
    public BookEntity find(BookInfo book){
        return queryBook(book.getIsbn());
    }
    public BookDTO update(BookDTO book){
        BookEntity entity = find(book);
        entity.setTitle(book.getTitle());
        entity.setAuthors(book.getAuthors());
        entity.setBinding(book.getBinding());
        entity.setPublisher(book.getPublisher());
        entity.setPublicationYear(book.getPublicationYear());
        entity.setNumberOfPages(book.getNumberOfPages());
        entity.setPrice(book.getPrice());
        entity = super.update(entity);
        return BookDTO.copyEntitytoDTO(entity);
    }
    
    
    
    public void delete(BookDTO book){
        BookEntity entity = find(book);
        log("remove entity: " + entity.getClass().toString());
        super.delete(entity);
    }
    
    public BookDTO create(BookDTO book){
        BookEntity entity = new BookEntity(book.getIsbn(), book.getTitle(), book.getAuthors(), 
                        book.getPublisher(), book.getPublicationYear(), 
                        book.getBinding(), book.getNumberOfPages(), book.getPrice());
        entity = super.create(entity);
        return BookDTO.copyEntitytoDTO(entity);
    }
    public BookEntity queryBook(String isbn){
        BookEntity bookEntity = null;
        TypedQuery<BookEntity> q = entityManager.createNamedQuery("Book.FindByIsbn", BookEntity.class);
        q.setParameter("isbn", isbn);
        try  {
            bookEntity =  q.getSingleResult();
        }
        catch(NoResultException ex){
            return null;
        }
        return bookEntity;
    }

}
