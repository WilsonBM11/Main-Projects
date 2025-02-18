using System;
using System.IO;
using System.Text;
using System.Threading.Tasks;
using ContaminaDOS_BackEnd.Models.ResponseBody;
using Microsoft.AspNetCore.Http;

namespace ContaminaDOS_BackEnd.Middleware;

public class Middleware
{
    private readonly RequestDelegate _next;

    public Middleware(RequestDelegate next)
    {
        _next = next;
    }

    public async Task Invoke(HttpContext context)
    {
        if (context.Request.ContentType != null && context.Request.ContentType.Contains("application/json", StringComparison.OrdinalIgnoreCase))
        {
            try
            {
                // Leer el cuerpo de la solicitud y validar si es un JSON válido
                using (var reader = new StreamReader(context.Request.Body, Encoding.UTF8))
                {
                    var requestBody = await reader.ReadToEndAsync();

                    if (string.IsNullOrWhiteSpace(requestBody))
                    {
                        context.Response.StatusCode = 400;
                        await context.Response.WriteAsJsonAsync(new Error() { status = 400, msg = "Invalid missing data" });
                        return;
                    }

                    // Se restaura el body para ser leido por los controladores
                    context.Request.Body = new MemoryStream(Encoding.UTF8.GetBytes(requestBody));
                }
            }
            catch (Exception)
            {
                context.Response.StatusCode = 400;
                await context.Response.WriteAsJsonAsync(new Error() { status = 400, msg = "The request is not valid" });
                return;
            }
        }

        await _next(context);
    }

}