using contaminaDOS_JVW.Models;
using DemoEF.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.SignalR;
using Newtonsoft.Json;
using System;
using System.Net;
using System.Numerics;
using System.Text;
using System.Text.RegularExpressions;
using System.Web;
using System.Xml.Linq;

namespace contaminaDOS_JVW.Controllers
{
    public class ContaminaDOSController : Controller
    {
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult Home(string server)
        {
            return View(new Server() { server = server });
        }

        // GET: ContaminaDOSController
        public ActionResult GameSearch(string name, string status, int page, int limit, string server)
        {
            try
            {
                GamesSearch games_data = new GamesSearch();
                using (var client = new HttpClient())
                {
                    // URL del servicio
                    string baseUrl = server + "/api/games/";

                    // Crear una instancia de UriBuilder para construir la URL con parámetros de consulta
                    var uriBuilder = new UriBuilder(baseUrl);
                    var query = HttpUtility.ParseQueryString(uriBuilder.Query);

                    // Agregar los parámetros de consulta
                    if (!string.IsNullOrEmpty(name)) query["name"] = name;
                    if (!string.IsNullOrEmpty(status)) query["status"] = status;
                    query["page"] = page.ToString();
                    query["limit"] = limit.ToString();

                    // Establecer la URL con los parámetros de consulta
                    uriBuilder.Query = query.ToString();

                    // Obtener la URL completa con los parámetros de consulta
                    string url = uriBuilder.ToString();

                    var response = client.GetAsync(url); //Ruta del metodo
                    response.Wait();

                    var result = response.Result;
                    if (result.IsSuccessStatusCode)
                    {
                        var readResponse = result.Content.ReadFromJsonAsync<GamesSearch>();
                        readResponse.Wait();

                        games_data = readResponse.Result;
                    }

                }

                games_data.server = server; //Se le indica en que servidor se encuentra

                string message = TempData["Message"] as string;

                // Asegúrate de manejar el caso en el que los datos no estén presentes
                if (!string.IsNullOrEmpty(message))
                {
                    ViewBag.AlertMessage = JsonConvert.DeserializeObject<AlertMessage>(message); ;
                }

                return View(games_data);
            }catch(Exception)
            {
                return View("Index");
            }
        }

        public ActionResult GameCreate(string server)
        {
            return View(new Server() { server = server });
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult GameCreate(Game game, string server)
        {
            try
            {
                GameData game_info = new GameData();

                using (var client = new HttpClient())
                {
                    //URL del servicio
                    string url = server + "/api/games/";
                    Task<HttpResponseMessage> response = null;

                    //Si se indica la contraseña, se crea normalmente, si no se indica, se crea un JSON con el body sin contraseña
                    if (!string.IsNullOrEmpty(game.password))
                        response = client.PostAsJsonAsync<Game>(url, game);

                    else
                        response = client.PostAsync(url,
                            new StringContent(JsonConvert.SerializeObject(new { game.name, game.owner }), Encoding.UTF8, "application/json")
                        );

                    response.Wait();

                    var result = response.Result;

                    var readResponse = result.Content.ReadFromJsonAsync<GameData>();
                    readResponse.Wait();

                    game_info = readResponse.Result;

                    if (result.IsSuccessStatusCode)
                    {
                        GameRoundData game_round_data = new GameRoundData()
                        {
                            player = game.owner,
                            password = game.password,
                            server = server,
                            game = game_info,
                            round = null
                        };
                        return View("GetGame", game_round_data);
                    }
                    else
                    {
                        ViewBag.AlertMessage = new AlertMessage() { Text = game_info.msg, Tipo = Alerta.danger.ToString(), Status = game_info.status };
                        return View("GameCreate", new Server() { server = server });
                    }
                }
            }catch(Exception)
            {
                return View("Index");
            }
        }

        public ActionResult UpdateGame(string gameId, string password, string player, string server)
        {
            try
            {
                GameData game = GetGameData(gameId, password, player, server);
                RoundData round = null;
                int counter_rounds = 0;
                var result = "";
                List<string> resultList = new List<string>();
                if (game.data.status != "lobby")
                {
                    var allRounds = GetRounds(gameId, password, player, server).data;
                    counter_rounds = allRounds.Count();

                    var enemies = 0;
                    var allies = 0;
                    foreach (var singleRound in allRounds)
                    {
                        if (singleRound.result == "enemies")
                        {
                            enemies++;
                        }
                        if (singleRound.result == "citizens")
                        {
                            allies++;
                        }

                        resultList.Add(singleRound.result);
                    }

                    if (allies >= 3)
                    {
                        result = "citizens";
                    }

                    if (enemies >= 3)
                    {
                        result = "enemies";
                    }

                    round = ShowRound(gameId, game.data.currentRound, password, player, server);
                }

                GameRoundData game_round_data = new GameRoundData()
                {
                    player = player,
                    password = password,
                    result = result,
                    server = server,
                    resultList = resultList,
                    count_rounds = counter_rounds,
                    game = game,
                    round = round
                };
                return View("GetGame", game_round_data);
            }
            catch (Exception)
            {
                return View("Index");
            }
        }

        public ActionResult GetGame(GameRoundData game_round)
        {
            return View(game_round);
        }

        public GameData GetGameData(string gameId, string password, string player, string server)
        {
            GameData game_info = new GameData();
            using (var client = new HttpClient())
            {
                // URL del servicio
                string baseUrl = server + "/api/games/" + gameId;

                // Crear una instancia de HttpRequestMessage en lugar de usar una URL
                var request = new HttpRequestMessage(HttpMethod.Get, baseUrl);

                // Agregar los valores de "password" y "player" como encabezados
                if (!string.IsNullOrEmpty(password)) request.Headers.Add("password", password);
                if (!string.IsNullOrEmpty(player)) request.Headers.Add("player", player);

                // Realizar la solicitud HTTP
                var response = client.SendAsync(request);
                response.Wait();

                var result = response.Result;
                if (result.IsSuccessStatusCode)
                {
                    var readResponse = result.Content.ReadFromJsonAsync<GameData>();
                    readResponse.Wait();

                    game_info = readResponse.Result;
                }

            }
            ViewBag.num_players = game_info.data.players.Count();
            return game_info;
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult JoinGame(string gameId, string password, string player, string name, string server)
        {
            try
            {
                Player new_player = new Player() { player = player };

                GameData game_info = new GameData();

                using (var client = new HttpClient())
                {
                    // URL del servicio
                    string baseUrl = server + "/api/games/" + gameId;

                    // Crear una instancia de HttpRequestMessage en lugar de usar una URL
                    var request = new HttpRequestMessage(HttpMethod.Put, baseUrl);

                    // Agregar los valores de "password" y "player" como encabezados
                    if (!string.IsNullOrEmpty(password)) request.Headers.Add("password", password);
                    if (!string.IsNullOrEmpty(player)) request.Headers.Add("player", player);

                    // Agregar el objeto new_player como contenido en el cuerpo de la solicitud
                    var json = new StringContent(JsonConvert.SerializeObject(new_player), Encoding.UTF8, "application/json");
                    request.Content = json;

                    // Realizar la solicitud HTTP
                    var response = client.SendAsync(request);
                    response.Wait();

                    var result = response.Result;

                    var readResponse = result.Content.ReadFromJsonAsync<GameData>();
                    readResponse.Wait();

                    game_info = readResponse.Result;

                    game_info.player = player;
                    game_info.password = password;

                    if (result.IsSuccessStatusCode)
                    {
                        GameRoundData game_round_data = new GameRoundData()
                        {
                            player = player,
                            password = password,
                            server = server,
                            game = game_info,
                            round = null
                        };
                        return View("GetGame", game_round_data);
                    }
                    else
                    {
                        TempData["Message"] = JsonConvert.SerializeObject(new AlertMessage()
                        {
                            Text = game_info.msg,
                            Tipo = Alerta.danger.ToString(),
                            Status = game_info.status
                        });
                        return RedirectToAction("GameSearch", new { name, status = "", page = 0, limit = 50, server });
                    }
                }
            }catch (Exception) {
                return View("Index");
            }
        }

        public GameData GameStart(string gameId, string password, string player, string server)
        {
            string xMsg_value = string.Empty;
            using (var client = new HttpClient())
            {
                // URL del servicio
                string baseUrl = server + "/api/games/" + gameId + "/start";

                // Crear una instancia de HttpRequestMessage en lugar de usar una URL
                var request = new HttpRequestMessage(HttpMethod.Head, baseUrl);

                // Agregar los valores de "password" y "player" como encabezados
                if (!string.IsNullOrEmpty(password)) request.Headers.Add("password", password);
                if (!string.IsNullOrEmpty(player)) request.Headers.Add("player", player);

                // Realizar la solicitud HTTP
                var response = client.SendAsync(request);
                response.Wait();

                var result = response.Result;

                // Obtener el valor del encabezado "X-msg" si existe
                if (result.Headers.TryGetValues("X-msg", out IEnumerable<string> values))
                {
                    xMsg_value = values.First();
                }

                return new GameData() {
                    status = (int) result.StatusCode,
                    msg = xMsg_value
                };
            }

        }

        // GET: ContaminaDOSController
        public RoundsSearch GetRounds(string gameId, string password, string player, string server)
        {
            RoundsSearch rounds_data = new RoundsSearch();
            using (var client = new HttpClient())
            {
                // URL del servicio
                string baseUrl = server + "/api/games/" + gameId + "/rounds";

                // Crear una instancia de HttpRequestMessage en lugar de usar una URL
                var request = new HttpRequestMessage(HttpMethod.Get, baseUrl);

                // Agregar los valores de "password" y "player" como encabezados
                if (!string.IsNullOrEmpty(password)) request.Headers.Add("password", password);
                if (!string.IsNullOrEmpty(player)) request.Headers.Add("player", player);

                // Realizar la solicitud HTTP
                var response = client.SendAsync(request);
                response.Wait();

                var result = response.Result;
                if (result.IsSuccessStatusCode)
                {
                    var readResponse = result.Content.ReadFromJsonAsync<RoundsSearch>();
                    readResponse.Wait();

                    rounds_data = readResponse.Result;
                }
            }
            return rounds_data;
        }

        // GET: ContaminaDOSController
        public RoundData ShowRound(string gameId, string roundId, string password, string player, string server)
        {
            RoundData round_info = new RoundData();
            using (var client = new HttpClient())
            {
                // URL del servicio
                string baseUrl = server + "/api/games/" + gameId + "/rounds/" + roundId;

                // Crear una instancia de HttpRequestMessage en lugar de usar una URL
                var request = new HttpRequestMessage(HttpMethod.Get, baseUrl);

                // Agregar los valores de "password" y "player" como encabezados
                if (!string.IsNullOrEmpty(password)) request.Headers.Add("password", password);
                if (!string.IsNullOrEmpty(player)) request.Headers.Add("player", player);

                // Realizar la solicitud HTTP
                var response = client.SendAsync(request);
                response.Wait();

                var result = response.Result;
                if (result.IsSuccessStatusCode)
                {
                    var readResponse = result.Content.ReadFromJsonAsync<RoundData>();
                    readResponse.Wait();

                    round_info = readResponse.Result;
                }
            }
            return round_info;
        }

        public RoundData ProposeGroup(string group_p, string gameId, string roundId, string password, string player, string server)
        {
            RoundData round_info = new RoundData();
            using (var client = new HttpClient())
            {
                // URL del servicio
                string baseUrl = server + "/api/games/" + gameId + "/rounds/" + roundId;

                // Crear una instancia de HttpRequestMessage en lugar de usar una URL
                var request = new HttpRequestMessage(HttpMethod.Patch, baseUrl);

                // Agregar los valores de "password" y "player" como encabezados
                if (!string.IsNullOrEmpty(password)) request.Headers.Add("password", password);
                if (!string.IsNullOrEmpty(player)) request.Headers.Add("player", player);

                // Agregar el objeto new_player como contenido en el cuerpo de la solicitud
                var json = new StringContent(group_p, Encoding.UTF8, "application/json");
                request.Content = json;

                // Realizar la solicitud HTTP
                var response = client.SendAsync(request);
                response.Wait();

                var result = response.Result;
                var readResponse = result.Content.ReadFromJsonAsync<RoundData>();
                readResponse.Wait();

                round_info = readResponse.Result;
            }
            return round_info;
        }

        public RoundData VoteGroup(string vote_p, string gameId, string roundId, string password, string player, string server)
        {
            RoundData round_info = new RoundData();
            using (var client = new HttpClient())
            {
                // URL del servicio
                string baseUrl = server + "/api/games/" + gameId + "/rounds/" + roundId;

                // Crear una instancia de HttpRequestMessage en lugar de usar una URL
                var request = new HttpRequestMessage(HttpMethod.Post, baseUrl);

                // Agregar los valores de "password" y "player" como encabezados
                if (!string.IsNullOrEmpty(password)) request.Headers.Add("password", password);
                if (!string.IsNullOrEmpty(player)) request.Headers.Add("player", player);

                // Agregar el objeto new_player como contenido en el cuerpo de la solicitud
                var json = new StringContent(vote_p, Encoding.UTF8, "application/json");
                request.Content = json;

                // Realizar la solicitud HTTP
                var response = client.SendAsync(request);
                response.Wait();

                var result = response.Result;

                var readResponse = result.Content.ReadFromJsonAsync<RoundData>();
                readResponse.Wait();

                round_info = readResponse.Result;

            }
            return round_info;
        }

        public RoundData SubmitAction(string action_p, string gameId, string roundId, string password, string player, string server)
        {
            RoundData round_info = new RoundData();
            using (var client = new HttpClient())
            {
                // URL del servicio
                string baseUrl = server + "/api/games/" + gameId + "/rounds/" + roundId;

                // Crear una instancia de HttpRequestMessage en lugar de usar una URL
                var request = new HttpRequestMessage(HttpMethod.Put, baseUrl);

                // Agregar los valores de "password" y "player" como encabezados
                if (!string.IsNullOrEmpty(password)) request.Headers.Add("password", password);
                if (!string.IsNullOrEmpty(player)) request.Headers.Add("player", player);

                // Agregar el objeto new_player como contenido en el cuerpo de la solicitud
                var json = new StringContent(action_p, Encoding.UTF8, "application/json");
                request.Content = json;

                // Realizar la solicitud HTTP
                var response = client.SendAsync(request);
                response.Wait();

                var result = response.Result;
                if (result.IsSuccessStatusCode)
                {
                    var readResponse = result.Content.ReadFromJsonAsync<RoundData>();
                    readResponse.Wait();

                    round_info = readResponse.Result;
                }
            }
            return round_info;
        }

    }
}
