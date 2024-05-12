package com.mvi.CSCB634College.faculty;

import com.mvi.CSCB634College.college.College;
import com.mvi.CSCB634College.college.CollegeRepository;
import com.mvi.CSCB634College.exception.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final ModelMapper modelMapper;
    private final CollegeRepository collegeRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository, ModelMapper modelMapper, CollegeRepository collegeRepository) {
        this.facultyRepository = facultyRepository;
        this.modelMapper = modelMapper;
        this.collegeRepository = collegeRepository;
    }

    public DtoFaculty createFaculty(DtoFaculty dtoFaculty, int collegeId) throws BadRequestException {
        dtoFaculty.setCollegeId(null);

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

    public List<DtoFaculty> getAllByCollege(Integer collegeId) {

        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new BadRequestException("College with id " + collegeId + " not found"));
            
            List<Faculty> faculties = facultyRepository.findAllByCollege(college);

            return faculties.stream()
                    .map(faculty -> modelMapper.map(faculty, DtoFaculty.class))
                    .collect(Collectors.toList());
        
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
