package com.mvi.CSCB634College.department;

import com.mvi.CSCB634College.college.College;
import com.mvi.CSCB634College.college.CollegeRepository;
import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.exception.UserNotFound;
import com.mvi.CSCB634College.professor.Professor;
import com.mvi.CSCB634College.professor.ProfessorRepository;
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
    private final ProfessorRepository professorRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository, ModelMapper modelMapper, CollegeRepository collegeRepository, ProfessorRepository professorRepository) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
        this.collegeRepository = collegeRepository;
        this.professorRepository = professorRepository;
    }

    //TODO update it such that the logic for update department also applies to create department
    public DtoDepartment createDepartment(DtoDepartment dtoDepartment) throws BadRequestException {

        Department department = modelMapper.map(dtoDepartment, Department.class);
        department.setId(null);
//        Department department = new Department();
        if (dtoDepartment.getCollegeId() != null) {
            College college = collegeRepository.findById(dtoDepartment.getCollegeId())
                    .orElseThrow(() -> new BadRequestException("College with id " + dtoDepartment.getCollegeId() + " not found"));
            department.setCollege(college);
        } else throw new BadRequestException("Department must have College");

        //If there is no professor provided we will not set one
        if (dtoDepartment.getDepartmentHeadId() != null) {
            Professor departmentHead = professorRepository.findById(dtoDepartment.getDepartmentHeadId())
                    .orElseThrow(() -> new BadRequestException("Professor with id " + dtoDepartment.getDepartmentHeadId() + " not found"));
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

        List<Department> departments = departmentRepository.findAllByCollege(college);

        return departments.stream()
                .map(Department -> modelMapper.map(Department, DtoDepartment.class))
                .collect(Collectors.toList());
    }

    public DtoDepartment updateDepartment(Long id, DtoDepartment dtoDepartment) throws BadRequestException {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Department with id " + id + " not found."));
        //modelMapper.map(dtoDepartment, department);

        if (dtoDepartment.getCollegeId() != null && !dtoDepartment.getCollegeId().equals(department.getCollege().getId())) {
            College college = collegeRepository.findById(dtoDepartment.getCollegeId())
                    .orElseThrow(() -> new BadRequestException("College with id " + dtoDepartment.getCollegeId() + " not found"));

            department.setCollege(college);
            department = departmentRepository.save(department);
        }




            if (dtoDepartment.getDepartmentHeadId() != null) {
                if (!professorRepository.existsById(dtoDepartment.getDepartmentHeadId())){
                    throw new UserNotFound("Professor with id " + dtoDepartment.getDepartmentHeadId() + " doesn't exist or isn't a professor");
                }


                if (department.getDepartmentHead() != null) {

                    if (department.getDepartmentHead().getId().equals(dtoDepartment.getDepartmentHeadId())) {
                        throw new BadRequestException("Professor with ID " + department.getDepartmentHead().getId() + " is already a department head of this department.");
                    } else {

                        //in this case we want to change the existing department head with a new one, so we need to get the old one and remove them as well as update their professor entity to reflect the change
                        //first get the old dude and retire him as well as update his entity
                        Professor oldDepartmentHead = professorRepository.findById(department.getDepartmentHead().getId()).orElseThrow();
                        oldDepartmentHead.setDepartment(null);
                        professorRepository.save(oldDepartmentHead);


                        //now is the candidate departmenting elsewhere
                        Professor departmentHeadCandidate = professorRepository.findById(dtoDepartment.getDepartmentHeadId()).orElseThrow();
                        if (departmentRepository.isUserAlreadyDepartmentHead(departmentHeadCandidate.getId())){

                            //update the other department to remove the candidate
                            Department otherDepartment = departmentRepository.findByDepartmentHead_Id(departmentHeadCandidate.getId()).orElseThrow();
                            otherDepartment.setDepartmentHead(null);
                            departmentRepository.save(otherDepartment);
                        }


                        department.setDepartmentHead(departmentHeadCandidate);
                        departmentRepository.save(department);
                    }

                } else {
                    Professor departmentHead = professorRepository.findById(dtoDepartment.getDepartmentHeadId())
                            .orElseThrow(() -> new BadRequestException("Professor with id " + dtoDepartment.getDepartmentHeadId() + " not found"));


                    //is candidate departmenting elsewhere if so then remove them from there
                    if (departmentRepository.isUserAlreadyDepartmentHead(departmentHead.getId())) {
                        Department otherDepartment = departmentRepository.findByDepartmentHead_Id(departmentHead.getId()).orElseThrow();
                        otherDepartment.setDepartmentHead(null);
                        departmentRepository.save(otherDepartment);
                    }

                    //We set and save the changes made to the department head to the department
                    department.setDepartmentHead(departmentHead);
                    department = departmentRepository.save(department);

                    //then we set and save the changes for the professor
                    departmentHead.setDepartment(department);
                    professorRepository.save(departmentHead);
                }

            }



        return modelMapper.map(department, DtoDepartment.class);
    }

    public void deleteDepartment(Long id) throws BadRequestException {

        Department Department = departmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Department with id " + id + " not found."));

        departmentRepository.delete(Department);
    }
}
