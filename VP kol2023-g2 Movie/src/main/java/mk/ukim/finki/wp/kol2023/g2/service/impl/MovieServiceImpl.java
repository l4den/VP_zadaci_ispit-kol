package mk.ukim.finki.wp.kol2023.g2.service.impl;

import mk.ukim.finki.wp.kol2023.g2.model.Director;
import mk.ukim.finki.wp.kol2023.g2.model.Genre;
import mk.ukim.finki.wp.kol2023.g2.model.Movie;
import mk.ukim.finki.wp.kol2023.g2.model.exceptions.InvalidDirectorIdException;
import mk.ukim.finki.wp.kol2023.g2.model.exceptions.InvalidMovieIdException;
import mk.ukim.finki.wp.kol2023.g2.repository.DirectorRepository;
import mk.ukim.finki.wp.kol2023.g2.repository.MovieRepository;
import mk.ukim.finki.wp.kol2023.g2.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;

    public MovieServiceImpl(MovieRepository movieRepository, DirectorRepository directorRepository) {
        this.movieRepository = movieRepository;
        this.directorRepository = directorRepository;
    }

    @Override
    public List<Movie> listAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie findById(Long id) {
        return movieRepository.findById(id).orElseThrow(()->new InvalidMovieIdException(id));
    }

    @Override
    public Movie create(String name, String description, Double rating, Genre genre, Long director) {
        Director director_obj = directorRepository.findById(director).orElseThrow(()->new InvalidDirectorIdException(director));
        Movie movie = new Movie(name, description, rating, genre, director_obj);
        return movieRepository.save(movie);
    }

    @Override
    public Movie update(Long id, String name, String description, Double rating, Genre genre, Long director) {
        Director director_obj = directorRepository.findById(director).orElseThrow(()->new InvalidDirectorIdException(director));
        Movie movie = movieRepository.findById(id).orElseThrow(()->new InvalidMovieIdException(id));
        movie.setName(name);
        movie.setDescription(description);
        movie.setRating(rating);
        movie.setGenre(genre);
        movie.setDirector(director_obj);
        return movieRepository.save(movie);
    }

    @Override
    public Movie delete(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(()->new InvalidMovieIdException(id));
        movieRepository.deleteById(id);
        return movie;
    }

    @Override
    public Movie vote(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(()->new InvalidMovieIdException(id));
        movie.setVotes(movie.getVotes()+1);
        return movieRepository.save(movie);
    }

    @Override
    public List<Movie> listMoviesWithRatingLessThenAndGenre(Double rating, Genre genre) {
        if(rating == null && genre == null){
            return movieRepository.findAll();
        }

        if(rating == null){
            return movieRepository.findAllByGenre(genre);
        }
        if(genre == null){
            return movieRepository.findAllByRatingLessThanEqual(rating);
        }

        return movieRepository.findAllByRatingLessThanEqualAndGenre(rating, genre);
    }
}
