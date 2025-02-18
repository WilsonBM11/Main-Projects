namespace ContaminaDOS_BackEnd.Models
{
    public class ServerException : Exception
    {
        public int status { get; }

        public ServerException(string message, int statusCode) : base(message)
        {
            status = statusCode;
        }
    }
}
