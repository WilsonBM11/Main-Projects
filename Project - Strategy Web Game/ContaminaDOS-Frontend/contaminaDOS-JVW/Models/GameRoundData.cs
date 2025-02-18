namespace contaminaDOS_JVW.Models
{
    public class GameRoundData
    {
        public string player { get; set; }
        public string password { get; set; }
        public string result { get; set; }
        public string server { get; set; }
        public List<string> resultList {get; set;} 
        public int count_rounds { get; set; }
        public GameData game { get; set; }
        public RoundData round { get; set; }
    }
}
