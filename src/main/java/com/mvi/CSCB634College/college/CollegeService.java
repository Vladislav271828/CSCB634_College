package com.mvi.CSCB634College.college;

import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.exception.DatabaseException;
import com.mvi.CSCB634College.security.Role;
import com.mvi.CSCB634College.security.auth.AuthenticationService;
import com.mvi.CSCB634College.security.config.JwtAuthenticationFilter;
import com.mvi.CSCB634College.user.User;
import com.mvi.CSCB634College.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollegeService {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final CollegeRepository collegeRepository;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CollegeService(CollegeRepository collegeRepository, AuthenticationService authenticationService, UserRepository userRepository, ModelMapper modelMapper) {
        this.collegeRepository = collegeRepository;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public DtoCollegeRequest createCollege(DtoCollegeRequest dtoCollegeRequest) throws BadRequestException {

        User rector;

        try {
            rector = userRepository.findByEmail(dtoCollegeRequest.getRectorEmail()).orElseThrow();//Email should always be valid format here due to the regex in the dto class

        } catch (Exception e) {
            throw new BadRequestException("User " + dtoCollegeRequest.getRectorEmail() + " is not found.");
        }

        if (!isRectorAlreadyRectoring(rector)) {//Check if the user is already a rector in the database
            throw new BadRequestException("User " + rector.getEmail() + " is already a rector.");
        }

        User currentlyLoggedUser = authenticationService.getCurrentlyLoggedUser();

        if (rector.getEmail().equals(currentlyLoggedUser.getEmail())) {//check if the ADMIN user is trying to become a rector all by himself
            throw new BadRequestException("User " + currentlyLoggedUser.getEmail() + " can't become rector of college by themselves. Another ADMIN must authorize the request.");
        }

        College college = modelMapper.map(dtoCollegeRequest, College.class);

        if (!isCollegeUnique(college)) {//Check if the college already exists in the database
            throw new BadRequestException("College with name " + college.getName() + " or address " + college.getAddress() + " already exists.");
        }

        college.setRector(rector);


        return saveAndRetrieveDtoCollege(college);//Saves and returns the college
    }


    public DtoCollegeRequest getCollegeById(Integer id) {
        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("College with id " + id + " not found"));

        return modelMapper.map(college, DtoCollegeRequest.class);
    }

    public DtoCollegeRequest updateCollege(Integer id, DtoCollegeRequest dtoCollegeRequest) {
        if (!authenticationService.getCurrentlyLoggedUser().getRole().equals(Role.ADMIN)) {
            throw new BadRequestException("User needs to be admin to create colleges");
        }

        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("College with id " + id + " not found"));

        modelMapper.map(dtoCollegeRequest, college);
        return saveAndRetrieveDtoCollege(college);
    }

    private DtoCollegeRequest saveAndRetrieveDtoCollege(College college) {
        try {
            college = collegeRepository.save(college);
        } catch (DataAccessException exc) {
            throw new DatabaseException("Database connectivity issue: " + exc.getMessage(), exc);
        }

        try {
            return modelMapper.map(college, DtoCollegeRequest.class);
        } catch (IllegalArgumentException exc) {
            throw new DatabaseException("Issue with mapping the contact data: " + exc.getMessage(), exc);
        }
    }

    public void deleteCollege(Integer id) {
        if (!authenticationService.getCurrentlyLoggedUser().getRole().equals(Role.ADMIN)) {
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

    public List<DtoCollegeRequest> getAllColleges() {
        return collegeRepository.findAll().stream()
                .map(college -> modelMapper.map(college, DtoCollegeRequest.class))
                .toList();
    }

    public DtoCollegeRequest addRectorToCollege(Integer collegeId, Integer rectorId) {
        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new BadRequestException("College with id " + collegeId + " not found"));

        User user = userRepository.findById(rectorId)
                .orElseThrow(() -> new BadRequestException("User with id " + rectorId + " not found"));

        college.setRector(user);

        return saveAndRetrieveDtoCollege(college);
    }

    public DtoCollegeRequest removeRectorFromCollege(Integer collegeId) {
        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new BadRequestException("College with id " + collegeId + " not found"));

        if (college.getRector() == null) {
            throw new BadRequestException("College with id " + collegeId + " does not have a rector");
        }

        college.setRector(null);

        return saveAndRetrieveDtoCollege(college);
    }

    private boolean isRectorAlreadyRectoring(User rector) {
        return collegeRepository.findByRector_EmailAllIgnoreCase(rector.getEmail()).isEmpty();
    }

    private boolean isCollegeUnique(College college) {

        return collegeRepository.findByNameIgnoreCaseOrAddressIgnoreCase(college.getName(), college.getAddress()).isEmpty();
    }

}
