namespace ContaminaDOS_BackEnd.Models.RequestBody
{
    public class GameCreate
    {
        public string name { get; set; }
        public string owner { get; set; }
        public string ? password { get; set; }
    }
}
