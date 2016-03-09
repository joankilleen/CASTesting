package org.musicstore.persistence;

import javax.persistence.Persistence;
import org.junit.Test;
import org.musicstore.persistence.testhelper.TestEnvironment;

public class MappingTest {

    @Test
    public void testMapping(){

        // Named Queries are checked
        // Hibernate can also validate the schema against entities (hibernate.hbm2ddl.auto)

        Persistence.createEntityManagerFactory(TestEnvironment.inMemory().getPersistenceUnitName());
        Persistence.createEntityManagerFactory(TestEnvironment.localTestDb().getPersistenceUnitName());
    }


}
