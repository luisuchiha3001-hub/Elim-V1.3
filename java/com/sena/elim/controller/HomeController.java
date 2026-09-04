package com.sena.elim.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    // Ruta para la página principal (http://localhost:8080/)
    @GetMapping({"/", "/index"})
    public String index(Authentication authentication, HttpSession session, Model model) {
        boolean haySesion = authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser");

        String nombreUsuario = "";

        if (haySesion) {
            // atributo personalizado si fue guardado al iniciar sesión
            if (session.getAttribute("nombres") != null) {
                nombreUsuario = session.getAttribute("nombres").toString();
            } else if (session.getAttribute("nombre") != null) {
                nombreUsuario = session.getAttribute("nombre").toString();
            } else {
                // usar el nombre principal autenticado por Spring Security (correo o username)
                nombreUsuario = authentication.getName();
            }
        }

        model.addAttribute("haySesion", haySesion);
        model.addAttribute("nombreUsuario", nombreUsuario);

        return "index";
    }


}