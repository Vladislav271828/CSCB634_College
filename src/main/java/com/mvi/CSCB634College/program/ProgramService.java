package com.mvi.CSCB634College.program;


import com.mvi.CSCB634College.course.Course;
import com.mvi.CSCB634College.course.CourseRepository;
import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.major.Major;
import com.mvi.CSCB634College.major.MajorRepository;
import com.mvi.CSCB634College.professor.Professor;
import com.mvi.CSCB634College.professor.ProfessorRepository;
import com.mvi.CSCB634College.professor.ProfessorService;
import com.mvi.CSCB634College.user.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProfessorRepository professorRepository;

    private final CourseRepository courseRepository;

    private final ModelMapper modelMapper;

    private final ProgramRepository programRepository;
    private final ProfessorService professorService;
    private final MajorRepository majorRepository;

    public DtoProgramResponse createProgram(DtoProgram dtoProgram){

        Program program = modelMapper.map(dtoProgram, Program.class);

        List<Professor> professors = new ArrayList<>();
        for (Integer professorId : dtoProgram.getProfessorIds()) {
            Professor professor = professorRepository.findById(professorId)
                    .orElseThrow(() -> new BadRequestException("Professor with id " + professorId + " not found"));
            professors.add(professor);
        }

        Course course = courseRepository.findById(dtoProgram.getCourseId())
                .orElseThrow(() -> new BadRequestException("Course with id " + dtoProgram.getCourseId() + " not found"));


        program.setCourse(course);
        program.setProfessors(professors);

        program = programRepository.save(program);


        DtoProgramResponse response = modelMapper.map(program, DtoProgramResponse.class);
        response.setProfessors(professorService.getResponseUsers(program.getProfessors()));
        return response;
    }

    public List<DtoProgramResponse> getProgramsByYear(Integer year) {
        List<Program> programs = programRepository.findAllByYear(year);
        return programs.stream()
                .map(program -> {
                    DtoProgramResponse response = modelMapper.map(program, DtoProgramResponse.class);
                    response.setProfessors(professorService.getResponseUsers(program.getProfessors()));
                    return response;
                })
                .toList();
    }

    public List<DtoProgramResponse> getProgramsByYearAndMajor(Integer year, Long majorId) {

        Major major = majorRepository.findById(majorId)
                .orElseThrow(() -> new BadRequestException("Major with id " + majorId + " not found"));

        List<Program> programs = programRepository.findAllByYearAndMajor(year, major);
        return programs.stream()
                .map(program -> {
                    DtoProgramResponse response = modelMapper.map(program, DtoProgramResponse.class);
                    response.setProfessors(professorService.getResponseUsers(program.getProfessors()));
                    return response;
                })
                .toList();
    }

    public DtoProgram getProgramById(Long programId) {
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new BadRequestException("Program with id " + programId + " not found"));
        DtoProgram response = modelMapper.map(program, DtoProgram.class);
        response.setProfessorIds(professorService.getResponseUsers(program.getProfessors())
                .stream().map(ResponseUser::getId).toList());
        return response;
    }
    
    public DtoProgramResponse editProgram(Long programId, DtoProgram dtoProgram) {
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new BadRequestException("Program with id " + programId + " not found"));

        List<Professor> professors = new ArrayList<>();
        for (Integer professorId : dtoProgram.getProfessorIds()) {
            Professor professor = professorRepository.findById(professorId)
                    .orElseThrow(() -> new BadRequestException("Professor with id " + professorId + " not found"));
            professors.add(professor);
        }
        if (!professors.equals(program.getProfessors())) {
            program.setProfessors(professors);
        }



        if (dtoProgram.getCourseId() != null && !dtoProgram.getCourseId().equals(program.getCourse().getId())){
            Course course = courseRepository.findById(dtoProgram.getCourseId())
                    .orElseThrow(() -> new BadRequestException("Course with id " + dtoProgram.getCourseId() + " not found"));
            program.setCourse(course);
        }

        modelMapper.map(dtoProgram, program);

        program = programRepository.save(program);
        DtoProgramResponse response = modelMapper.map(program, DtoProgramResponse.class);
        response.setProfessors(professorService.getResponseUsers(program.getProfessors()));
        return response;
    }

    public void deleteProgram(Long programId) {
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new BadRequestException("Program with id " + programId + " not found"));
        program.getProfessors().clear();
        programRepository.save(program);
        programRepository.delete(program);
    }
}
