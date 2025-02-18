namespace ContaminaDOS_BackEnd.Models.ResponseBody
{
    public class GetRound
    {
        public int status { get; set; }
        public string msg { get; set; }
        public Round data { get; set; }
    }
}
