import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.AddRequest;
import org.apache.directory.api.ldap.model.message.AddRequestImpl;
import org.apache.directory.api.ldap.model.message.AddResponse;
import org.apache.directory.ldap.client.api.DefaultLdapConnectionFactory;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapConnectionPool;
import org.apache.directory.ldap.client.api.ValidatingPoolableLdapConnectionFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.ws.rs.core.Response;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ApplicationScoped
public class Connection {

    @ConfigProperty(name = "ldap.host")
    String ldapHost;

    @ConfigProperty(name = "ldap.port")
    int ldapPort;

    @ConfigProperty(name = "ldap.bindDn")
    String bindDn;

    @ConfigProperty(name = "ldap.bindPassword")
    String bindPassword;

    @ConfigProperty(name = "ldap.connectionTimeout")
    long connectionTimeout;

    @ConfigProperty(name = "ldap.pool.maxActive", defaultValue = "8")
    int poolMaxActive;

    @ConfigProperty(name = "ldap.pool.maxIdle", defaultValue = "8")
    int poolMaxIdle;

    @Getter
    private LdapConnectionPool ldapConnectionPool;

    @ConfigProperty(name = "ldap.base.dn")
    private String baseDn;

    @ConfigProperty(name = "ldap.states.objectClasses.all")
    List<String> tranObjectClasses;

    @PostConstruct
    void init() {
        LdapConnectionConfig config = new LdapConnectionConfig();
        config.setLdapHost(ldapHost);
        config.setLdapPort(ldapPort);
        config.setName(bindDn);
        config.setCredentials(bindPassword);

        DefaultLdapConnectionFactory factory = new DefaultLdapConnectionFactory(config);
        factory.setTimeOut(connectionTimeout);

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setLifo(true);
        poolConfig.setMaxTotal(poolMaxActive);
        poolConfig.setMaxIdle(poolMaxIdle);
        poolConfig.setMinIdle(0);
        poolConfig.setTestOnBorrow(false);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestWhileIdle(false);
        poolConfig.setTimeBetweenEvictionRunsMillis(-1);
        poolConfig.setBlockWhenExhausted(true);

        ldapConnectionPool = new LdapConnectionPool(
                new ValidatingPoolableLdapConnectionFactory(factory), poolConfig);
        log.info("LdapConnectionPool started.");
    }

    public Response addStates(ArrayList<trancategory> tranCategoryList) {
        try (LdapConnection connection = ldapConnectionPool.getConnection()) {
            for (trancategory category : tranCategoryList) {
                String rdn = "transactionGroupId=" + category.getTransactionGroupId();
                String dn = rdn + "," + baseDn;

                Entry entry = new DefaultEntry(dn);
                // entry.add("objectClass", category.getObjectClass() != null ? category.getObjectClass() : objectClass);
                addObjectClasses(entry);
                entry.add("lastActivationDate", category.getLastActivationDate());
                entry.add("creator", category.getCreator());
                entry.add("createdDate", category.getCreatedDate());
                entry.add("lastActivationUser", category.getLastActivationUser());
                entry.add("transactionGroupsUid", category.getTransactionGroupsUid());
                entry.add("description", category.getDescription());
                entry.add("uniqueMember1", category.getUniqueMember());
                entry.add("isActive", category.getIsActive());

                connection.add(entry);
            }
            return Response.ok().build();
        } catch (Exception e) {
            log.error("Error adding entries to LDAP", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error adding entries to LDAP: " + e.getMessage()).build();
        }
    }

    private void addObjectClasses(Entry entry) throws LdapException {
        for(int i=0;i<tranObjectClasses.size();i++){
            entry.add("objectClass",tranObjectClasses.get(i));
        }
    }


    // @WithSpan
    // public AddResponse ldapEntry(LdapConnection connection, Entry entry) throws LdapException, Exception {
    //     AddRequest addRequest = new AddRequestImpl();
    //     addRequest.setEntry(entry);
    //     log.info("LDAP_ADD_REQUEST : " + addRequest.getEntryDn());
    //     log.debug("LDAP_ADD_REQUEST : " + addRequest.toString());
    //     return connection.add(addRequest);
    // }

    public boolean addTestEntry() {
        LdapConnection connection = null;
        try {
            connection = ldapConnectionPool.getConnection();
            Entry entry = new DefaultEntry(
                "transactionGroupId=TG54321,ou=tranCategory,dc=transactionCategory,dc=com",
                "objectClass: transactionCategoryObjectClass",
                "objectClass: top",
                "lastActivationDate: 20240722120000Z",
                "creator: cn=Directory Manager",
                "createdDate: 20240701083000Z",
                "lastActivationUser: cn=Directory Manager",
                "transactionGroupId: TG54321",
                "transactionGroupsUid: UID67890",
                "description:transaction entry",
                "isActive: TRUE",
                "uniqueMember1: cn=Directory Manager"
            );
            connection.add(entry);
            log.info("Test entry added successfully.");
            return true;
        } catch (LdapException e) {
            log.error("Failed to add test entry.", e);
            return false;
        } finally {
            if (connection != null) {
                try {
                    ldapConnectionPool.releaseConnection(connection);
                } catch (LdapException e) {
                    log.error("Failed to release LDAP connection.", e);
                }
            }
        }
    }

    void onStart(@Observes StartupEvent ev) {
        log.info("Application started");
    }

    void onStop(@Observes ShutdownEvent ev) {
        if (ldapConnectionPool != null) {
            ldapConnectionPool.close();
        }
        log.info("Application stopped and LDAP connection pool closed");
    }
}

