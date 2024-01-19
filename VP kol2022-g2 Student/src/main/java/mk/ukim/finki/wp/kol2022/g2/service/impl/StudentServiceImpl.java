package mk.ukim.finki.wp.kol2022.g2.service.impl;

import mk.ukim.finki.wp.kol2022.g2.model.Course;
import mk.ukim.finki.wp.kol2022.g2.model.Student;
import mk.ukim.finki.wp.kol2022.g2.model.StudentType;
import mk.ukim.finki.wp.kol2022.g2.model.exceptions.InvalidCourseIdException;
import mk.ukim.finki.wp.kol2022.g2.model.exceptions.InvalidEmailException;
import mk.ukim.finki.wp.kol2022.g2.model.exceptions.InvalidStudentIdException;
import mk.ukim.finki.wp.kol2022.g2.repository.CourseRepository;
import mk.ukim.finki.wp.kol2022.g2.repository.StudentRepository;
import mk.ukim.finki.wp.kol2022.g2.service.StudentService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService, UserDetailsService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Student> listAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow(()->new InvalidStudentIdException(id));
    }

    @Override
    public Student create(String name, String email, String password, StudentType type, List<Long> courseId, LocalDate enrollmentDate) {
        List<Course> courses = courseRepository.findAllById(courseId);
        return studentRepository.save(new Student(name, email, passwordEncoder.encode(password), type, courses, enrollmentDate));
    }

    @Override
    public Student update(Long id, String name, String email, String password, StudentType type, List<Long> coursesId, LocalDate enrollmentDate) {
        List<Course> courses = courseRepository.findAllById(coursesId);
        Student student = studentRepository.findById(id).orElseThrow(()->new InvalidStudentIdException(id));
        student.setName(name);
        student.setEmail(email);
        student.setPassword(passwordEncoder.encode(password));
        student.setType(type);
        student.setCourses(courses);
        student.setEnrollmentDate(enrollmentDate);
        return studentRepository.save(student);
    }

    @Override
    public Student delete(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(()->new InvalidStudentIdException(id));
        studentRepository.delete(student);
        return student;
    }

    @Override
    public List<Student> filter(Long courseId, Integer yearsOfStudying) {
        if(courseId == null && yearsOfStudying == null){
            return studentRepository.findAll();
        }
        Course course = null;
        LocalDate current_date = LocalDate.now();
        if(courseId == null){
            LocalDate date = current_date.minusYears(yearsOfStudying);
            System.out.println(current_date + " - " +yearsOfStudying+ " years = "+current_date.minusYears(yearsOfStudying));
            return studentRepository.findAllByEnrollmentDate(date);
        }
        if(yearsOfStudying == null){
            course = courseRepository.findById(courseId).orElseThrow(()->new InvalidCourseIdException(courseId));
            return studentRepository.findAllByCoursesContains(course);
        }
        LocalDate date = current_date.minusYears(yearsOfStudying);
        course = courseRepository.findById(courseId).orElseThrow(()->new InvalidCourseIdException(courseId));
        return studentRepository.findAllByCoursesContainsAndEnrollmentDate(course, date);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student student = studentRepository.findByEmail(email).orElseThrow(()-> new InvalidEmailException(email));
        return new org.springframework.security.core.userdetails.User(
                student.getEmail(),
                student.getPassword(),
                Collections.singletonList(student.getType()));
    }
}
