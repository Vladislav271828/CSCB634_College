package com.mvi.CSCB634College.department;

import com.mvi.CSCB634College.college.College;
import com.mvi.CSCB634College.college.CollegeRepository;
import com.mvi.CSCB634College.exception.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository DepartmentRepository;
    private final ModelMapper modelMapper;
    private final CollegeRepository collegeRepository;

    @Autowired
    public DepartmentService(DepartmentRepository DepartmentRepository, ModelMapper modelMapper, CollegeRepository collegeRepository) {
        this.DepartmentRepository = DepartmentRepository;
        this.modelMapper = modelMapper;
        this.collegeRepository = collegeRepository;
    }

    public DtoDepartment createDepartment(DtoDepartment dtoDepartment, int collegeId) throws BadRequestException {

        dtoDepartment.setDepartmentHeadId(null);
        dtoDepartment.setCollegeId(null);

        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new BadRequestException("College with id " + collegeId + " not found"));
//trqbwat proverki!!!!
        Department Department = modelMapper.map(dtoDepartment, Department.class);
        Department.setCollege(college);

        return modelMapper.map(DepartmentRepository.save(Department), DtoDepartment.class);
    }

    public DtoDepartment getDepartmentById(Integer id) throws BadRequestException {

        Department Department = DepartmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Department with id " + id + " not found."));

        return modelMapper.map(Department, DtoDepartment.class);
    }

    public List<DtoDepartment> getAllByCollege(Integer collegeId) {

        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new BadRequestException("College with id " + collegeId + " not found"));

        List<Department> departments = DepartmentRepository.findAllByCollege(college);

        return departments.stream()
                .map(Department -> modelMapper.map(Department, DtoDepartment.class))
                .collect(Collectors.toList());

    }

    public DtoDepartment updateDepartment(Integer id, DtoDepartment dtoDepartment) throws BadRequestException {

        Department Department = DepartmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Department with id " + id + " not found."));
        //trqbwat proverki!!!!
        modelMapper.map(dtoDepartment, Department);

        return modelMapper.map(DepartmentRepository.save(Department), DtoDepartment.class);
    }
//nqma profesor oshte :(
//    public DtoDepartment changeDepartmentHead(Integer id, Integer departmentHeadId) throws BadRequestException {
//
//        Department Department = DepartmentRepository.findById(id)
//                .orElseThrow(() -> new BadRequestException("Department with id " + id + " not found."));
//
//        Professor departmentHead = professorRepository.findById(departmentHeadId)
//                .orElseThrow(() -> new BadRequestException("User with id " + departmentHeadId + " not found."));
//
//        Department.setDepartmentHead(departmentHead);
//
//        return modelMapper.map(DepartmentRepository.save(Department), DtoDepartment.class);
//    }

    public void deleteDepartment(Integer id) throws BadRequestException {

        Department Department = DepartmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Department with id " + id + " not found."));

        DepartmentRepository.delete(Department);
    }
}
