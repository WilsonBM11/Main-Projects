package dev.leocamacho.authentication.handlers.commands;

import dev.leocamacho.authentication.exceptions.BusinessException;
import dev.leocamacho.authentication.exceptions.InvalidInputException;
import dev.leocamacho.authentication.jpa.entities.UserEntity;
import dev.leocamacho.authentication.jpa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegisterUserHandler {

    private final UserRepository repository;

    private final PasswordEncoder encoder;

    private final JmsTemplate jmsTemplate;

    @Autowired
    public RegisterUserHandler(UserRepository repository, PasswordEncoder encoder, JmsTemplate jmsTemplate) {
        this.repository = repository;
        this.encoder = encoder;
        this.jmsTemplate = jmsTemplate;
    }

    public record Command(String email, String name, String password) {
    }

    public void register(Command command) {
        validateRequiredFields(command);
        validateExistingUser(command.email());
        UserEntity user = new UserEntity();
        user.setEmail(command.email());
        user.setName(command.name());
        user.setPassword(encoder.encode(command.password()));
        user.setRoles(List.of("ACCOUNT_MANAGER"));

        try {
            repository.save(user);
            jmsTemplate.convertAndSend("message", messageToEmail(user, command));
            jmsTemplate.convertAndSend("subject", "Registro exitoso");
            jmsTemplate.convertAndSend("toUser", user.getEmail());
        } catch (RuntimeException ex) {
            throw new BusinessException("Error sending email");
        }
    }

    private void validateExistingUser(String email) {
        if (repository.findByEmail(email).isPresent()) {
            throw new BusinessException("User already exists");
        }
    }

    private void validateRequiredFields(Command command) {
        if (command.email().isBlank() || command.name().isBlank() || command.password().isBlank()) {
            throw new InvalidInputException("All fields are required");
        }
    }

    public String messageToEmail(UserEntity user, Command command) {
        return "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta http-equiv='X-UA-Compatible' content='IE=edge'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Bienvenido a nuestra plataforma</title>" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            background-color: #f4f4f4;" +
                "            color: #333;" +
                "            margin: 2rem;" +
                "            padding: 0;" +
                "        }" +
                "        .container {" +
                "            width: 80%;" +
                "            margin: auto;" +
                "            background-color: #fff;" +
                "            padding: 20px;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);" +
                "        }" +
                "        .header {" +
                "            text-align: center;" +
                "            border-bottom: 1px solid #ddd;" +
                "            padding-bottom: 10px;" +
                "            margin-bottom: 20px;" +
                "        }" +
                "        .header h1 {" +
                "            color: #5cb85c;" +
                "        }" +
                "        .content p {" +
                "            font-size: 1.1em;" +
                "        }" +
                "        .details {" +
                "            list-style-type: none;" +
                "            padding: 0;" +
                "        }" +
                "        .details li {" +
                "            margin: 10px 0;" +
                "        }" +
                "        .details b {" +
                "            display: inline-block;" +
                "            width: 150px;" +
                "        }" +
                "        .footer {" +
                "            text-align: center;" +
                "            font-size: 0.9em;" +
                "            color: #777;" +
                "            margin-top: 20px;" +
                "            border-top: 1px solid #ddd;" +
                "            padding-top: 10px;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>" +
                "            <h1>EasyTours</h1>" +
                "            <h2>Bienvenido a nuestra plataforma</h2>"+
                "        </div>" +
                "        <div class='content'>" +
                "            <p>Hola " + user.getName() + ",</p>" +
                "            <p>Bienvenido a nuestra plataforma. El equipo de EasyTours te da la bienvenida y te desea un viaje agradable</p>"+
                "            <p>Detalles de su cuenta:</p>" +
                "            <ul class='details'>" +
                "                <li><b>Email:</b> " + user.getEmail() + "</li>" +
                "            </ul>" +
                "            <p>Ahora puede iniciar sesión en nuestra plataforma y comenzar a utilizar nuestros servicios.</p>" +
                "            <p>Gracias por elegirnos!</p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>¡Viaja bien, viaja seguro, viaja con EasyTours!</p>" +
                "            <p>Atentamente,<br>El equipo de EasyTours</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }


}