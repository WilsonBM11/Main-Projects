namespace ContaminaDOS_BackEnd.Models.ResponseBody
{
    public class Round
    {
        public string id { get; set; }
        public string leader { get; set; }
        public string status { get; set; }
        public string result { get; set; }
        public string phase { get; set; }
        public string createdAt { get; set; }
        public string updatedAt { get; set; }
        public List<string> group { get; set; }
        public List<bool> votes { get; set; }
    }
}
