package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/rest")
public class PlayerController {

  private PlayerService playerService;

  @Autowired
  public PlayerController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @GetMapping("/players/count")
  public ResponseEntity<Long> getCountPlayers(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "title", required = false) String title,
      @RequestParam(value = "race", required = false) Race race,
      @RequestParam(value = "profession", required = false) Profession profession,
      @RequestParam(value = "after", required = false) Long after,
      @RequestParam(value = "before", required = false) Long before,
      @RequestParam(value = "banned", required = false) Boolean banned,
      @RequestParam(value = "minExperience", required = false) Integer minExperience,
      @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
      @RequestParam(value = "minLevel", required = false) Integer minLevel,
      @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {
    return new ResponseEntity<>(playerService
        .getPlayersCount(name, title, race, profession, after, before, banned, minExperience,
            maxExperience, minLevel, maxLevel), HttpStatus.OK);
  }

  @GetMapping("/players")
  public ResponseEntity<List<Player>> getPlayersList(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "title", required = false) String title,
      @RequestParam(value = "race", required = false) Race race,
      @RequestParam(value = "profession", required = false) Profession profession,
      @RequestParam(value = "after", required = false) Long after,
      @RequestParam(value = "before", required = false) Long before,
      @RequestParam(value = "banned", required = false) Boolean banned,
      @RequestParam(value = "minExperience", required = false) Integer minExperience,
      @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
      @RequestParam(value = "minLevel", required = false) Integer minLevel,
      @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
      @RequestParam(value = "order", defaultValue = "ID") PlayerOrder order,
      @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
      @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize) {
    List<Player> list = playerService
        .getPlayers(name, title, race, profession, after, before, banned, minExperience,
            maxExperience, minLevel, maxLevel, order, pageNumber, pageSize);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/players")
  public ResponseEntity<Player> createPlayer(@RequestBody() Player requestPlayer) {
    if (requestPlayer.getName() == null || requestPlayer.getTitle() == null
        || requestPlayer.getRace() == null || requestPlayer.getProfession() == null
        || requestPlayer.getBirthday() == null || requestPlayer.getExperience() == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    Boolean banned = requestPlayer.getBanned() == null ? false : requestPlayer.getBanned();
    return playerService
        .createPlayer(requestPlayer.getName(), requestPlayer.getTitle(), requestPlayer.getRace(),
            requestPlayer.getProfession(), requestPlayer.getBirthday(), banned, requestPlayer.getExperience());
  }

  @GetMapping("players/{id:.+}")
  public ResponseEntity<Player> getPlayer(@PathVariable String id) {
    return playerService.getPlayer(id);
  }

  @DeleteMapping("players/{id:.+}")
  public ResponseEntity<Player> deletePlayer(@PathVariable String id) {
    return playerService.deletePlayer(id);
  }

  @PostMapping("/players/{id:.+}")
  public ResponseEntity<Player> updatePlayer(@PathVariable String id, @RequestBody Player player) {
    ResponseEntity<Player> getPlayerForId = getPlayer(id);
    if (getPlayerForId.getStatusCode() == HttpStatus.OK) {
      return playerService.updatePlayer(player, getPlayerForId.getBody());
    } else {
      return getPlayerForId;
    }
  }
}
