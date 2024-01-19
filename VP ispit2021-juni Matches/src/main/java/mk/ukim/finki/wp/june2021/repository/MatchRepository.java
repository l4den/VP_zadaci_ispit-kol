package mk.ukim.finki.wp.june2021.repository;

import mk.ukim.finki.wp.june2021.model.Match;
import mk.ukim.finki.wp.june2021.model.MatchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match,Long> {
    List<Match> findAllByPriceLessThan(Double price);
    List<Match> findAllByTypeEquals(MatchType type);
    List<Match> findAllByPriceLessThanAndTypeEquals(Double price, MatchType type);
}
