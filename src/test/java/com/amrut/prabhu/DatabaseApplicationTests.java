package com.amrut.prabhu;

import eu.rekawek.toxiproxy.model.ToxicDirection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.ToxiproxyContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DatabaseApplicationTests {

    @Autowired
    private PostRepository postRepository;
    private static Network network = Network.newNetwork();
    private static ToxiproxyContainer.ContainerProxy dbProxy;
    @Container
    private static GenericContainer mySQLContainer = new MySQLContainer(DockerImageName.parse("mysql/mysql-server:5.7").asCompatibleSubstituteFor("mysql"))
            .withDatabaseName("database")
            .withUsername("user1")
            .withPassword("password")
            .withNetwork(network);

    @Container
    public static ToxiproxyContainer toxiProxy = new ToxiproxyContainer()
            .withNetwork(network);

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> createDBProxy());
    }

    private static String createDBProxy() {
        dbProxy = toxiProxy.getProxy(mySQLContainer, 3306);
        return "jdbc:mysql://" + dbProxy.getContainerIpAddress() + ":" + dbProxy.getProxyPort() + "/database";
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void checkTimeoutConditionWithDatabase() {

        assertThat(postRepository.findAll())
                .isEmpty();
        try {
            dbProxy.toxics()
                    .timeout("timeout", ToxicDirection.DOWNSTREAM, 1000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertThatThrownBy(() -> postRepository.findAll())
                .isInstanceOf(Exception.class);

    }

}



