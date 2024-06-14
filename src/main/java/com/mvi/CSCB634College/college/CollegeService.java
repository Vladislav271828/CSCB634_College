package com.mvi.CSCB634College.college;

import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.exception.CollegeNotFound;
import com.mvi.CSCB634College.exception.DatabaseException;
import com.mvi.CSCB634College.exception.UserNotFound;
import com.mvi.CSCB634College.security.Role;
import com.mvi.CSCB634College.security.auth.AuthenticationService;
import com.mvi.CSCB634College.security.config.JwtAuthenticationFilter;
import com.mvi.CSCB634College.user.ResponseUser;
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

        dtoCollegeRequest.setId(null);

        User rector = null;

        if (dtoCollegeRequest.getRectorEmail() != null) {
            rector = userRepository.findByEmail(dtoCollegeRequest.getRectorEmail()).orElseThrow(() -> new UserNotFound("User " + dtoCollegeRequest.getRectorEmail() + " is not found."));//Email should always be valid format here due to the regex in the dto class

            if (isRectorAlreadyRectoring(rector)) {//Check if the user is already a rector in the database
                throw new BadRequestException("User " + rector.getEmail() + " is already a rector.");
            }

            User currentlyLoggedUser = authenticationService.getCurrentlyLoggedUser();

            if (rector.getEmail().equals(currentlyLoggedUser.getEmail())) {//check if the ADMIN user is trying to become a rector all by himself
                throw new BadRequestException("User " + currentlyLoggedUser.getEmail() + " can't become rector of college by themselves. Another ADMIN must authorize the request.");
            }

        }

//
//        if (!isCollegeUnique(college)) {//Check if the college already exists in the database
//            throw new BadRequestException("College with name " + college.getName() + " or address " + college.getAddress() + " already exists.");
//        }
        College college = modelMapper.map(dtoCollegeRequest, College.class);
        college.setRector(rector);


        return saveAndRetrieveDtoCollege(college);//Saves and returns the college
    }


    public DtoCollegeRequest getCollegeById(Long id) {
        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new CollegeNotFound("College with id " + id + " not found"));

        return modelMapper.map(college, DtoCollegeRequest.class);
    }

    public DtoCollegeRequest updateCollege(Long id, DtoCollegeRequest dtoCollegeRequest) {
        dtoCollegeRequest.setId(null);
        User currentUser = authenticationService.getCurrentlyLoggedUser();

        // Find college by id
        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new CollegeNotFound("College with id " + id + " not found"));

        // Check if at least one field is provided
        if (dtoCollegeRequest.getName() == null && dtoCollegeRequest.getAddress() == null && dtoCollegeRequest.getRectorEmail() == null) {
            throw new BadRequestException("At least one field must be updated.");
        }

        // Update name if it's provided and unique
        if (dtoCollegeRequest.getName() != null && !dtoCollegeRequest.getName().isBlank()) {
            if (!isNameUnique(dtoCollegeRequest.getName(), id)) {
                throw new BadRequestException("College with name " + dtoCollegeRequest.getName() + " already exists.");
            }
            college.setName(dtoCollegeRequest.getName());
        }

        // Update address if it's provided
        if (dtoCollegeRequest.getAddress() != null && !dtoCollegeRequest.getAddress().isBlank()) {
            if (!dtoCollegeRequest.getAddress().equals(college.getAddress())) {

                college.setAddress(dtoCollegeRequest.getAddress());
            } else {
                throw new BadRequestException("Address must be different than the current college address.");
            }
        }

        // Update rector if the email is provided and the user is not a rector of another college
        if (dtoCollegeRequest.getRectorEmail() != null && !dtoCollegeRequest.getRectorEmail().isBlank()) {
            User rector = userRepository.findByEmail(dtoCollegeRequest.getRectorEmail())
                    .orElseThrow(() -> new UserNotFound("User with email " + dtoCollegeRequest.getRectorEmail() + " not found"));

            if (college.getRector() != null) {
                if (college.getRector().getEmail().equals(rector.getEmail())) {
                    throw new BadRequestException("User " + rector.getEmail() + " is already a rector of this college.");


                } else if (dtoCollegeRequest.getRectorEmail().equals(currentUser.getEmail())) {
                    throw new BadRequestException("User " + currentUser.getEmail() + " can't become college rector by themselves.");


                } else if (isRectorAlreadyRectoring(rector)) {//if rector is already rectoring we remove them as the rector from the other college and set them here
                    College otherCollege = collegeRepository.findByRector_Email(rector.getEmail()).orElseThrow();
                    otherCollege.setRector(null);
                    collegeRepository.save(otherCollege);

                    college.setRector(rector);

                } else {
                    college.setRector(rector);
                }
            } else {//in case the rector is not decided but the candidate is already rectoring
                if (isRectorAlreadyRectoring(rector)) {//if rector is already rectoring we remove them as the rector from the other college and set them here
                    College otherCollege = collegeRepository.findByRector_Email(rector.getEmail()).orElseThrow();
                    otherCollege.setRector(null);
                    collegeRepository.save(otherCollege);

                    college.setRector(rector);

                }
                college.setRector(rector);
            }

        }

        // Save the updated college
        return saveAndRetrieveDtoCollege(college);
    }

    private boolean isNameUnique(String name, Long collegeId) {
        return collegeRepository.findByNameIgnoreCase(name)
                .stream()
                .noneMatch(c -> !c.getId().equals(collegeId));
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

    public void deleteCollege(Long id) {
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


    private boolean isRectorAlreadyRectoring(User rector) {
        return !collegeRepository.findByRector_EmailAllIgnoreCase(rector.getEmail()).isEmpty();
    }

    private boolean isCollegeUnique(College college) {

        return collegeRepository.findByNameIgnoreCaseOrAddressIgnoreCase(college.getName(), college.getAddress()).isEmpty();
    }

    public List<ResponseUser> getAllUserByCollegeId(Long id) {
    List<College> colleges =  collegeRepository.findAll();

    if (colleges.isEmpty()){
        throw new CollegeNotFound("No colleges found");
    }

    //TODO: ??????????? HOW get all users under one collage?

     return null;
    }
}
