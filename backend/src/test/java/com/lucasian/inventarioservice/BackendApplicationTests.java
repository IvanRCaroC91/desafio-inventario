package com.lucasian.inventarioservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
})
@ActiveProfiles("test")
@Disabled("Requiere PostgreSQL o configuración de DB de test para levantar el contexto")
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
