package com.example.demowithtests.util.config;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.dto.EmployeeDto;
import com.example.demowithtests.dto.EmployeeReadDto;
import org.springframework.stereotype.Component;

@Component
public class EmployeeConverter {

    private final EmployeeMapper employeeMapper;

    public EmployeeConverter(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    public EmployeeDto toDto(Employee entity) {
        return employeeMapper.toDto(entity);
    }

    public EmployeeReadDto toReadDto(Employee entity) {
        return employeeMapper.toReadDto(entity);
    }

    public Employee fromDto(EmployeeDto dto) {
        return employeeMapper.fromDto(dto);
    }
}