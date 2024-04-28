package com.pe.qr.reader.kardex.controller;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

    private Map<String, Integer> mapa = new HashMap<>();

    @GetMapping("/")
    public String index(Model model) {
        int sum = 0;
        for (int value : mapa.values()) {
            sum += value;
        }
        model.addAttribute("totalObjects", sum);
        return "index";
    }

    @PostMapping("/save")
    @ResponseBody
    public String saveData(@RequestParam(name = "dataInput") String dataInput) {
        // Aquí puedes procesar el dato recibido y guardarlos en la base de datos o en cualquier otro lugar.
        // Por ejemplo:
        System.out.println("Received data: " + dataInput);
        agregarAlMapa(dataInput);
        // Puedes devolver una respuesta si es necesario.
        return "Data received successfully!";
    }

    @GetMapping("/favicon.ico")
    public String favicon() {
        System.out.println("favicon");
        return "forward:/resources/static/favicon.ico";
    }

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        // Exportar a excel
        String nombreArchivo = "conteo_texto.xlsx";

        Workbook libroExcel = WorkbookFactory.create(true);
        Sheet hoja = libroExcel.createSheet("Conteo de Texto");

        int filaActual = 0;

        for (Map.Entry<String, Integer> entry : mapa.entrySet()) {
            Row fila = hoja.createRow(filaActual++);
            Cell celdaTexto = fila.createCell(0);
            Cell celdaContador = fila.createCell(1);

            celdaTexto.setCellValue(entry.getKey());
            celdaContador.setCellValue(entry.getValue());
        }

        try (FileOutputStream outputStream = new FileOutputStream(nombreArchivo)) {
            libroExcel.write(outputStream);
            System.out.println("Datos exportados correctamente a " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al exportar los datos a Excel: " + e.getMessage());
        }

        // Establecer la respuesta del navegador para descargar el archivo Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

        // Escribir el libro de Excel en la respuesta del navegador
        libroExcel.write(response.getOutputStream());

        // Cerrar el libro de Excel
        libroExcel.close();
    }


    private void agregarAlMapa(String texto) {
        if (mapa.containsKey(texto)) {
            int contador = mapa.get(texto);
            mapa.put(texto, contador + 1);
        } else {
            mapa.put(texto, 1);
        }

        if (texto.contains("B-")) {
            System.out.println("Borrar un elemento: " + texto);
            String[] split = texto.split("B-");
            int contador = mapa.get(split[1]);
            mapa.put(split[1], contador - 1);
            mapa.remove(texto);
        }

//        System.out.println("Conteo de texto:");
//        for (Map.Entry<String, Integer> entry : mapa.entrySet()) {
//            String texto1 = entry.getKey();
//            int contador = entry.getValue();
//            System.out.println(texto1 + ": " + contador);
//        }
    }


}
