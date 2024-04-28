package org.example;

import java.util.*;

import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main2 {

    public static void main(String[] args) throws IOException {
        Map<String, Integer> mapa = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Ingresa un texto (o 'salir' para terminar):");
            String texto = scanner.nextLine();

            if (texto.equalsIgnoreCase("salir")) {
                break;
            }

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
        }

        System.out.println("Conteo de texto:");
        for (Map.Entry<String, Integer> entry : mapa.entrySet()) {
            String texto = entry.getKey();
            int contador = entry.getValue();
            System.out.println(texto + ": " + contador);
        }

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
    }
}
