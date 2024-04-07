package com.mvi.CSCB634College.college;

import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.exception.DatabaseException;
import com.mvi.CSCB634College.security.Role;
import com.mvi.CSCB634College.security.auth.AuthenticationService;

import com.mvi.CSCB634College.user.User;
import com.mvi.CSCB634College.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final AuthenticationService authenticationService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final UserRepository userRepository;

    @Autowired
    public CollegeService(CollegeRepository collegeRepository, AuthenticationService authenticationService, UserRepository userRepository) {
        this.collegeRepository = collegeRepository;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }

    public DtoCollege createCollege(DtoCollege dtoCollege) {
        if (!authenticationService.getCurrentlyLoggedUser().getRole().equals(Role.ADMIN)){
            throw new BadRequestException("User needs to be admin to create colleges");
        }

        College college = modelMapper.map(dtoCollege, College.class);

        return saveAndRetrieveDtoCollege(college);
    }


    public DtoCollege getCollegeById(Integer id) {
        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("College with id " + id + " not found"));

        return modelMapper.map(college, DtoCollege.class);
    }

    public DtoCollege updateCollege(Integer id, DtoCollege dtoCollege) {
        if (!authenticationService.getCurrentlyLoggedUser().getRole().equals(Role.ADMIN)){
            throw new BadRequestException("User needs to be admin to create colleges");
        }

        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("College with id " + id + " not found"));

        modelMapper.map(dtoCollege, college);

        return saveAndRetrieveDtoCollege(college);
    }

    private DtoCollege saveAndRetrieveDtoCollege(College college) {
        try {
            college = collegeRepository.save(college);
        } catch (DataAccessException exc) {
            throw new DatabaseException("Database connectivity issue: " + exc.getMessage(), exc);
        }

        try {
            return modelMapper.map(college, DtoCollege.class);
        } catch (IllegalArgumentException exc) {
            throw new DatabaseException("Issue with mapping the contact data: " + exc.getMessage(), exc);
        }
    }

    public void deleteCollege(Integer id) {
        if (!authenticationService.getCurrentlyLoggedUser().getRole().equals(Role.ADMIN)){
            throw new BadRequestException("User needs to be admin to create colleges");
        }

        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("College with id " + id + " not found"));

        try {
            collegeRepository.delete(college);
        } catch (DataAccessException exc) {
            throw new DatabaseException("Database connectivity issue: " + exc.getMessage(), exc);
        }
    }

    public List<DtoCollege> getAllColleges() {
        return collegeRepository.findAll().stream()
                .map(college -> modelMapper.map(college, DtoCollege.class))
                .toList();
    }

    public DtoCollege addRectorToCollege(Integer collegeId, Integer rectorId) {
        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new BadRequestException("College with id " + collegeId + " not found"));

        User user = userRepository.findById(rectorId)
                .orElseThrow(() -> new BadRequestException("User with id " + rectorId + " not found"));

        college.setRector(user);

        return saveAndRetrieveDtoCollege(college);
    }

    public DtoCollege removeRectorFromCollege(Integer collegeId) {
        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new BadRequestException("College with id " + collegeId + " not found"));

        if (college.getRector() == null) {
            throw new BadRequestException("College with id " + collegeId + " does not have a rector");
        }

        college.setRector(null);

        return saveAndRetrieveDtoCollege(college);
    }


}
