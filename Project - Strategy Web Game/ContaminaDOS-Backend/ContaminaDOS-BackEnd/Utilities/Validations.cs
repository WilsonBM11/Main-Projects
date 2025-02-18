using ContaminaDOS_BackEnd.Models;
using ContaminaDOS_BackEnd.Models.ResponseBody;
using Microsoft.IdentityModel.Tokens;

namespace ContaminaDOS_BackEnd.Utilities
{
    public class Validations
    {
        public void GameExists(Models.Game game, bool existence)
        {
            // existence -> true = Si existe lanza un error
            if (game == null && !existence)
                throw new ServerException("The specified resource was not found", 404);
            if (game != null && existence)
                throw new ServerException("Asset already exists", 409);
        }

        public void RoundExists(GameRound round)
        {
            if (round == null)
            {
                throw new ServerException("The specified resource was not found", 404);
            }
        }

        public void GroupExists(RoundGroup group)
        {
            if (group == null)
            {
                throw new ServerException("The specified resource was not found", 404);
            }
        }

        public void ValidatePassword(Models.Game game, string password)
        {
            if (game.use_password && !game.game_password.Equals(password))
                throw new ServerException("Invalid credentials", 401);
        }

        public void ValidatePlayer(Models.Game game, string player, bool existence)
        {
            // existence -> true = Si existe lanza un error
            if (game.Players.Where(x => x.player_name == player).IsNullOrEmpty() && !existence)
                throw new ServerException("Not part of the game", 403);
            if (!game.Players.Where(x => x.player_name == player).IsNullOrEmpty() && existence)
                throw new ServerException("Player is already part of the game", 403);
        }

        public void ValidateNewPlayer(string player, string new_player)
        {
            if (player != new_player)
            {
                throw new ServerException("Client Error", 400);
            }
        }

        public void ValidateLength(string input)
        {
            if (input.Length < 3 || input.Length > 20)
            {
                throw new ServerException("The strings must have more than 3 and less than 20 characters", 400);
            }
        }

    }
}
