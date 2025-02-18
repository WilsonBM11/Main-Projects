namespace ContaminaDOS_BackEnd.Models.ResponseBody
{
    public class GetGame
    {
        public int status { get; set; }
        public string msg { get; set; }
        public Game data { get; set; }
    }
}
