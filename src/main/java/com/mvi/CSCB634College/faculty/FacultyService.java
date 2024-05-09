package com.mvi.CSCB634College.faculty;

import com.mvi.CSCB634College.college.College;
import com.mvi.CSCB634College.college.CollegeRepository;
import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.security.auth.AuthenticationService;
import com.mvi.CSCB634College.security.config.JwtAuthenticationFilter;
import com.mvi.CSCB634College.user.UserRepository;
import jdk.swing.interop.SwingInterOpUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final FacultyRepository facultyRepository;
    private final ModelMapper modelMapper;
    private final CollegeRepository collegeRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository, AuthenticationService authenticationService, UserRepository userRepository, ModelMapper modelMapper, CollegeRepository collegeRepository) {
        this.facultyRepository = facultyRepository;
        this.modelMapper = modelMapper;
        this.collegeRepository = collegeRepository;
    }

    public DtoFaculty createFaculty(DtoFaculty dtoFaculty, int collegeId) throws BadRequestException {


        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new BadRequestException("College with id " + collegeId + " not found"));
//trqbwat proverki!!!!
        Faculty faculty = modelMapper.map(dtoFaculty, Faculty.class);
        faculty.setCollege(college);

        return modelMapper.map(facultyRepository.save(faculty), DtoFaculty.class);
    }

    public DtoFaculty getFacultyById(Integer id) throws BadRequestException {

        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Faculty with id " + id + " not found."));

        return modelMapper.map(faculty, DtoFaculty.class);
    }

    public DtoFaculty updateFaculty(Integer id, DtoFaculty dtoFaculty) throws BadRequestException {

        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Faculty with id " + id + " not found."));
        //trqbwat proverki!!!!
        modelMapper.map(dtoFaculty, faculty);

        return modelMapper.map(facultyRepository.save(faculty), DtoFaculty.class);
    }

    public void deleteFaculty(Integer id) throws BadRequestException {

        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Faculty with id " + id + " not found."));

        facultyRepository.delete(faculty);
    }
}
