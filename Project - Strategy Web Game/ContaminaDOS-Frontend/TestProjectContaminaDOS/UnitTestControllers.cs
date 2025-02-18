using contaminaDOS_JVW.Controllers;
using contaminaDOS_JVW.Models;
using System.Diagnostics;

namespace TestProjectContaminaDOS
{
    [TestClass]
    public class UnitTestControllers
    {
        [TestMethod]
        public void TestGameSearch()
        {
            // Arrange
            ContaminaDOSController controller = new ContaminaDOSController();

            //Game game = new Game() { name = "TestDos", owner = "desconocido", password = "pass"};

            //Player player = new Player() { player = "THOR"};

            List<string> group = new List<string>() { "THOR" ,"TonyStark"};

            //UserSession.server = "https://contaminados.meseguercr.com/api/games/";
            // Act
            //GamesSearch result = controller.GameSearch("","lobby");
            //GameData result = controller.JoinGame(player, "650664d0f411279eb60992d4", "groot", player.player);
            //GameData result = controller.GetGame("650664d0f411279eb60992d4", "groot", "THOR");
            //string result = controller.GameStart("651a2840c0e96c62714b02e3","" ,"wilson");
            //RoundsSearch result = controller.GetRounds("650664d0f411279eb60992d4", "groot", "THOR");
            //RoundData result = controller.ShowRound("650664d0f411279eb60992d4", "6507a74ef411279eb6099398", "groot", "THOR");
            //RoundData result = controller.ProposeGroup(group, "650664d0f411279eb60992d4", "6507a74ef411279eb6099398", "groot", "THOR");
            //RoundData result = controller.VoteGroup(vote, "650664d0f411279eb60992d4", "6507a74ef411279eb6099398", "groot", "THOR");
            // Assert
            //Assert.IsNotNull(result); // Verifica que el resultado no sea nulo

            //Debug.WriteLine($"Contenido de result: {result}");

        }
    }
}