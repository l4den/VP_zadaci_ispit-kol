package mk.ukim.finki.wp.kol2023.g1.service.impl;

import mk.ukim.finki.wp.kol2023.g1.model.Player;
import mk.ukim.finki.wp.kol2023.g1.model.PlayerPosition;
import mk.ukim.finki.wp.kol2023.g1.model.Team;
import mk.ukim.finki.wp.kol2023.g1.model.exceptions.InvalidPlayerIdException;
import mk.ukim.finki.wp.kol2023.g1.model.exceptions.InvalidTeamIdException;
import mk.ukim.finki.wp.kol2023.g1.repository.PlayerRepository;
import mk.ukim.finki.wp.kol2023.g1.repository.TeamRepository;
import mk.ukim.finki.wp.kol2023.g1.service.PlayerService;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Player> listAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player findById(Long id) {
        return playerRepository.findById(id).orElseThrow(()->new InvalidPlayerIdException(id));
    }

    @Override
    public Player create(String name, String bio, Double pointsPerGame, PlayerPosition position, Long team) {
        Team team_obj = teamRepository.findById(team).orElseThrow(()->new InvalidTeamIdException(team));
        return playerRepository.save(new Player(name, bio, pointsPerGame, position, team_obj));
    }

    @Override
    public Player update(Long id, String name, String bio, Double pointsPerGame, PlayerPosition position, Long team) {
        Team team_obj = teamRepository.findById(team).orElseThrow(()->new InvalidTeamIdException(team));
        Player player = playerRepository.findById(id).orElseThrow(()->new InvalidPlayerIdException(id));
        player.setName(name);
        player.setBio(bio);
        player.setPointsPerGame(pointsPerGame);
        player.setPosition(position);
        player.setTeam(team_obj);
        return playerRepository.save(player);
    }

    @Override
    public Player delete(Long id) {
        Player player = playerRepository.findById(id).orElseThrow(()->new InvalidPlayerIdException(id));
        playerRepository.delete(player);
        return player;
    }

    @Override
    public Player vote(Long id) {
        Player player = playerRepository.findById(id).orElseThrow(()->new InvalidPlayerIdException(id));
        player.setVotes(player.getVotes()+1);
        return playerRepository.save(player);
    }

    @Override
    public List<Player> listPlayersWithPointsLessThanAndPosition(Double pointsPerGame, PlayerPosition position) {
        // both null
        if(pointsPerGame==null && position == null){
            return playerRepository.findAll();
        }
        // first is null
        if(pointsPerGame == null){
            return playerRepository.findAllByPosition(position);
        }
        //second is null
        if(position == null){
            return playerRepository.findAllByPointsPerGameIsLessThan(pointsPerGame);
        }
        // no null
        return playerRepository.findAllByPointsPerGameIsLessThanAndPosition(pointsPerGame, position);
    }
}
