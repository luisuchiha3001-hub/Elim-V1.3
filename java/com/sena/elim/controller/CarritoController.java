package com.sena.elim.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    // Método auxiliar con la clave "usuario" que usa tu UsuarioController
    private boolean esUsuarioAutenticado(HttpSession session) {
        return session.getAttribute("usuario") != null;
    }

    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        if (!esUsuarioAutenticado(session)) {
            return "redirect:/login";
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) session.getAttribute("carrito");
        if (items == null) {
            items = new ArrayList<>();
        }

        double subtotal = 0.0;
        for (Map<String, Object> item : items) {
            double precio = Double.parseDouble(item.get("precio").toString());
            int cantidad = Integer.parseInt(item.get("cantidad").toString());
            subtotal += (precio * cantidad);
        }

        model.addAttribute("items", items);
        model.addAttribute("subtotal", (long) subtotal);

        return "principal/carrito";
    }

    @PostMapping("/eliminar")
    public String eliminarDelCarrito(@RequestParam("id_producto") Long idProducto, HttpSession session) {
        if (!esUsuarioAutenticado(session)) {
            return "redirect:/login";
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) session.getAttribute("carrito");

        if (items != null) {
            items.removeIf(item -> item.get("id").toString().equals(idProducto.toString()));
            session.setAttribute("carrito", items);
        }

        return "redirect:/carrito";
    }
}