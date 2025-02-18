namespace contaminaDOS_JVW.Models
{
    public class GameData
    {
        public string player { get; set; }
        public string password { get; set; }
        public int status { get; set; }
        public string msg { get; set; }
        public Data data { get; set; }

        public class Data
        {
            public string id { get; set; }
            public string name { get; set; }
            public string status { get; set; }
            public bool password { get; set; }
            public string currentRound { get; set; }
            public List<string> players { get; set; }
            public List<string> enemies { get; set; }
            public string owner { get; set; }
        }

    }
}
