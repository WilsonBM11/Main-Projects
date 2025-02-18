namespace ContaminaDOS_BackEnd.Models.ResponseBody
{
    public class GetRounds
    {
        public int status { get; set; }
        public string msg { get; set; }
        public List<Round> data { get; set; }
    }
}
