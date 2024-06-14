package com.mvi.CSCB634College.absence;

import com.mvi.CSCB634College.enrollment.Enrollment;
import com.mvi.CSCB634College.enrollment.EnrollmentRepository;
import com.mvi.CSCB634College.exception.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AbsenceService(AbsenceRepository absenceRepository, EnrollmentRepository enrollmentRepository, ModelMapper modelMapper) {
        this.absenceRepository = absenceRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.modelMapper = modelMapper;
    }

    public DtoAbsence createAbsence(DtoAbsence dtoAbsence) {

        Absence absence = modelMapper.map(dtoAbsence, Absence.class);

        if (dtoAbsence.getEnrollmentId() != null) {
            Enrollment enrollment = enrollmentRepository.findById(dtoAbsence.getEnrollmentId())
                    .orElseThrow(() -> new BadRequestException("Enrollment with id " + dtoAbsence.getEnrollmentId() + " not found."));
            absence.setEnrollment(enrollment);
        }
        else throw new BadRequestException("Enrollment id is required.");

        return modelMapper.map(absenceRepository.save(absence), DtoAbsence.class);
    }

    public DtoAbsence getAbsenceById(Long id) {
        Absence absence = absenceRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Absence with id " + id + " not found."));
        return modelMapper.map(absence, DtoAbsence.class);
    }

    public List<DtoAbsence> getAllByEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new BadRequestException("Enrollment with id " + enrollmentId + " not found."));

        return absenceRepository.findAllByEnrollment(enrollment).stream()
                .map(absence -> modelMapper.map(absence, DtoAbsence.class))
                .collect(Collectors.toList());
    }

    public DtoAbsence updateAbsence(Long id, DtoAbsence dtoAbsence) {
        Absence absence = absenceRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Absence with id " + id + " not found."));

        if (!dtoAbsence.getEnrollmentId().equals(absence.getEnrollment().getId())) {
            throw new BadRequestException("You cant change the enrollment. Create a new absence");
        }

        modelMapper.map(dtoAbsence, absence);

        return modelMapper.map(absenceRepository.save(absence), DtoAbsence.class);
    }

    public void deleteAbsence(Long id) {
        Absence absence = absenceRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Absence with id " + id + " not found."));
        absenceRepository.delete(absence);
    }




}
