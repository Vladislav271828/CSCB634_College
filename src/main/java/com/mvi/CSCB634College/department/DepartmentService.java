package com.mvi.CSCB634College.department;

import com.mvi.CSCB634College.college.College;
import com.mvi.CSCB634College.college.CollegeRepository;
import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.faculty.Faculty;
import com.mvi.CSCB634College.faculty.FacultyRepository;
import com.mvi.CSCB634College.professor.Professor;
import com.mvi.CSCB634College.professor.ProfessorRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    private final CollegeRepository collegeRepository;
    private final FacultyRepository facultyRepository;
    private final ProfessorRepository professorRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository, ModelMapper modelMapper, CollegeRepository collegeRepository, FacultyRepository facultyRepository, ProfessorRepository professorRepository) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
        this.collegeRepository = collegeRepository;
        this.facultyRepository = facultyRepository;
        this.professorRepository = professorRepository;
    }

    public DtoDepartment createDepartment(DtoDepartment dtoDepartment) throws BadRequestException {

        Department department = modelMapper.map(dtoDepartment, Department.class);
        department.setId(null);
        //Department department = new Department();
        if (dtoDepartment.getFacultyId() != null) {
            Faculty faculty = facultyRepository.findById(dtoDepartment.getFacultyId())
                    .orElseThrow(() -> new BadRequestException("Faculty with id " + dtoDepartment.getFacultyId() + " not found"));
            department.setFaculty(faculty);
        } else throw new BadRequestException("Department must have College");

        //If there is no professor provided we will not set one and leave the value as null for the professor to be set later
        if (dtoDepartment.getDepartmentHeadId() != null) {
            Professor departmentHead = professorRepository.findById(dtoDepartment.getDepartmentHeadId())
                    .orElseThrow(() -> new BadRequestException("Professor with id " + dtoDepartment.getDepartmentHeadId() + " not found"));

            //This allows for the user to set a professor whom is a department head already - removes the professor from their old position and sets them here
            if (departmentRepository.isUserAlreadyDepartmentHead(departmentHead.getId())){
                Department otherDepartment = departmentRepository.findByDepartmentHead_Id(departmentHead.getId()).orElseThrow();
                otherDepartment.setDepartmentHead(null);
                departmentRepository.save(otherDepartment);
            }
            departmentHead.setDepartment(department);
            department = departmentRepository.save(department);
            departmentHead = professorRepository.save(departmentHead);

            department.setDepartmentHead(departmentHead);
        }

        department = departmentRepository.save(department);


        return modelMapper.map(department, DtoDepartment.class);
    }

    public DtoDepartment getDepartmentById(Long id) throws BadRequestException {

        Department Department = departmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Department with id " + id + " not found."));

        return modelMapper.map(Department, DtoDepartment.class);
    }

    public List<DtoDepartment> getAllByCollege(Long collegeId) {

        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new BadRequestException("College with id " + collegeId + " not found"));

        List<Department> departments = departmentRepository.findAllByCollege(collegeId);

        return departments.stream()
                .map(Department -> modelMapper.map(Department, DtoDepartment.class))
                .collect(Collectors.toList());
    }

    public List<DtoDepartment> getAllByFaculty(Long facultyId) {

        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new BadRequestException("Faculty with id " + facultyId + " not found"));

        List<Department> departments = departmentRepository.findAllByFaculty(faculty);

        return departments.stream()
                .map(Department -> modelMapper.map(Department, DtoDepartment.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public DtoDepartment updateDepartment(Long id, DtoDepartment dtoDepartment) throws BadRequestException {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Department with id " + id + " not found."));

        if (dtoDepartment.getFacultyId() != null && !dtoDepartment.getFacultyId().equals(department.getFaculty().getId())) {
            Faculty faculty = facultyRepository.findById(dtoDepartment.getFacultyId())
                    .orElseThrow(() -> new BadRequestException("Faculty with id " + dtoDepartment.getFacultyId() + " not found"));

            department.setFaculty(faculty);
            department = departmentRepository.save(department);
        }

        if (dtoDepartment.getDepartmentHeadId() != null) {
            assignDepartmentHead(department, dtoDepartment.getDepartmentHeadId());
        }

        return modelMapper.map(department, DtoDepartment.class);
    }

    @Transactional
    public void assignDepartmentHead(Department department, Integer departmentHeadId) throws BadRequestException {
        Professor departmentHead = professorRepository.findById(departmentHeadId)
                .orElseThrow(() -> new BadRequestException("Professor with id " + departmentHeadId + " not found"));

        if (departmentRepository.isUserAlreadyDepartmentHead(departmentHead.getId())) {
            Department otherDepartment = departmentRepository.findByDepartmentHead_Id(departmentHead.getId())
                    .orElseThrow(() -> new BadRequestException("Department not found for existing department head"));
            otherDepartment.setDepartmentHead(null);
            departmentRepository.save(otherDepartment);
        }

        department.setDepartmentHead(departmentHead);
        departmentRepository.save(department);

        departmentHead.setDepartment(department);
        professorRepository.save(departmentHead);
    }


    public void deleteDepartment(Long id) throws BadRequestException {

        Department Department = departmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Department with id " + id + " not found."));

        departmentRepository.delete(Department);
    }
}
