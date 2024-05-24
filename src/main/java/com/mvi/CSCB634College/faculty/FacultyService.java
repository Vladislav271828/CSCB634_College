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

    public DtoFaculty createFaculty(DtoFaculty dtoFaculty) throws BadRequestException {

        Faculty faculty = modelMapper.map(dtoFaculty, Faculty.class);

        if (dtoFaculty.getCollegeId() != null) {
        College college = collegeRepository.findById(dtoFaculty.getCollegeId())
                .orElseThrow(() -> new BadRequestException("College with id " + dtoFaculty.getCollegeId() + " not found"));
            faculty.setCollege(college);
        }
        else throw new BadRequestException("Faculty must have College");

        return modelMapper.map(facultyRepository.save(faculty), DtoFaculty.class);
    }

    public DtoFaculty getFacultyById(Long id) throws BadRequestException {

        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Faculty with id " + id + " not found."));

        return modelMapper.map(faculty, DtoFaculty.class);
    }

    public List<DtoFaculty> getAllByCollege(Long collegeId) {

        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new BadRequestException("College with id " + collegeId + " not found"));
            
            List<Faculty> faculties = facultyRepository.findAllByCollege(college);

            return faculties.stream()
                    .map(faculty -> modelMapper.map(faculty, DtoFaculty.class))
                    .collect(Collectors.toList());
        
    }

    public DtoFaculty updateFaculty(Long id, DtoFaculty dtoFaculty) throws BadRequestException {

        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Faculty with id " + id + " not found."));
        modelMapper.map(dtoFaculty, faculty);
        //dali ima smisul da e pozvoleno da se smenq?
        if (dtoFaculty.getCollegeId() != null && !dtoFaculty.getCollegeId().equals(faculty.getCollege().getId())) {
            College college = collegeRepository.findById(dtoFaculty.getCollegeId())
                    .orElseThrow(() -> new BadRequestException("College with id " + dtoFaculty.getCollegeId() + " not found"));
            faculty.setCollege(college);
        }
        


        return modelMapper.map(facultyRepository.save(faculty), DtoFaculty.class);
    }

    public void deleteFaculty(Long id) throws BadRequestException {

        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Faculty with id " + id + " not found."));

        facultyRepository.delete(faculty);
    }
}
