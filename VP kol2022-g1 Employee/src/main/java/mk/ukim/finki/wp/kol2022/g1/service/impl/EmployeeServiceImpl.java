package mk.ukim.finki.wp.kol2022.g1.service.impl;

import mk.ukim.finki.wp.kol2022.g1.model.Employee;
import mk.ukim.finki.wp.kol2022.g1.model.EmployeeType;
import mk.ukim.finki.wp.kol2022.g1.model.Skill;
import mk.ukim.finki.wp.kol2022.g1.model.exceptions.InvalidEmployeeEmailException;
import mk.ukim.finki.wp.kol2022.g1.model.exceptions.InvalidEmployeeIdException;
import mk.ukim.finki.wp.kol2022.g1.model.exceptions.InvalidSkillIdException;
import mk.ukim.finki.wp.kol2022.g1.repository.EmployeeRepository;
import mk.ukim.finki.wp.kol2022.g1.repository.SkillRepository;
import mk.ukim.finki.wp.kol2022.g1.service.EmployeeService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService, UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final SkillRepository skillRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               SkillRepository skillRepository,
                               PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.skillRepository = skillRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Employee> listAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElseThrow(()->new InvalidEmployeeIdException(id));
    }

    @Override
    public Employee create(String name, String email, String password, EmployeeType type, List<Long> skillId, LocalDate employmentDate) {
        List<Skill> skills = skillRepository.findAllById(skillId);
        return employeeRepository.save(new Employee(name, email, passwordEncoder.encode(password), type, skills, employmentDate));
    }

    @Override
    public Employee update(Long id, String name, String email, String password, EmployeeType type, List<Long> skillId, LocalDate employmentDate) {
        Employee employee = employeeRepository.findById(id).orElseThrow(()->new InvalidEmployeeIdException(id));
        List<Skill> skills = skillRepository.findAllById(skillId);
        employee.setName(name);
        employee.setEmail(email);
        employee.setPassword(passwordEncoder.encode(password));
        employee.setType(type);
        employee.setSkills(skills);
        employee.setEmploymentDate(employmentDate);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee delete(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(()->new InvalidEmployeeIdException(id));
        employeeRepository.delete(employee);
        return employee;
    }

    @Override
    public List<Employee> filter(Long skillId, Integer yearsOfService) {
        // Ako nema search kirterium
        if(skillId == null && yearsOfService == null){
            return employeeRepository.findAll();
        }
        // Ako nema criterium 1
        if(skillId == null){
            LocalDate earliest_date = LocalDate.now().minusYears(yearsOfService);
            return employeeRepository.findAllByEmploymentDateBefore(earliest_date);
        }
        // Ako nema criterium 2
        if(yearsOfService == null){
            Skill skill = skillRepository.findById(skillId).orElseThrow(()->new InvalidSkillIdException(skillId));
            return employeeRepository.findAllBySkillsContains(skill);
        }

        Skill skill = skillRepository.findById(skillId).orElseThrow(()->new InvalidSkillIdException(skillId));
        LocalDate earliest_date = LocalDate.now().minusYears(yearsOfService);
        return employeeRepository.findAllBySkillsContainsAndEmploymentDateBefore(skill, earliest_date);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email).orElseThrow(()-> new InvalidEmployeeEmailException(email));
        return new org.springframework.security.core.userdetails.User(
                employee.getEmail(),
                employee.getPassword(),
                Collections.singletonList(employee.getType()));
    }
}
