package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.entity.client.Client;
import com.citabella.citabellaapi.entity.employee.Employee;
import com.citabella.citabellaapi.entity.enums.Gender;
import com.citabella.citabellaapi.entity.enums.UsageType;
import com.citabella.citabellaapi.entity.product.Product;
import com.citabella.citabellaapi.entity.security.Role;
import com.citabella.citabellaapi.entity.security.User;
import com.citabella.citabellaapi.entity.treatment.Treatment;
import com.citabella.citabellaapi.exception.ResourceNotFoundException;
import com.citabella.citabellaapi.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Transactional
@AllArgsConstructor
@Component
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final TreatmentRepository treatmentRepository;
    private final ProductRepository productRepository;



    @PostConstruct
    public void init() {
        createRoleIfNotExist("ADMIN", "System administrator");
        createRoleIfNotExist("EMPLOYEE", "Employee of the company");
        createRoleIfNotExist("USER", "User without anything assigned");
        createRoleIfNotExist("CLIENT", "Verified client");

        createUserIfNotExist("Admin", "admin@citabella.com", "citabella123", "ADMIN");
        createUserIfNotExist("Desconocido", "desconocido@opa.com", "hola123", "USER");
        createUserIfNotExist("Cliente", "cliente@buenagente.com", "cliente1231", "CLIENT");

        createClientIfNotExist("Ivan", "694474441", LocalDate.of(2003, 1, 9), Gender.MALE);
        createClientIfNotExist("Angela", "12391239", LocalDate.of(2008, 3, 25), Gender.FEMALE);

        createProduct("SuavePelo", "Mascarilla", BigDecimal.valueOf(12), BigDecimal.valueOf(19), UsageType.BOTH, "Tienda Mascarillas", null, null);
        createProduct("CortaPelo", "Tijeras", BigDecimal.valueOf(52), null, UsageType.INTERNAL, "Tienda Pelos", null, null);

        createEmployeeIfNotExist("Ruth Molina", "Jefa", BigDecimal.valueOf(100));
        createEmployeeIfNotExist("Ivan L", "Barredor", BigDecimal.valueOf(0.1));

        createTreatment("Lavar cabeza", "Frotar cabeza, poner jabón, aclarar", 3, BigDecimal.valueOf(0));
        createTreatment("Cortar puntas", "", 3, BigDecimal.valueOf(0));
    }

    private void createRoleIfNotExist(String name, String description) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = new Role();
            role.setName(name);
            role.setDescription(description);
            roleRepository.save(role);
        }
    }

    private void createUserIfNotExist(String name, String email, String password, String roleName) {
        if (userRepository.findByUsername(name).isEmpty()) {
            User user = new User();
            user.setUsername(name);
            user.setEmail(email);
            user.setPasswordHash(passwordEncoder.encode(password));
            Role role = roleRepository.findByName(roleName).orElseThrow(()
                    -> new ResourceNotFoundException("Role not found"));
            user.setRole(role);
            userRepository.save(user);
        }
    }

    private void createClientIfNotExist(String name, String phoneNumber, LocalDate birthday, Gender gender) {
        if (clientRepository.existsByPhoneNumber(phoneNumber)) {
            return;
        }
        Client client = new Client();
        client.setName(name);
        client.setPhoneNumber(phoneNumber);
        client.setBirthday(birthday);
        client.setGender(gender);

        clientRepository.save(client);
    }

    private void createTreatment(String name, String description, Integer minimumDuration, BigDecimal price) {
        Treatment treatment = new Treatment();
        treatment.setName(name);
        treatment.setDescription(description);
        treatment.setMinimumDuration(minimumDuration);
        treatment.setPrice(price);

        treatmentRepository.save(treatment);
    }

    private void createEmployeeIfNotExist(String name, String position, BigDecimal commision) {
        if (employeeRepository.existsByName(name)) {
            return;
        }
        Employee employee = new Employee();
        employee.setName(name);
        employee.setPosition(position);
        employee.setCommission(commision);

        employeeRepository.save(employee);
    }

    private void createProduct(String name, String category, BigDecimal purchasePrice, BigDecimal salePrice, UsageType usageType, String supplier, Boolean isCritical, String imageKey) {
        Product product = new Product();
        product.setName(name);
        product.setCategory(category);
        product.setPurchasePrice(purchasePrice);
        product.setSalePrice(salePrice);
        product.setUsageType(usageType);
        product.setSupplier(supplier);
        product.setIsCritical(isCritical);

        productRepository.save(product);
    }




















}
