namespace DemoEF.Models
{
    public class AlertMessage
    {
        public string Text { get; set; }
        public string Tipo { get; set; }
        public int Status { get; set; }

    }

    public enum Alerta
    {
        primary,
        success,
        warning,
        danger

    };

}



