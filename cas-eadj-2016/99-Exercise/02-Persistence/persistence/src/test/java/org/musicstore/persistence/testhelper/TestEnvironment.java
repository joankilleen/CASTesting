package org.musicstore.persistence.testhelper;

public class TestEnvironment {

    // See test/resources/META-INF/persistence.xml for the setup of both persistence units below:
    private static String PU_IN_MEMORY = "EnterpriseMusicStoreTest"; // Test against an in-memory database
    private static String PU_LOCAL = "EnterpriseMusicStoreIntegrationTest"; // Test against a dedicated database

    private String persistenceUnitName;


    private TestEnvironment() {
    }

    public static TestEnvironment inMemory(){
        TestEnvironment env = new TestEnvironment();
        env.persistenceUnitName = PU_IN_MEMORY;
        return env;
    }

    public static TestEnvironment localTestDb(){
        TestEnvironment env = new TestEnvironment();
        env.persistenceUnitName = PU_LOCAL;
        return env;
    }

    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }


}
