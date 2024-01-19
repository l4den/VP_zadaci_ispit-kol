package mk.ukim.finki.wp.kol2022.g3.service.impl;
import mk.ukim.finki.wp.kol2022.g3.model.ForumUser;
import mk.ukim.finki.wp.kol2022.g3.model.ForumUserType;
import mk.ukim.finki.wp.kol2022.g3.model.Interest;
import mk.ukim.finki.wp.kol2022.g3.model.exceptions.InvalidForumUserIdException;
import mk.ukim.finki.wp.kol2022.g3.model.exceptions.InvalidInterestIdException;
import mk.ukim.finki.wp.kol2022.g3.model.exceptions.InvalidUserEmailException;
import mk.ukim.finki.wp.kol2022.g3.repository.ForumUserRepository;
import mk.ukim.finki.wp.kol2022.g3.repository.InterestRepository;
import mk.ukim.finki.wp.kol2022.g3.service.ForumUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;

@Service
public class ForumUserServiceImpl implements ForumUserService, UserDetailsService {

    private final InterestRepository interestRepository;
    private final PasswordEncoder passwordEncoder;
    private final ForumUserRepository forumUserRepository;

    public ForumUserServiceImpl(InterestRepository interestRepository, ForumUserRepository forumUserRepository, PasswordEncoder passwordEncoder) {
        this.interestRepository = interestRepository;
        this.forumUserRepository = forumUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<ForumUser> listAll() {
        return forumUserRepository.findAll();
    }

    @Override
    public ForumUser findById(Long id) {
        return forumUserRepository.findById(id).orElseThrow(()->new InvalidForumUserIdException(id));
    }

    @Override
    public ForumUser create(String name, String email, String password, ForumUserType type, List<Long> interestId, LocalDate birthday) {
        List<Interest> interests = interestRepository.findAllById(interestId);
        String encoded_password = passwordEncoder.encode(password);
        return forumUserRepository.save(new ForumUser(name, email, encoded_password, type, interests, birthday));
    }

    @Override
    public ForumUser update(Long id, String name, String email, String password, ForumUserType type, List<Long> interestId, LocalDate birthday) {
        List<Interest> interests = interestRepository.findAllById(interestId);
        ForumUser forumUser = forumUserRepository.findById(id).orElseThrow(()->new InvalidForumUserIdException(id));
        String encoded_password = passwordEncoder.encode(password);
        forumUser.setName(name);
        forumUser.setEmail(email);
        forumUser.setPassword(encoded_password);
        forumUser.setType(type);
        forumUser.setInterests(interests);
        forumUser.setBirthday(birthday);
        return forumUserRepository.save(forumUser);
    }

    @Override
    public ForumUser delete(Long id) {
        ForumUser forumUser = forumUserRepository.findById(id).orElseThrow(()->new InvalidForumUserIdException(id));
        forumUserRepository.delete(forumUser);
        return forumUser;
    }

    @Override
    public List<ForumUser> filter(Long interestId, Integer age) {
        if(interestId == null && age == null){
            return forumUserRepository.findAll();
        }
        LocalDate current_date = LocalDate.now();
        if(interestId == null && age != 0){
            LocalDate youngest = LocalDate.of(current_date.getYear() - age, current_date.getMonth(), current_date.getDayOfMonth());
            return forumUserRepository.findAllByBirthdayBefore(youngest);
        }
        Interest interest = interestRepository.findById(interestId).orElseThrow(()->new InvalidInterestIdException(interestId));
        if(age == null || age == 0){
            return forumUserRepository.findAllByInterestsContaining(interest);
        }
        LocalDate youngest = LocalDate.of(current_date.getYear() - age, current_date.getMonth(), current_date.getDayOfMonth());
        return forumUserRepository.findAllByBirthdayBeforeAndInterestsContaining(youngest, interest);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername( "+email+" )");
        ForumUser user = forumUserRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(user.getType()));
    }
}
