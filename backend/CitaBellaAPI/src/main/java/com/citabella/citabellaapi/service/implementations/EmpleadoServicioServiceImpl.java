package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.entity.employee.Employee;
import com.citabella.citabellaapi.entity.employee.EmployeeTreatment;
import com.citabella.citabellaapi.entity.employee.EmployeeTreatmentId;
import com.citabella.citabellaapi.entity.treatment.Treatment;
import com.citabella.citabellaapi.repository.EmpleadoRepository;
import com.citabella.citabellaapi.repository.EmpleadoServicioRepository;
import com.citabella.citabellaapi.repository.ServicioRepository;
import com.citabella.citabellaapi.service.interfaces.EmpleadoServicioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class EmpleadoServicioServiceImpl implements EmpleadoServicioService {

    private final EmpleadoRepository empleadoRepository;
    private final ServicioRepository servicioRepository;
    private final EmpleadoServicioRepository empleadoServicioRepository;



    @Override
    public void asignar(Integer idEmpleado, Integer idServicio) {

        if (empleadoServicioRepository
                .existsById_IdEmpleadoAndId_IdServicio(idEmpleado, idServicio)) {
            throw new IllegalArgumentException("El servicio ya está asignado al empleado");
        }

        Employee employee = empleadoRepository.findById(idEmpleado).orElseThrow();
        Treatment treatment = servicioRepository.findById(idServicio).orElseThrow();

        EmployeeTreatment es = new EmployeeTreatment();
        es.setEmployee(employee);
        es.setTreatment(treatment);
        es.setId(new EmployeeTreatmentId(idEmpleado, idServicio));

        empleadoServicioRepository.save(es);
    }

    /*@Override
    public List<EmpleadoServicio> listarPorEmpleado(Integer idEmpleado) {
        return repo.findByEmpleado_IdEmpleado(idEmpleado);
    }*/
}
