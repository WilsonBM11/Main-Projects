namespace contaminaDOS_JVW.Models
{
    public class RoundsSearch
    {
        public int status { get; set; }
        public string msg { get; set; }
        public List<Data> data { get; set; }

        public class Data
        {
            public string id { get; set; }
            public string leader { get; set; }
            public string status { get; set; }
            public string result { get; set; }
            public string phase { get; set; }
            public List<string> group { get; set; }
            public List<bool> votes { get; set; }
        }

    }
}
