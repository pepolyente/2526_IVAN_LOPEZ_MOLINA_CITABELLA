package com.citabella.citabellaapi.config.demo;

import com.citabella.citabellaapi.entity.client.Client;
import com.citabella.citabellaapi.entity.employee.Employee;
import com.citabella.citabellaapi.entity.enums.*;
import com.citabella.citabellaapi.entity.product.Product;
import com.citabella.citabellaapi.entity.security.Role;
import com.citabella.citabellaapi.entity.security.User;
import com.citabella.citabellaapi.entity.treatment.Treatment;
import com.citabella.citabellaapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DemoDataInitializer {

    @Value("${app.demo.enabled:false}")
    private boolean enabled;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final TreatmentRepository treatmentRepository;

    private final PasswordEncoder passwordEncoder;

    private final Random random = new Random();

    private static final String[] FIRST_NAMES = {
            "Lucia", "Sofia", "Mateo", "Daniel", "Hugo",
            "Pablo", "Elena", "Carmen", "Laura", "Andrea",
            "Raul", "Aitana", "David", "Miguel", "Sandra",
            "Paula", "Ruben", "Javier", "Cristina", "Natalia",
            "Mario", "Claudia", "Ivan", "Adrian", "Sara"
    };

    private static final String[] LAST_NAMES = {
            "Garcia", "Martinez", "Lopez", "Fernandez",
            "Ruiz", "Navarro", "Torres", "Diaz",
            "Moreno", "Sanchez", "Romero", "Vega",
            "Herrera", "Gil", "Flores", "Cano",
            "Iglesias", "Leon", "Ortega", "Castro"
    };

    private static final String[] EMPLOYEE_POSITIONS = {
            "Estilista",
            "Barbero",
            "Colorista",
            "Recepcionista"
    };

    @EventListener(ApplicationReadyEvent.class)
    public void init() {

        if (!enabled) {
            return;
        }

        if (userRepository.count() > 0) {
            return;
        }

        System.out.println("=================================");
        System.out.println("CARGANDO DEMO DATA...");
        System.out.println("=================================");

        createRoles();

        createAdmin();

        createEmployees(10);

        createClients(250);

        createTreatments();

        createProducts(120);

        System.out.println("=================================");
        System.out.println("DEMO DATA CARGADA");
        System.out.println("=================================");
    }

    private void createRoles() {

        createRole("ADMIN", "Administrador");
        createRole("EMPLOYEE", "Empleado");
        createRole("CLIENT", "Cliente");
    }

    private void createRole(String name, String description) {

        if (roleRepository.findByName(name).isPresent()) {
            return;
        }

        Role role = new Role();

        role.setName(name);
        role.setDescription(description);

        roleRepository.save(role);
    }

    private void createAdmin() {

        Role adminRole = roleRepository
                .findByName("ADMIN")
                .orElseThrow();

        User admin = new User();

        admin.setUsername(adminUsername);

        admin.setEmail(adminEmail);

        admin.setPasswordHash(
                passwordEncoder.encode(adminPassword)
        );

        admin.setProfileType(ProfileType.ADMIN);

        admin.setAccountStatus(AccountStatus.ACTIVE);

        admin.setRole(adminRole);

        userRepository.save(admin);
    }

    private void createEmployees(int amount) {

        for (int i = 0; i < amount; i++) {

            String fullName = randomName();

            Employee employee = new Employee();

            employee.setName(fullName);

            employee.setPosition(
                    randomPosition()
            );

            employee.setCommission(
                    BigDecimal.valueOf(
                            5 + random.nextInt(15)
                    )
            );

            employeeRepository.save(employee);

            createEmployeeUser(fullName, i);
        }
    }

    private void createEmployeeUser(
            String fullName,
            int index
    ) {

        Role employeeRole = roleRepository
                .findByName("EMPLOYEE")
                .orElseThrow();

        User user = new User();

        user.setUsername(
                "employee" + index
        );

        user.setEmail(
                "employee" + index + "@citabella.com"
        );

        user.setPasswordHash(
                passwordEncoder.encode(adminPassword)
        );

        user.setProfileType(ProfileType.EMPLOYEE);

        user.setAccountStatus(AccountStatus.ACTIVE);

        user.setRole(employeeRole);

        userRepository.save(user);
    }

    private void createClients(int amount) {

        for (int i = 0; i < amount; i++) {

            Client client = new Client();

            client.setName(
                    randomName()
            );

            client.setPhoneNumber(
                    randomPhone()
            );

            client.setBirthday(
                    randomBirthday()
            );

            client.setGender(
                    random.nextBoolean()
                            ? Gender.MALE
                            : Gender.FEMALE
            );

            clientRepository.save(client);
        }
    }

    private void createTreatments() {

        createTreatment(
                "Corte de pelo",
                "Corte clásico",
                30,
                15
        );

        createTreatment(
                "Corte degradado",
                "Fade profesional",
                45,
                18
        );

        createTreatment(
                "Balayage",
                "Mechas balayage",
                120,
                80
        );

        createTreatment(
                "Tinte completo",
                "Coloración completa",
                90,
                60
        );

        createTreatment(
                "Peinado boda",
                "Peinado elegante",
                60,
                50
        );

        createTreatment(
                "Lavado premium",
                "Lavado y masaje",
                25,
                12
        );

        createTreatment(
                "Arreglo de barba",
                "Perfilado profesional",
                20,
                10
        );
    }

    private void createTreatment(
            String name,
            String description,
            Integer duration,
            Integer price
    ) {

        Treatment treatment = new Treatment();

        treatment.setName(name);

        treatment.setDescription(description);

        treatment.setMinimumDuration(duration);

        treatment.setPrice(
                BigDecimal.valueOf(price)
        );

        treatmentRepository.save(treatment);
    }

    private void createProducts(int amount) {

        String[] productNames = {
                "Champu Repair",
                "Mascarilla Pro",
                "Cera Mate",
                "Aceite Argan",
                "Spray Volume",
                "Gel Fijador",
                "Laca Premium",
                "Peine Carbono",
                "Secador Pro",
                "Plancha Ceramic"
        };

        String[] brands = {
                "Loreal",
                "Kerastase",
                "Revlon",
                "Schwarzkopf",
                "American Crew",
                "Tahe"
        };

        String[] categories = {
                "Cabello",
                "Barberia",
                "Herramientas",
                "Coloracion"
        };

        for (int i = 0; i < amount; i++) {

            Product product = new Product();

            product.setName(
                    productNames[random.nextInt(productNames.length)]
                            + " "
                            + (100 + i)
            );

            product.setCategory(
                    categories[random.nextInt(categories.length)]
            );

            product.setPurchasePrice(
                    BigDecimal.valueOf(
                            3 + random.nextInt(40)
                    )
            );

            product.setSalePrice(
                    BigDecimal.valueOf(
                            10 + random.nextInt(80)
                    )
            );

            product.setUsageType(
                    random.nextBoolean()
                            ? UsageType.BOTH
                            : UsageType.INTERNAL
            );

            product.setSupplier(
                    brands[random.nextInt(brands.length)]
            );

            product.setIsCritical(
                    random.nextBoolean()
            );

            productRepository.save(product);
        }
    }

    private String randomName() {

        return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)]
                + " "
                + LAST_NAMES[random.nextInt(LAST_NAMES.length)];
    }

    private String randomPosition() {

        return EMPLOYEE_POSITIONS[
                random.nextInt(EMPLOYEE_POSITIONS.length)
                ];
    }

    private String randomPhone() {

        return "6"
                + (10000000 + random.nextInt(89999999));
    }

    private LocalDate randomBirthday() {

        return LocalDate.of(
                1960 + random.nextInt(45),
                1 + random.nextInt(12),
                1 + random.nextInt(28)
        );
    }
}