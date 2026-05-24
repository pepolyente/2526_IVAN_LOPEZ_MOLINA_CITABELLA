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

        createClients(45);

        createNormalUsers(6);

        createTreatments();

        createProducts();

        System.out.println("=================================");
        System.out.println("DEMO DATA CARGADA");
        System.out.println("=================================");
    }

    private void createRoles() {

        createRole("ADMIN", "Administrador");
        createRole("EMPLOYEE", "Empleado");
        createRole("CLIENT", "Cliente");
        createRole("USER", "Visitante");
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

        Role employeeRole = roleRepository
                .findByName("EMPLOYEE")
                .orElseThrow();

        String[][] employees = {
                {"Lucia Garcia", "lucia.garcia"},
                {"Sofia Martinez", "sofia.martinez"},
                {"Mateo Lopez", "mateo.lopez"},
                {"Daniel Fernandez", "daniel.fernandez"},
                {"Hugo Ruiz", "hugo.ruiz"},
                {"Pablo Navarro", "pablo.navarro"},
                {"Elena Torres", "elena.torres"},
                {"Carmen Diaz", "carmen.diaz"},
                {"Laura Moreno", "laura.moreno"},
                {"Andrea Sanchez", "andrea.sanchez"}
        };

        for (int i = 0; i < amount && i < employees.length; i++) {

            String fullName = employees[i][0];

            String username = employees[i][1];

            User user = new User();

            user.setUsername(username);

            user.setEmail(
                    username + "@citabella.com"
            );

            user.setPasswordHash(
                    passwordEncoder.encode(adminPassword)
            );

            user.setProfileType(ProfileType.EMPLOYEE);

            user.setAccountStatus(AccountStatus.ACTIVE);

            user.setRole(employeeRole);

            userRepository.save(user);

            Employee employee = new Employee();

            employee.setName(fullName);

            employee.setPosition(
                    EMPLOYEE_POSITIONS[
                            i % EMPLOYEE_POSITIONS.length
                            ]
            );

            employee.setCommission(
                    BigDecimal.valueOf(
                            5 + (i % 15)
                    )
            );

            employee.setUser(user);

            employee.setActive(true);

            employeeRepository.save(employee);
        }
    }

    private void createNormalUsers(int amount) {

        Role userRole = roleRepository
                .findByName("USER")
                .orElseThrow();

        for (int i = 0; i < amount; i++) {

            User user = new User();

            user.setUsername(
                    "user" + (i + 1)
            );

            user.setEmail(
                    "user" + (i + 1) + "@gmail.com"
            );

            user.setPasswordHash(
                    passwordEncoder.encode(adminPassword)
            );

            user.setProfileType(ProfileType.NONE);

            user.setAccountStatus(AccountStatus.ACTIVE);

            user.setRole(userRole);

            userRepository.save(user);
        }
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
                "Corte clásico personalizado",
                30,
                15
        );

        createTreatment(
                "Corte degradado",
                "Fade profesional moderno",
                45,
                18
        );

        createTreatment(
                "Balayage",
                "Mechas balayage naturales",
                120,
                80
        );

        createTreatment(
                "Tinte completo",
                "Coloración completa profesional",
                90,
                60
        );

        createTreatment(
                "Peinado boda",
                "Peinado elegante para eventos",
                60,
                50
        );

        createTreatment(
                "Lavado premium",
                "Lavado hidratante con masaje capilar",
                25,
                12
        );

        createTreatment(
                "Arreglo de barba",
                "Perfilado y arreglo profesional",
                20,
                10
        );

        createTreatment(
                "Botox capilar",
                "Tratamiento reparador intensivo",
                90,
                70
        );

        createTreatment(
                "Alisado keratina",
                "Alisado profesional de larga duración",
                180,
                150
        );

        createTreatment(
                "Mechas clásicas",
                "Iluminación tradicional del cabello",
                100,
                65
        );

        createTreatment(
                "Corte infantil",
                "Corte para niños",
                20,
                12
        );

        createTreatment(
                "Recogido fiesta",
                "Peinado para celebraciones",
                50,
                45
        );

        createTreatment(
                "Tratamiento hidratante",
                "Nutrición profunda capilar",
                40,
                30
        );

        createTreatment(
                "Decoloración",
                "Decoloración profesional",
                120,
                90
        );

        createTreatment(
                "Moldeado",
                "Moldeado y volumen",
                90,
                55
        );

        createTreatment(
                "Extensiones premium",
                "Colocación y adaptación de extensiones naturales",
                150,
                120
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

    private void createProducts() {

        createProduct(
                "Champú Repair Expert",
                "Cabello",
                8.50,
                18.95,
                UsageType.BOTH,
                "L'Oréal Professionnel",
                true,
                "01_product"
        );

        createProduct(
                "Mascarilla Nutritive",
                "Cabello",
                12.00,
                24.90,
                UsageType.BOTH,
                "Kérastase",
                true,
                "02_product"
        );

        createProduct(
                "Cera Mate Barber",
                "Barbería",
                4.20,
                11.95,
                UsageType.BOTH,
                "American Crew",
                false,
                "03_product"
        );

        createProduct(
                "Aceite de Argán Premium",
                "Cabello",
                6.50,
                16.50,
                UsageType.BOTH,
                "Tahe",
                false,
                "04_product"
        );

        createProduct(
                "Spray Protector Térmico",
                "Cabello",
                5.80,
                14.95,
                UsageType.BOTH,
                "Revlon",
                true,
                "05_product"
        );

        createProduct(
                "Gel Fijador Extra Strong",
                "Barbería",
                3.20,
                9.95,
                UsageType.BOTH,
                "American Crew",
                false,
                "06_product"
        );

        createProduct(
                "Laca Profesional Volume",
                "Cabello",
                5.00,
                13.50,
                UsageType.BOTH,
                "Schwarzkopf",
                false,
                "07_product"
        );

        createProduct(
                "Champú Anticaspa",
                "Cabello",
                7.20,
                17.90,
                UsageType.BOTH,
                "Kérastase",
                true,
                "08_product"
        );

        createProduct(
                "Tinte Rubio Ceniza",
                "Coloración",
                9.50,
                22.00,
                UsageType.INTERNAL,
                "L'Oréal Professionnel",
                true,
                "09_product"
        );

        createProduct(
                "Oxidante 20 Vol",
                "Coloración",
                4.00,
                10.50,
                UsageType.INTERNAL,
                "Schwarzkopf",
                true,
                "10_product"
        );

        createProduct(
                "Polvo Decolorante",
                "Coloración",
                11.00,
                26.90,
                UsageType.INTERNAL,
                "Revlon",
                true,
                "11_product"
        );

        createProduct(
                "Peine Profesional Carbono",
                "Herramientas",
                2.80,
                7.50,
                UsageType.BOTH,
                "Eurostil",
                false,
                "12_product"
        );

        createProduct(
                "Cepillo Redondo Cerámico",
                "Herramientas",
                4.50,
                12.95,
                UsageType.BOTH,
                "Termix",
                false,
                "13_product"
        );

        createProduct(
                "Secador Profesional Ionic",
                "Herramientas",
                45.00,
                89.95,
                UsageType.INTERNAL,
                "Parlux",
                true,
                "14_product"
        );

        createProduct(
                "Plancha Ceramic Pro",
                "Herramientas",
                38.00,
                79.95,
                UsageType.INTERNAL,
                "GHD",
                true,
                "15_product"
        );

        createProduct(
                "Espuma Rizos Definidos",
                "Cabello",
                5.50,
                13.90,
                UsageType.BOTH,
                "Tahe",
                false,
                "16_product"
        );

        createProduct(
                "Sérum Reparador",
                "Cabello",
                7.80,
                19.95,
                UsageType.BOTH,
                "Kérastase",
                false,
                "17_product"
        );

        createProduct(
                "After Shave Barber",
                "Barbería",
                4.80,
                12.90,
                UsageType.BOTH,
                "American Crew",
                false,
                "18_product"
        );

        createProduct(
                "Champú Silver",
                "Cabello",
                8.20,
                18.50,
                UsageType.BOTH,
                "Schwarzkopf",
                false,
                "19_product"
        );

        createProduct(
                "Mascarilla Color Protect",
                "Cabello",
                10.50,
                22.90,
                UsageType.BOTH,
                "L'Oréal Professionnel",
                false,
                "20_product"
        );
    }

    private void createProduct(
            String name,
            String category,
            Double purchasePrice,
            Double salePrice,
            UsageType usageType,
            String supplier,
            Boolean isCritical,
            String imageKey
    ) {

        Product product = new Product();

        product.setName(name);

        product.setCategory(category);

        product.setPurchasePrice(
                BigDecimal.valueOf(purchasePrice)
        );

        product.setSalePrice(
                BigDecimal.valueOf(salePrice)
        );

        product.setUsageType(usageType);

        product.setSupplier(supplier);

        product.setIsCritical(isCritical);

        product.setImageKey(imageKey);

        product.setActive(true);

        productRepository.save(product);
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