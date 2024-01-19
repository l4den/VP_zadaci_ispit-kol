package mk.ukim.finki.wp.june2021.service.impl;


import mk.ukim.finki.wp.june2021.model.Match;
import mk.ukim.finki.wp.june2021.model.MatchLocation;
import mk.ukim.finki.wp.june2021.model.MatchType;
import mk.ukim.finki.wp.june2021.model.exceptions.InvalidMatchIdException;
import mk.ukim.finki.wp.june2021.repository.MatchRepository;
import mk.ukim.finki.wp.june2021.service.MatchLocationService;
import mk.ukim.finki.wp.june2021.service.MatchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchLocationService matchLocationService;

    public MatchServiceImpl(MatchRepository matchRepository, MatchLocationService matchLocationService) {
        this.matchRepository = matchRepository;
        this.matchLocationService = matchLocationService;
    }

    @Override
    public List<Match> listAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match findById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(()->new InvalidMatchIdException(id));
    }

    @Override
    public Match create(String name, String description, Double price, MatchType type, Long location) {
        MatchLocation matchLocation = matchLocationService.findById(location);
        return this.matchRepository.save(new Match(name,description,price,type,matchLocation));
    }

    @Override
    public Match update(Long id, String name, String description, Double price, MatchType type, Long location) {
        Match match = matchRepository.findById(id).orElseThrow(()->new InvalidMatchIdException(id));
        MatchLocation matchLocation = matchLocationService.findById(location);
        match.setName(name);
        match.setDescription(description);
        match.setPrice(price);
        match.setType(type);
        match.setLocation(matchLocation);
        return matchRepository.save(match);
    }

    @Override
    public Match delete(Long id) {
        Match match = matchRepository.findById(id).orElseThrow(()->new InvalidMatchIdException(id));
        this.matchRepository.delete(match);
        return match;
    }

    @Override
    public Match follow(Long id) {
        Match match = matchRepository.findById(id).orElseThrow(()->new InvalidMatchIdException(id));
        match.setFollows(match.getFollows()+1);
        return matchRepository.save(match);
    }

    @Override
    public List<Match> listMatchesWithPriceLessThanAndType(Double price, MatchType type) {
        if(price == null && type == null){
            return matchRepository.findAll();
        }

        if(price == null){
            return matchRepository.findAllByTypeEquals(type);
        }

        if(type == null){
            return matchRepository.findAllByPriceLessThan(price);
        }
        return matchRepository.findAllByPriceLessThanAndTypeEquals(price, type);
    }
}
