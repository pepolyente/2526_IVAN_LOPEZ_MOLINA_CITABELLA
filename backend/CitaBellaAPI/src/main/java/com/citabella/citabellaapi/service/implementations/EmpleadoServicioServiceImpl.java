package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.entity.employee.Employee;
import com.citabella.citabellaapi.entity.employee.EmployeeTreatment;
import com.citabella.citabellaapi.entity.employee.EmployeeTreatmentId;
import com.citabella.citabellaapi.entity.treatment.Treatment;
import com.citabella.citabellaapi.repository.EmployeeRepository;
import com.citabella.citabellaapi.repository.EmployeeTreatmentRepository;
import com.citabella.citabellaapi.repository.TreatmentRepository;
import com.citabella.citabellaapi.service.interfaces.EmpleadoServicioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class EmpleadoServicioServiceImpl implements EmpleadoServicioService {

    private final EmployeeRepository employeeRepository;
    private final TreatmentRepository treatmentRepository;
    private final EmployeeTreatmentRepository employeeTreatmentRepository;



    @Override
    public void asignar(Integer idEmpleado, Integer idServicio) {

        if (employeeTreatmentRepository
                .existsById_employeeIdAndId_treatmentId(idEmpleado, idServicio)) {
            throw new IllegalArgumentException("El servicio ya está asignado al empleado");
        }

        Employee employee = employeeRepository.findById(idEmpleado).orElseThrow();
        Treatment treatment = treatmentRepository.findById(idServicio).orElseThrow();

        EmployeeTreatment es = new EmployeeTreatment();
        es.setEmployee(employee);
        es.setTreatment(treatment);
        es.setId(new EmployeeTreatmentId(idEmpleado, idServicio));

        employeeTreatmentRepository.save(es);
    }

    /*@Override
    public List<EmpleadoServicio> listarPorEmpleado(Integer idEmpleado) {
        return repo.findByEmpleado_IdEmpleado(idEmpleado);
    }*/
}
