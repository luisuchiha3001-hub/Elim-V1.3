package com.sena.elim.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsuarioController {

    // mapeo por GET /registro - muestra la vista de registro
    @GetMapping("/registro")
    public String mostrarRegistro(Authentication authentication,
                                  HttpSession session,
                                  @RequestParam(value = "error", required = false) String error,
                                  @RequestParam(value = "exito", required = false) String exito,
                                  Model model) {

        // redirección si ya hay una sesión activa (equivalente a isset($_SESSION['usuario']))
        boolean haySesion = (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser"))
                || session.getAttribute("usuario") != null;

        if (haySesion) {
            return "redirect:/";
        }

        // transmisión de mensajes de error o éxito
        model.addAttribute("error", error);
        model.addAttribute("exito", exito);

        return "principal/registro"; // carga templates/principal/registro.html
    }

    // mapeo por POTS/registrar - procesa el envío del formulario
    @PostMapping("/registrar")
    public String registrarUsuario(@RequestParam("tipo_documento") String tipoDocumento,
                                   @RequestParam("documento") String documento,
                                   @RequestParam("nombres") String nombres,
                                   @RequestParam("apellidos") String apellidos,
                                   @RequestParam("correo") String correo,
                                   @RequestParam("telefono") String telefono,
                                   @RequestParam("direccion") String direccion,
                                   @RequestParam("password") String password,
                                   @RequestParam("confirm_password") String confirmPassword) {

        // Validación básica de coincidencia de contraseñas
        if (!password.equals(confirmPassword)) {
            return "redirect:/registro?error=Las+contraseñas+no+coinciden";
        }

        // llamar servicio de base de datos para guardar el usuario

        return "redirect:/login?exito=Usuario+registrado+correctamente";
    }
    // GET /login - Muestra la vista de login
    @GetMapping("/login")
    public String mostrarLogin(Authentication authentication,
                               HttpSession session,
                               @RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "exito", required = false) String exito,
                               Model model) {

        // 1. Redirección si ya hay una sesión activa (equivalente a isset($_SESSION['usuario']))
        boolean haySesion = (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser"))
                || session.getAttribute("usuario") != null;

        if (haySesion) {
            return "redirect:/";
        }

        // 2. Transmisión de mensajes de error o éxito a la vista
        model.addAttribute("error", error);
        model.addAttribute("exito", exito);

        return "principal/login"; // Carga templates/principal/login.html
    }

    // POST /login - Procesar inicio de sesión
    @PostMapping("/login")
    public String procesarLogin(@RequestParam("correo_login") String correo,
                                @RequestParam("clave_login") String clave,
                                HttpSession session) {

        // TODO: Validar credenciales con tu servicio/repositorio de base de datos
        // Ejemplo condicional simulado:
        boolean credencialesValidas = true;

        if (!credencialesValidas) {
            return "redirect:/login?error=Correo+o+contraseña+incorrectos";
        }

        // Simulación de guardado en sesión
        session.setAttribute("usuario", correo);
        session.setAttribute("nombre", "Usuario"); // Ajustar con el nombre real traído de la BD

        return "redirect:/";
    }
}