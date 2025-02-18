using ContaminaDOS_BackEnd.Data;
using ContaminaDOS_BackEnd.Models.RequestBody;
using ContaminaDOS_BackEnd.Models.ResponseBody;
using ContaminaDOS_BackEnd.Models;
using ContaminaDOS_BackEnd.Utilities;
using Microsoft.AspNetCore.Mvc;
using System.ComponentModel.DataAnnotations;
using System;
using System.Net;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using System.Runtime.InteropServices;
using System.Numerics;
using GoogleApi.Entities;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace ContaminaDOS_BackEnd.Controllers
{
    [Route("api/games")]
    [ApiController]
    public class ContaminaDOSController : ControllerBase
    {
        private readonly DataContext db = new DataContext();

        //GET Game Search
        [HttpGet]
        public GetGameSearch GameSearch(string? name, string? status, int? page, int? limit)
        {
            var game = new GetGameSearch();
            int defaultPage = 0;
            int defaultLimit = 50;

            try
            {
                if (limit == 0)
                {

                    var error_response = new GetGameSearch()
                    {
                        msg = "Invalid limit number",
                        status = 400
                    };
                    HttpContext.Response.StatusCode = 400;
                    return error_response;
                }

                var requestedGame = db.Games
                    .Include(g => g.Players)
                    .Where(g =>
                        (string.IsNullOrEmpty(name) || g.game_name == name) &&
                        (string.IsNullOrEmpty(status) || g.game_status == status));

                if (page != null && limit != null)
                {
                    requestedGame = requestedGame.Skip((int)(page * limit)).Take((int)limit);
                }

                if (limit != null && page == null)
                {
                    requestedGame = requestedGame.Skip((int)defaultPage).Take((int)limit);
                }

                if (limit == null && page != null)
                {
                    requestedGame = requestedGame.Skip((int)page).Take((int)defaultLimit);
                }

                var games = requestedGame.ToList();

                var gamesList = new List<GetGameSearch.Data>();

                foreach (var item in games)
                {
                    db.SaveChanges();
                    var game_data = SearchGameResponse(item);

                    gamesList.Add(game_data);

                }
                HttpContext.Response.StatusCode = 200;
                game.msg = "Game Found"; game.status = 200;
                game.data = gamesList;


                return game;
            }
            catch (ServerException ex)
            {

                GetGameSearch error_response = new GetGameSearch() { msg = ex.Message, status = ex.status };
                HttpContext.Response.StatusCode = ex.status;
                return error_response;
            }
        }


        // POST api/games
        [HttpPost]
        public GetGame GameCreate([FromBody] GameCreate new_game)
        {
            
            try
            {
                if (new_game == null || new_game.name.Length < 3 || new_game.name == null || string.IsNullOrEmpty(new_game.name))
                {
                    var msg = "Invalid or missing game name";
                    GetGame error_response = new GetGame() { msg = "Invalid or missing game name", status = 400 };
                    HttpContext.Response.StatusCode = 400;
                    return error_response;
                }

                var validations = new Validations();

                //Se verifica si tiene password o no
                bool use_password = false;
                if (!string.IsNullOrEmpty(new_game.password))
                {
                    validations.ValidateLength(new_game.password);
                    use_password = true;
                }
                else { new_game.password = ""; }

                //Se valida el name
                validations.ValidateLength(new_game.name);

                var existing_game = db.Games.Where(x => x.game_name == new_game.name).FirstOrDefault();

                //Si existe lanza un error
                validations.GameExists(existing_game, true);

                //Se valida el nombre del player
                validations.ValidateLength(new_game.owner);

                //Se crea el id del game
                string game_id = Guid.NewGuid().ToString();

                Models.Game game = new Models.Game()
                {
                    id = game_id,
                    game_name = new_game.name,
                    game_owner = new_game.owner,
                    game_status = "lobby",
                    createdAt = DateTime.UtcNow.ToString("yyyy-MM-ddTHH:mm:ss.fffZ"),
                    updatedAt = DateTime.UtcNow.ToString("yyyy-MM-ddTHH:mm:ss.fffZ"),
                    use_password = use_password,
                    game_password = new_game.password,
                    currentRound = "00000000-0000-0000-0000-000000000000"
                };

                game.Players.Add(
                    new Player()
                    {
                        id = Guid.NewGuid().ToString(),
                        player_name = new_game.owner,
                        player_role = "citizen",
                        game_id = game_id
                    });

                db.Games.Add(game);

                db.SaveChanges();

                GetGame game_data = GetGameResponse(game, new_game.owner);
                HttpContext.Response.StatusCode = 200;
                game_data.msg = "Game Created"; game_data.status = 200;
                return game_data;

            }
            catch (ServerException ex)
            {
                GetGame error_response = new GetGame() { msg = ex.Message, status = ex.status };
                HttpContext.Response.StatusCode = ex.status;
                return error_response;
            }
        }

        // GET api/games/{gameId}/
        [HttpGet("{gameId}")]
        public GetGame GetGame(string gameId, [FromHeader][Optional] string? password, [FromHeader][Required] string player)
        {
            try
            {
                var validations = new Validations();
                var game = db.Games.Include(g => g.Players).FirstOrDefault(x => x.id == gameId);

                validations.GameExists(game, false);

                validations.ValidatePassword(game, password);

                validations.ValidatePlayer(game, player, false);

                GetGame game_data = GetGameResponse(game, player);
                game_data.msg = "Game Found"; game_data.status = 200;

                HttpContext.Response.StatusCode = 200;
                return game_data;

            }
            catch (ServerException ex)
            {
                GetGame error_response = new GetGame() { msg = ex.Message, status = ex.status };
                HttpContext.Response.StatusCode = ex.status;
                return error_response;
            }
        }
        // PUT api/games/{gameId}/
        [HttpPut("{gameId}")]
        public GetGame JoinGame([Required] string gameId, [FromBody] JoinGame new_player, [FromHeader][Optional] string? password, [FromHeader][Required] string player)
        {
            try
            {
                if (new_player == null || new_player.player.Length < 3 || new_player.player.Length > 20)
                {
                    HttpContext.Response.StatusCode = 400;
                    return new GetGame { msg = "Invalid or missing player", status = 400 };
                }

                var validations = new Validations();
                var game = db.Games.Include(g => g.Players).Include(f => f.GameRounds).FirstOrDefault(x => x.id == gameId);

                validations.ValidateNewPlayer(player, new_player.player);

                validations.GameExists(game, false);

                validations.ValidatePassword(game, password);

                validations.ValidateLength(player);

                // Si existe lanza un error
                validations.ValidatePlayer(game, player, true);

                Player currentPlayer = new Player()
                {
                    id = Guid.NewGuid().ToString(),
                    player_name = new_player.player,
                    player_role = "citizen",
                    game_id = gameId
                };
                db.Players.Add(currentPlayer);
                db.SaveChanges();

                GetGame game_data = GetGameResponse(game, player);
                game_data.msg = "Joinned successfuly"; game_data.status = 200;

                HttpContext.Response.StatusCode = 200;
                return game_data;
            }
            catch (ServerException ex)
            {
                GetGame error_response = new GetGame() { msg = ex.Message, status = ex.status };
                HttpContext.Response.StatusCode = ex.status;
                return error_response;
            }
        }

        // HEAD api/games/{gameId}/start
        [HttpHead("{gameId}/start")]
        public void GameStart(string gameId, [FromHeader][Optional] string? password, [FromHeader][Required] string player)
        {
            var game = db.Games.Where(x => x.id == gameId).FirstOrDefault();

            if (game == null)
            {
                HttpContext.Response.StatusCode = 404;
                HttpContext.Response.Headers.Add("X-msg", "Game not found");
                return;
            }

            if (game.use_password && !game.game_password.Equals(password))
            {
                HttpContext.Response.StatusCode = 401;
                HttpContext.Response.Headers.Add("X-msg", "Unauthorized");
                return;
            }

            if (game.game_status != "lobby")
            {
                HttpContext.Response.StatusCode = 409;
                HttpContext.Response.Headers.Add("X-msg", "Game already started");
                return;
            }

            List<Player> game_players = db.Players.Where(x => x.game_id == gameId).ToList();
            int num_players = game_players.Count();

            // Se revisa si el player pertenece al juego y si es el owner para iniciar el juego
            if (string.IsNullOrEmpty(player) || game_players.Where(x => x.player_name == player).FirstOrDefault() == null || game.game_owner != player)
            {
                HttpContext.Response.StatusCode = 403;
                HttpContext.Response.Headers.Add("X-msg", "Forbidden");
                return;
            }

            if (num_players < 5)
            {
                HttpContext.Response.StatusCode = 428;
                HttpContext.Response.Headers.Add("X-msg", "Need 5 players to start");
                return;
            }

            // Se generan los enemigos con un random
            int enemies = 0;
            while (GetEnemies(num_players) > enemies)
            {
                Player enemy = game_players[new Random().Next(0, num_players)];
                if (enemy.player_role.Equals("citizen"))
                {
                    enemy.player_role = "enemy";
                    db.Players.Update(enemy);
                    enemies++;
                }
            }

            // Se crea la ronda inicial
            CreateRound(game, game_players, num_players);

            // Se guardan todos los cambios
            db.SaveChanges();

            HttpContext.Response.StatusCode = 200;
            HttpContext.Response.Headers.Add("X-msg", "Game started");
            return;
        }

        //GET api/games/{gameId}/rounds/{roundId}
        [HttpGet("{gameId}/rounds")]
        public GetRounds GetRounds([Required] string gameId, [FromHeader][Optional] string? password, [FromHeader][Required] string player)
        {
            try
            {
                var validations = new Validations();
                var game = db.Games.Include(g => g.Players).Include(f => f.GameRounds).FirstOrDefault(x => x.id == gameId);

                validations.GameExists(game, false);

                validations.ValidatePassword(game, password);

                validations.ValidatePlayer(game, player, false);

                //Se hicieron las validaciones, se empiezan a tomar los datos de las rondas
                GetRounds getRounds = new GetRounds();
                var listRounds = new List<Round>();
                foreach (var round in game.GameRounds)
                {
                    var data = GetRoundResponse(round);
                    listRounds.Add(data);
                }

                getRounds.data = listRounds;
                getRounds.msg = "Rounds Found"; getRounds.status = 200;

                return getRounds;
            }
            catch (ServerException ex)
            {
                GetRounds error_response = new GetRounds() { msg = ex.Message, status = ex.status };
                HttpContext.Response.StatusCode = ex.status;
                return error_response;
            }
        }


        //GET /api/games/{gameId}/rounds/{roundId}
        [HttpGet("{gameId}/rounds/{roundId}")]
        public GetRound ShowRound([Required] string gameId, [Required] string roundId, [FromHeader] string? password, [FromHeader][Required] string player)
        {
            try
            {
                var validations = new Validations();
                var game = db.Games.Include(g => g.Players).Include(f => f.GameRounds).FirstOrDefault(x => x.id == gameId);

                validations.GameExists(game, false);

                validations.ValidatePassword(game, password);

                validations.ValidatePlayer(game, player, false);

                //Se hicieron las validaciones, se empiezan a tomar los datos de las rondas
                GetRound getRound = new GetRound();

                getRound.data = GetRoundResponse(game.GameRounds.FirstOrDefault(x => x.id == roundId));
                getRound.msg = "Round Found"; getRound.status = 200;

                return getRound;
            }
            catch (ServerException ex)
            {
                GetRound error_response = new GetRound() { msg = ex.Message, status = ex.status };
                HttpContext.Response.StatusCode = ex.status;
                return error_response;
            }
        }

        // PATCH api/games/{gameId}/rounds/{roundId}
        [Route("{gameId}/rounds/{roundId}")]
        [HttpPatch]
        public GetRound ProposeGroup(string gameId, string roundId, [FromBody] Group group, [FromHeader][Optional] string? password, [FromHeader][Required] string player)
        {
            try
            {

                if (group == null || group.group == null || !group.group.Any())
                {
                    HttpContext.Response.StatusCode = 400;
                    return new GetRound { msg = "Invalid or missing group", status = 400 };
                }

                var validations = new Validations();
                var game = db.Games.Include(p => p.Players).Include(r => r.GameRounds).FirstOrDefault(x => x.id == gameId);

                validations.GameExists(game, false);

                var round = game.GameRounds.Where(x => x.id == roundId).FirstOrDefault();

                validations.RoundExists(round);

                validations.ValidatePassword(game, password);

                validations.ValidatePlayer(game, player, false);

                // Se valida que se pueda proponer un grupo y que el player sea el lider
                if (round.round_status != "waiting-on-leader" || player != round.leader)
                {
                    GetRound error_response = new GetRound()
                    {
                        msg = "This action is not allowed at this time",
                        status = 428
                    };
                    HttpContext.Response.StatusCode = 428;
                    return error_response;
                }

                // Se valida que no haya propuesto un grupo en esa phase
                var roundGroups = db.RoundGroups.Where(x => x.round_id == round.id);
                if (roundGroups.Where(x => x.phase == round.phase).FirstOrDefault() != null)
                {
                    GetRound error_response = new GetRound()
                    {
                        msg = "Asset already exists",
                        status = 409
                    };
                    HttpContext.Response.StatusCode = 409;
                    return error_response;
                }

                // Se obtiene la cantidad de jugadores que deberia tener el grupo 
                int group_size = GetGroup(game.Players.Count(), game.GameRounds.Count());

                // Se valida si el body del request cumple con la cantidad de jugadores de grupo
                if (group.group.Count() != group_size)
                {
                    GetRound error_response = new GetRound()
                    {
                        msg = "Requires a group of " + group_size + " members",
                        status = 428
                    };
                    HttpContext.Response.StatusCode = 428;
                    return error_response;
                }

                string group_id = Guid.NewGuid().ToString();

                // Crea el grupo
                RoundGroup roundGroup = new RoundGroup()
                {
                    id = group_id,
                    phase = round.phase,
                    round_id = round.id,
                };

                // Conforma y registra el grupo, en caso de tener participantes que no pertenecen al juego lo indica
                foreach (string group_member in group.group)
                {
                    var player_member = game.Players.Where(x => x.player_name == group_member).FirstOrDefault();
                    if (player_member != null)
                    {
                        GroupPlayer groupPlayer = new GroupPlayer()
                        {
                            group = roundGroup,
                            player = player_member
                        };
                        db.GroupPlayers.Add(groupPlayer);
                    }
                    else
                    {
                        GetRound error_response = new GetRound()
                        {
                            msg = "The player " + group_member + " doesn't exist",
                            status = 428
                        };
                        HttpContext.Response.StatusCode = 428;
                        return error_response;
                    }
                }

                // Actualiza la ronda
                round.round_status = "voting"; 
                round.updatedAt = DateTime.UtcNow.ToString("yyyy-MM-ddTHH:mm:ss.fffZ");
                db.GameRounds.Update(round);

                db.SaveChanges();

                HttpContext.Response.StatusCode = 200;
                return new GetRound()
                {
                    status = 200,
                    msg = "Group Created",
                    data = GetRoundResponse(round)
                };
            }
            catch (ServerException ex)
            {
                GetRound error_response = new GetRound() { msg = ex.Message, status = ex.status };
                HttpContext.Response.StatusCode = ex.status;
                return error_response;
            }
        }

        // POST api/games/{gameId}/rounds/{roundId}
        [Route("{gameId}/rounds/{roundId}")]
        [HttpPost]
        public GetRound VoteGroup(string gameId, string roundId, [FromBody] Models.RequestBody.Vote vote, [FromHeader][Optional] string? password, [FromHeader][Required] string player)
        {
            try
            {

                if (vote == null || !(vote.vote is bool))
                {
                    HttpContext.Response.StatusCode = 400;
                    return new GetRound { msg = "Invalid or missing vote", status = 400 };
                }


                var validations = new Validations();
                var game = db.Games.Include(p => p.Players).Include(r => r.GameRounds).FirstOrDefault(x => x.id == gameId);

                validations.GameExists(game, false);

                var round = game.GameRounds.Where(x => x.id == roundId).FirstOrDefault();

                validations.RoundExists(round);

                validations.ValidatePassword(game, password);

                validations.ValidatePlayer(game, player, false);

                // Se valida que este en fase de votacion
                if (round.round_status != "voting")
                    throw new ServerException("This action is not allowed at this time", 428);

                var group = db.RoundGroups.FirstOrDefault(x => x.round_id == roundId && x.phase == round.phase);

                validations.GroupExists(group);

                var player_id = game.Players.FirstOrDefault(x => x.player_name == player).id;

                var votes = db.Votes.Where(x => x.group_id == group.id).ToList();

                // Se valida que el jugador no haya votado
                if (votes.FirstOrDefault(x => x.player_id == player_id) != null)
                    throw new ServerException("Asset already exists", 409);

                var new_vote = new Models.Vote()
                {
                    player_id = player_id,
                    group_id = group.id,
                    vote1 = vote.vote
                };

                // Cuando se cumple todo hasta este punto, se registra el voto
                db.Votes.Add(new_vote);
                db.SaveChanges();

                // Se valida si es el último voto para dar un resultado
                votes.Add(new_vote);

                if (votes.Count() == game.Players.Count())
                {
                    // La mayoria de votos estan de acuerdo con el grupo
                    if (votes.Count(v => v.vote1 == true) > votes.Count(v => v.vote1 == false))
                    {
                        // Actualiza la ronda
                        round.round_status = "waiting-on-group";
                        round.updatedAt = DateTime.UtcNow.ToString("yyyy-MM-ddTHH:mm:ss.fffZ");
                        db.GameRounds.Update(round);
                        db.SaveChanges();
                    }
                    // La mayoria no esta de acuerdo o hay un empate
                    else
                    {
                        if (round.phase != "vote3")
                        {
                            // Actualiza la ronda
                            round.round_status = "waiting-on-leader";
                            round.phase = "vote" + (int.Parse(round.phase[^1].ToString()) + 1);
                            round.updatedAt = DateTime.UtcNow.ToString("yyyy-MM-ddTHH:mm:ss.fffZ");
                            db.GameRounds.Update(round);
                            db.SaveChanges();
                        }
                        else
                        {
                            round.round_status = "ended";
                            round.result = "enemies";
                            round.updatedAt = DateTime.UtcNow.ToString("yyyy-MM-ddTHH:mm:ss.fffZ");
                            db.GameRounds.Update(round);
                            db.SaveChanges();
                            CheckResults(game, password, player);
                        }
                    }   
                }
                HttpContext.Response.StatusCode = 200;
                return new GetRound()
                {
                    status = 200,
                    msg = "Voted Successfully",
                    data = GetRoundResponse(round)
                };

            }
            catch (ServerException ex)
            {
                GetRound error_response = new GetRound() { msg = ex.Message, status = ex.status };
                HttpContext.Response.StatusCode = ex.status;
                return error_response;
            }
        }

        //PUT /api/games/{gameId}/rounds/{roundId}
        [HttpPut("{gameId}/rounds/{roundId}")]
        public GetRound SubmitAction([Required] string gameId, [Required] string roundId, [FromHeader] string? password, [Required][FromHeader] string player, [FromBody] Models.RequestBody.Action action)
        {
            try
            {
                if (action == null)
                {
                    HttpContext.Response.StatusCode = 400;
                    return new GetRound { msg = "Invalid or missing action", status = 400 };
                }

                var validations = new Validations();
                var game = db.Games.Include(g => g.Players).Include(f => f.GameRounds).FirstOrDefault(x => x.id == gameId);

                validations.GameExists(game, false);

                validations.ValidatePassword(game, password);

                validations.ValidatePlayer(game, player, false);

                var round = db.GameRounds.Where(x => x.id == roundId).FirstOrDefault();

                validations.RoundExists(round);

                var current_player = game.Players.FirstOrDefault(x => x.player_name.Equals(player));

                //Verificar que se puedan realizar acciones en ese momento
                if (round.round_status != "waiting-on-group")
                {
                    GetRound error_response = new GetRound()
                    {
                        msg = "Action not allowed at this time",
                        status = 428
                    };
                    HttpContext.Response.StatusCode = 428;
                    return error_response;
                }

                //Jugador no es parte del grupo
                var groupId = db.RoundGroups.Where(x => x.round_id == roundId && x.phase == round.phase).FirstOrDefault().id;

                if (db.GroupPlayers.Where(x => x.group_id == groupId && x.player.id == current_player.id).IsNullOrEmpty())
                {
                    GetRound error_response = new GetRound()
                    {
                        msg = "Player is not part of the group",
                        status = 403
                    };
                    HttpContext.Response.StatusCode = 403;
                    return error_response;
                }

                if (db.GroupPlayers.Where(x => x.player.id == current_player.id && x.player.player_role == "enemy").IsNullOrEmpty() && !action.action)
                {
                    GetRound error_response = new GetRound()
                    {
                        msg = "Citizen cannot sabotage the round",
                        status = 428
                    };
                    HttpContext.Response.StatusCode = 428;
                    return error_response;
                }

                //Verificar que no se repitan las acciones
                if (!db.RoundActions.Where(x => x.round_id == roundId && x.player_id == current_player.id).IsNullOrEmpty())
                {
                    GetRound error_response = new GetRound()
                    {
                        msg = "Player already submit action",
                        status = 409
                    };
                    HttpContext.Response.StatusCode = 409;
                    return error_response;
                }

                //Añadir Accion a la ronda
                RoundAction roundAction = new RoundAction()
                {
                    round_id = roundId,
                    player_id = current_player.id,
                    round_action = action.action
                };

                db.RoundActions.Add(roundAction);
                db.SaveChanges();

                Round data = new Round();

                GetRound success_response = new GetRound()
                {
                    msg = "Action Registered",
                    status = 200
                };

                //Si todos los jugadores del grupo ya realizaron la accion

                if (db.RoundActions.Where(x => x.round_id == roundId).Count() == db.GroupPlayers.Where(x => x.group_id == groupId).Count())
                {
                    var actions = db.RoundActions.Where(x => x.round_id == roundId).ToList();

                    var sabotages = actions.Count(x => x.round_action == false);
                    // Añadir los resultados de la ronda actual
                    if (sabotages != 0)
                        round.result = "enemies";
                    else
                        round.result = "citizens";

                    round.round_status = "ended";
                    db.GameRounds.Update(round);
                    db.SaveChanges();

                    CheckResults(game, password, player);
                }

                data = GetRoundResponse(round);
                success_response.data = data;

                HttpContext.Response.StatusCode = 200;
                return success_response;
            }
            catch (ServerException ex)
            {
                GetRound error_response = new GetRound() { msg = ex.Message, status = ex.status };
                HttpContext.Response.StatusCode = ex.status;
                return error_response;
            }
        }

        // Métodos privados
        private GetGame GetGameResponse(Models.Game game, string current_player)
        {
            List<string> players = new List<string>(); List<string> enemies = new List<string>();

            foreach (Player player in game.Players)
            {
                players.Add(player.player_name);
                if (game.Players.Where(x => x.player_name == current_player).FirstOrDefault().player_role == "enemy")
                    if (player.player_role == "enemy")
                        enemies.Add(player.player_name);
            }

            return new GetGame()
            {
                data = new Models.ResponseBody.Game()
                {
                    id = game.id,
                    name = game.game_name,
                    owner = game.game_owner,
                    status = game.game_status,
                    createdAt = game.createdAt,
                    updatedAt = game.updatedAt,
                    password = game.use_password,
                    currentRound = game.currentRound,
                    players = players,
                    enemies = enemies
                }
            };
        }

        private Round GetRoundResponse(GameRound round)
        {
            //Añadir revision de RoundGroup
            var groupRound = db.RoundGroups.Where(x => x.round_id == round.id && x.phase == round.phase).FirstOrDefault();
            var players = new List<string>();
            if (groupRound != null)
            {
                var group = db.GroupPlayers.Where(x => x.group_id == groupRound.id);
                if (group != null)
                {
                    foreach (var playerOne in group)
                    {
                        players.Add(playerOne.player.player_name);
                    }
                }
            }

            var votes = new List<bool>();

            if (groupRound != null)
            {
                //Votaciones de la ronda (Revisar)
                var votations = db.Votes.Where(x => x.group_id == groupRound.id);

                if (!votations.IsNullOrEmpty())
                {
                    foreach (var vote in votations)
                    {
                        votes.Add(vote.vote1);
                    }
                }
            }

            Round data = new Round
            {
                id = round.id,
                leader = round.leader,
                status = round.round_status,
                result = round.result,
                phase = round.phase,
                group = players,
                votes = votes,
                createdAt = round.createdAt,
                updatedAt = round.updatedAt
            };

            return data;
        }

        private GetGameSearch.Data SearchGameResponse(Models.Game game)
        {
            List<Player> game_players = db.Players.Where(x => x.game_id == game.id).ToList();
            List<string> players = new List<string>(); List<string> enemies = new List<string>();

            foreach (Player player in game_players)
            {
                players.Add(player.player_name);
                if (player.player_role == "enemy") enemies.Add(player.player_name);
            }

            return new GetGameSearch.Data()
            {
                id = game.id,
                name = game.game_name,
                owner = game.game_owner,
                status = game.game_status,
                createdAt = game.createdAt.ToString(),
                updatedAt = game.updatedAt.ToString(),
                password = game.use_password,
                currentRound = game.currentRound,
                players = players,
                enemies = enemies
            };
        }

        private void CheckResults(Models.Game game, string password, string player)
        {
            //Verificar si hay ganador, en caso contrario crear una ronda
            var rounds = GetRounds(game.id, password, player);

            // Ya uno gano, se acaba el juego
            if(rounds.data.Count(x => x.result == "enemies") >= 3 || rounds.data.Count(x => x.result == "citizens") >= 3)
            {
                game.game_status = "ended";
                game.updatedAt = DateTime.UtcNow.ToString("yyyy-MM-ddTHH:mm:ss.fffZ");
                db.Games.Update(game);
                db.SaveChanges();
            }
            // Aun no han ganado, se crea nueva ronda
            else
            {
                List<Player> game_players = (List<Player>) game.Players;
                CreateRound(game, game_players, game_players.Count());
                db.SaveChanges();
            }
        }

        private void CreateRound(Models.Game game, List<Player> game_players, int num_players)
        {
            // Se crea la ronda con un lider random y valores iniciales
            string round_id = Guid.NewGuid().ToString();
            GameRound game_round = new GameRound()
            {
                id = round_id,
                leader = game_players[new Random().Next(0, num_players)].player_name,
                round_status = "waiting-on-leader",
                result = "none",
                phase = "vote1",
                createdAt = DateTime.UtcNow.ToString("yyyy-MM-ddTHH:mm:ss.fffZ"),
                updatedAt = DateTime.UtcNow.ToString("yyyy-MM-ddTHH:mm:ss.fffZ"),
                game_id = game.id
            };

            db.GameRounds.Add(game_round);

            // Se actualiza la informacion del game
            game.currentRound = round_id;
            game.game_status = "rounds";
            game.updatedAt = DateTime.UtcNow.ToString("yyyy-MM-ddTHH:mm:ss.fffZ");

            db.Games.Update(game);
        }

        private int GetEnemies(int num_playes)
        {
            switch (num_playes)
            {
                case 5: return 2;
                case 6: return 2;
                case 7: return 3;
                case 8: return 3;
                case 9: return 3;
                case 10: return 4;
                default: return 0;
            }
        }

        private int GetGroup(int num_players, int decade)
        {
            switch (num_players)
            {
                case 5:
                    return (decade == 1) ? 2 : (decade == 2) ? 3 : (decade == 3) ? 2 : (decade == 4) ? 3 : (decade == 5) ? 3 : 0;
                case 6:
                    return (decade == 1) ? 2 : (decade == 2) ? 3 : (decade == 3) ? 4 : (decade == 4) ? 3 : (decade == 5) ? 4 : 0;
                case 7:
                    return (decade == 1) ? 2 : (decade == 2) ? 3 : (decade == 3) ? 3 : (decade == 4) ? 4 : (decade == 5) ? 4 : 0;
                case 8:
                    return (decade == 1) ? 3 : (decade == 2) ? 4 : (decade == 3) ? 4 : (decade == 4) ? 5 : (decade == 5) ? 5 : 0;
                case 9:
                    return (decade == 1) ? 3 : (decade == 2) ? 4 : (decade == 3) ? 4 : (decade == 4) ? 5 : (decade == 5) ? 5 : 0;
                case 10:
                    return (decade == 1) ? 3 : (decade == 2) ? 4 : (decade == 3) ? 4 : (decade == 4) ? 5 : (decade == 5) ? 5 : 0;
                default:
                    return 0;
            }
        }
    }
}
