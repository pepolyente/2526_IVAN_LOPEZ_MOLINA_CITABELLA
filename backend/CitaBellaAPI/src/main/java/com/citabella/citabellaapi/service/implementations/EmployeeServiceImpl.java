package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.employee.EmployeeRequest;
import com.citabella.citabellaapi.dto.employee.EmployeeResponse;
import com.citabella.citabellaapi.entity.employee.Employee;
import com.citabella.citabellaapi.entity.enums.AccountStatus;
import com.citabella.citabellaapi.entity.enums.ProfileType;
import com.citabella.citabellaapi.entity.security.Role;
import com.citabella.citabellaapi.entity.security.User;
import com.citabella.citabellaapi.exception.BadRequestException;
import com.citabella.citabellaapi.exception.ResourceNotFoundException;
import com.citabella.citabellaapi.mappers.EmployeeMapper;
import com.citabella.citabellaapi.repository.EmployeeRepository;
import com.citabella.citabellaapi.repository.RoleRepository;
import com.citabella.citabellaapi.repository.UserRepository;
import com.citabella.citabellaapi.service.interfaces.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public EmployeeResponse create(EmployeeRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new BadRequestException("Name is mandatory");
        }
        if (employeeRepository.existsByName(request.name())) {
            throw new BadRequestException("Employee's name already registered");
        }
        Employee employee = new Employee();
        employee.setName(request.name());
        employee.setPosition(request.position());
        return EmployeeMapper.toResponse(employeeRepository.save(employee));
    }


    @Override
    public EmployeeResponse getById(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return EmployeeMapper.toResponse(employee);
    }

    @Override
    public Page<EmployeeResponse> findAll(Pageable pageable, Boolean active) {
        if (active != null) {
            return employeeRepository.findAllByActive(active, pageable)
                    .map(EmployeeMapper::toResponse);
        }
        return employeeRepository.findAll(pageable)
                .map(EmployeeMapper::toResponse);
    }

    @Override
    public EmployeeResponse update(Integer id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        if (request.name() != null && !request.name().isBlank()) {
            if (!request.name().equals(employee.getName())
                    && employeeRepository.existsByName(request.name())) {
                throw new BadRequestException("Employee's name already registered");
            }
            employee.setName(request.name());
        }
        if (request.position() != null) {
            employee.setPosition(request.position());
        }

        return EmployeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponse deactivate(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        employee.setActive(false);
        return EmployeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponse activate(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        employee.setActive(true);
        return EmployeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public void linkUserAccount(Integer employeeId, Integer userId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (employee.getUser() != null) {
            throw new BadRequestException("Employee already linked to another user");
        }
        try {
            user.assignEmployee(employee);
        } catch (IllegalStateException e) {
            throw new BadRequestException("User already has a profile assigned");
        }

        employee.setUser(user);
        employeeRepository.save(employee);

        Role employeeRole = roleRepository.findByName("EMPLOYEE")
                .orElseThrow(() -> new BadRequestException("EMPLOYEE role not found"));
        user.setRole(employeeRole);
        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
    }
}
