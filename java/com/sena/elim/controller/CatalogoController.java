package com.sena.elim.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/catalogo")
public class CatalogoController {

    // Método privado para dinamizar la lista de categorías del menú
    private List<Map<String, String>> obtenerCategorias(String categoriaExcluida) {
        List<Map<String, String>> todas = List.of(
                Map.of("nombre_categoria", "Niños", "url", "/catalogo/ninos"),
                Map.of("nombre_categoria", "Damas", "url", "/catalogo/damas"),
                Map.of("nombre_categoria", "Caballeros", "url", "/catalogo/caballeros"),
                Map.of("nombre_categoria", "Bebés", "url", "/catalogo/bebes")
        );

        return todas.stream()
                .filter(c -> !c.get("nombre_categoria").equalsIgnoreCase(categoriaExcluida))
                .toList();
    }

    @GetMapping("/ninos")
    public String catalogoNinos(HttpSession session, Model model) {
        List<Map<String, Object>> productos = new ArrayList<>();
        List<Map<String, String>> categorias = obtenerCategorias("Niños");

        List<?> carrito = (List<?>) session.getAttribute("carrito");
        int contadorCarrito = (carrito != null) ? carrito.size() : 0;

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("contadorCarrito", contadorCarrito);

        return "principal/ninos";
    }

    @GetMapping("/damas")
    public String catalogoDamas(HttpSession session, Model model) {
        List<Map<String, Object>> productos = new ArrayList<>();
        List<Map<String, String>> categorias = obtenerCategorias("Damas");

        List<?> carrito = (List<?>) session.getAttribute("carrito");
        int contadorCarrito = (carrito != null) ? carrito.size() : 0;

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("contadorCarrito", contadorCarrito);

        return "principal/damas";
    }

    @GetMapping("/caballeros")
    public String catalogoCaballeros(HttpSession session, Model model) {

        // 1. Obtener lista de productos de la categoría "Caballeros" (vía Service/BD)
        List<Map<String, Object>> productos = new ArrayList<>();

        // 2. Filtrar el resto de categorías para el dropdown
        List<Map<String, String>> categorias = obtenerCategorias("Caballeros");

        // 3. Conteo de elementos en el carrito desde la sesión
        List<?> carrito = (List<?>) session.getAttribute("carrito");
        int contadorCarrito = (carrito != null) ? carrito.size() : 0;

        // 4. Inyección de atributos al modelo Thymeleaf
        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("contadorCarrito", contadorCarrito);

        return "principal/caballeros"; // Renderiza templates/principal/caballeros.html
    }

    @GetMapping("/bebes")
    public String catalogoBebes(HttpSession session, Model model) {

        // 1. Obtener lista de productos de la categoría "Bebés" (vía Service/BD)
        List<Map<String, Object>> productos = new ArrayList<>();

        // 2. Filtrar el resto de categorías para el dropdown
        List<Map<String, String>> categorias = obtenerCategorias("Bebés");

        // 3. Conteo de ítems en carrito desde la sesión
        List<?> carrito = (List<?>) session.getAttribute("carrito");
        int contadorCarrito = (carrito != null) ? carrito.size() : 0;

        // 4. Inyección al modelo
        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("contadorCarrito", contadorCarrito);

        return "principal/bebes"; // Renderiza templates/principal/bebes.html
    }
}