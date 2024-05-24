package com.mvi.CSCB634College.department;

import com.mvi.CSCB634College.college.College;
import com.mvi.CSCB634College.college.CollegeRepository;
import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.professor.Professor;
import com.mvi.CSCB634College.professor.ProfessorRepository;
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
    private final ProfessorRepository professorRepository;

    @Autowired
    public DepartmentService(DepartmentRepository DepartmentRepository, ModelMapper modelMapper, CollegeRepository collegeRepository, ProfessorRepository professorRepository) {
        this.DepartmentRepository = DepartmentRepository;
        this.modelMapper = modelMapper;
        this.collegeRepository = collegeRepository;
        this.professorRepository = professorRepository;
    }

    public DtoDepartment createDepartment(DtoDepartment dtoDepartment) throws BadRequestException {

        Department Department = modelMapper.map(dtoDepartment, Department.class);

        if (dtoDepartment.getCollegeId() != null) {
            College college = collegeRepository.findById(dtoDepartment.getCollegeId())
                    .orElseThrow(() -> new BadRequestException("College with id " + dtoDepartment.getCollegeId() + " not found"));
            Department.setCollege(college);
        }
        else throw new BadRequestException("Department must have College");

        if (dtoDepartment.getDepartmentHeadId() != null){
            Professor departmentHead = professorRepository.findById(dtoDepartment.getDepartmentHeadId())
                    .orElseThrow(() -> new BadRequestException("User with id " + dtoDepartment.getDepartmentHeadId() + " not found"));
            Department.setDepartmentHead(departmentHead);
        }

        return modelMapper.map(DepartmentRepository.save(Department), DtoDepartment.class);
    }

    public DtoDepartment getDepartmentById(Long id) throws BadRequestException {

        Department Department = DepartmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Department with id " + id + " not found."));

        return modelMapper.map(Department, DtoDepartment.class);
    }

    public List<DtoDepartment> getAllByCollege(Long collegeId) {

        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new BadRequestException("College with id " + collegeId + " not found"));

        List<Department> departments = DepartmentRepository.findAllByCollege(college);

        return departments.stream()
                .map(Department -> modelMapper.map(Department, DtoDepartment.class))
                .collect(Collectors.toList());
    }

    public DtoDepartment updateDepartment(Long id, DtoDepartment dtoDepartment) throws BadRequestException {

        Department Department = DepartmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Department with id " + id + " not found."));
        modelMapper.map(dtoDepartment, Department);

        if (dtoDepartment.getCollegeId() != null && !dtoDepartment.getCollegeId().equals(Department.getCollege().getId())) {
            College college = collegeRepository.findById(dtoDepartment.getCollegeId())
                    .orElseThrow(() -> new BadRequestException("College with id " + dtoDepartment.getCollegeId() + " not found"));

            Department.setCollege(college);
        }
        if (dtoDepartment.getDepartmentHeadId() != null && !dtoDepartment.getDepartmentHeadId().equals(Department.getDepartmentHead().getId())){
            Professor departmentHead = professorRepository.findById(dtoDepartment.getDepartmentHeadId())
                    .orElseThrow(() -> new BadRequestException("User with id " + dtoDepartment.getDepartmentHeadId() + " not found"));
            Department.setDepartmentHead(departmentHead);
        }



        return modelMapper.map(DepartmentRepository.save(Department), DtoDepartment.class);
    }

    public void deleteDepartment(Long id) throws BadRequestException {

        Department Department = DepartmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Department with id " + id + " not found."));

        DepartmentRepository.delete(Department);
    }
}
