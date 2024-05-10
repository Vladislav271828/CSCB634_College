package com.mvi.CSCB634College.major;

import com.mvi.CSCB634College.department.Department;
import com.mvi.CSCB634College.department.DepartmentRepository;
import com.mvi.CSCB634College.exception.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MajorService {

    private final MajorRepository majorRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public MajorService(MajorRepository majorRepository, DepartmentRepository departmentRepository, ModelMapper modelMapper) {
        this.majorRepository = majorRepository;
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    public DtoMajor createMajor(DtoMajor dtoMajor, int departmentId) {
        dtoMajor.setDepartmentId(null);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new BadRequestException("Department with id " + departmentId + " not found"));

        Major major = modelMapper.map(dtoMajor, Major.class);
        major.setDepartment(department);

        return modelMapper.map(majorRepository.save(major), DtoMajor.class);
    }

    public DtoMajor getMajorById(Integer id) {

        Major major = majorRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Major with id " + id + " not found."));

        return modelMapper.map(major, DtoMajor.class);
    }

    public List<DtoMajor> getAllByDepartment(Integer departmentId) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new BadRequestException("Department with id " + departmentId + " not found"));

        List<Major> majors = majorRepository.findAllByDepartment(department);

        return majors.stream()
                .map(major -> modelMapper.map(major, DtoMajor.class))
                .collect(Collectors.toList());
    }

    public DtoMajor updateMajor(Integer id, DtoMajor dtoMajor) {

        Major major = majorRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Major with id " + id + " not found."));

        modelMapper.map(dtoMajor, major);

        return modelMapper.map(majorRepository.save(major), DtoMajor.class);
    }


    public void deleteMajor(Integer id) {

        Major major = majorRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Major with id " + id + " not found."));

        majorRepository.delete(major);
    }

}
