package com.sena.elim.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        Map<String, Object> usuario = (Map<String, Object>) session.getAttribute("usuario");

        Integer rolUsuario = null;
        if (usuario != null && usuario.get("id_rol_fk") != null) {
            rolUsuario = Integer.parseInt(usuario.get("id_rol_fk").toString());
        }

        if (rolUsuario == null || rolUsuario != 1) {
            return "redirect:/";
        }

        Integer totalProductos = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM productos", Integer.class);
        Integer stockBajo = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM productos WHERE stock < 6", Integer.class);
        Integer totalVentas = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM pedidos", Integer.class);
        Double ingresosTotales = jdbcTemplate.queryForObject("SELECT COALESCE(SUM(total_pago), 0) FROM pedidos", Double.class);

        String nombreAdmin = "Administrador";
        if (usuario.get("nombres") != null) {
            nombreAdmin = usuario.get("nombres").toString().trim().split(" ")[0];
        }

        String sqlTabla = "SELECT p.*, c.nombre_categoria " +
                "FROM productos p " +
                "LEFT JOIN categorias c ON p.id_categoria_fk = c.id_categoria " +
                "ORDER BY p.id_producto DESC";
        List<Map<String, Object>> productos = jdbcTemplate.queryForList(sqlTabla);

        model.addAttribute("totalProductos", totalProductos != null ? totalProductos : 0);
        model.addAttribute("stockBajo", stockBajo != null ? stockBajo : 0);
        model.addAttribute("totalVentas", totalVentas != null ? totalVentas : 0);
        model.addAttribute("ingresosTotales", ingresosTotales != null ? ingresosTotales.longValue() : 0);
        model.addAttribute("primerNombre", nombreAdmin);
        model.addAttribute("productos", productos);

        return "admin/dashboard";
    }

    @GetMapping("/agregar-pijama")
    public String mostrarAgregarPijama(HttpSession session,
                                       @RequestParam(value = "error", required = false) String error,
                                       @RequestParam(value = "exito", required = false) String exito,
                                       Model model) {

        @SuppressWarnings("unchecked")
        Map<String, Object> usuario = (Map<String, Object>) session.getAttribute("usuario");
        if (usuario == null || usuario.get("id_rol_fk") == null || Integer.parseInt(usuario.get("id_rol_fk").toString()) != 1) {
            return "redirect:/";
        }

        List<Map<String, Object>> categorias = jdbcTemplate.queryForList("SELECT id_categoria, nombre_categoria FROM categorias ORDER BY nombre_categoria");

        model.addAttribute("categorias", categorias);
        model.addAttribute("error", error);
        model.addAttribute("exito", exito);

        return "admin/agregar_pijama";
    }

    @PostMapping("/agregar-pijama")
    public String guardarPijama(@RequestParam("nombre_producto") String nombreProducto,
                                @RequestParam(value = "descripcion", required = false) String descripcion,
                                @RequestParam("precio") Double precio,
                                @RequestParam("stock") Integer stock,
                                @RequestParam("id_categoria_fk") Integer idCategoria,
                                @RequestParam(value = "estado", defaultValue = "Disponible") String estado,
                                @RequestParam(value = "imagen", required = false) MultipartFile imagen,
                                HttpSession session) {

        @SuppressWarnings("unchecked")
        Map<String, Object> usuario = (Map<String, Object>) session.getAttribute("usuario");
        if (usuario == null || usuario.get("id_rol_fk") == null || Integer.parseInt(usuario.get("id_rol_fk").toString()) != 1) {
            return "redirect:/";
        }

        String nombreImagen = null;

        if (imagen != null && !imagen.isEmpty()) {
            try {
                nombreImagen = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
                String rutaDirectorio = "src/main/resources/static/images/";
                File folder = new File(rutaDirectorio);
                if (!folder.exists()) folder.mkdirs();

                Path path = Paths.get(rutaDirectorio + nombreImagen);
                Files.write(path, imagen.getBytes());
            } catch (IOException e) {
                return "redirect:/admin/agregar-pijama?error=Error+al+subir+la+imagen";
            }
        }

        String sql = "INSERT INTO productos (nombre_producto, descripcion, precio, stock, imagen_url, id_categoria_fk, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, nombreProducto, descripcion, precio, stock, nombreImagen, idCategoria, estado);

        return "redirect:/admin/agregar-pijama?exito=Producto+guardado+correctamente";
    }
}