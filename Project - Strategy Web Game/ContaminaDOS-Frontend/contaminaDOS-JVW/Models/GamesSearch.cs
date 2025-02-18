namespace contaminaDOS_JVW.Models
{
    public class GamesSearch
    {
        public int status { get; set; }
        public string msg { get; set; }
        public List<Data> data { get; set; }
        public string server { get; set; }
        public class Data
        {
            public string id { get; set; }
            public string name { get; set; }
            public string status { get; set; }
            public bool password { get; set; }
            public string currentRound { get; set; }
            public List<string> players { get; set; }
            public List<string> enemies { get; set; }
        }

    }
}
