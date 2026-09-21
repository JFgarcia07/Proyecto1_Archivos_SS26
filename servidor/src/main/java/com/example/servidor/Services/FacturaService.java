package com.example.servidor.Services;

import com.example.servidor.Model.DetalleVenta;
import com.example.servidor.Model.Venta;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;

@Service
public class FacturaService {
    
    private static final Color COLOR_PRINCIPAL = new Color(13, 110, 253);
    private static final Color COLOR_FONDO_TOTAL = new Color(233, 236, 239);

    private final VentaService ventaService;

    public FacturaService(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    /**
     * Genera el PDF de la factura de una venta.
     *
     * @return los bytes del PDF, o null si la venta no existe o hubo error
     */
    public byte[] generarFactura(Integer idVenta) {
        Venta venta = ventaService.buscarPorId(idVenta);

        if (venta == null) {
            return null;
        }

        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        Document documento = new Document(PageSize.LETTER, 40, 40, 40, 40);

        try {
            PdfWriter.getInstance(documento, salida);
            documento.open();

            agregarEncabezado(documento, venta);
            agregarDatosCliente(documento, venta);
            agregarTablaProductos(documento, venta);
            agregarTotales(documento, venta);
            agregarPie(documento);

            documento.close();
        } catch (DocumentException e) {
            System.err.println("Error al generar la factura: " + e.getMessage());
            return null;
        }

        return salida.toByteArray();
    }

    private void agregarEncabezado(Document documento, Venta venta) throws DocumentException {
        Font fuenteEmpresa = new Font(Font.HELVETICA, 18, Font.BOLD, COLOR_PRINCIPAL);
        Font fuenteFactura = new Font(Font.HELVETICA, 13, Font.BOLD);
        Font fuenteNormal = new Font(Font.HELVETICA, 10, Font.NORMAL);

        Paragraph empresa = new Paragraph("Mini ERP - Muebles para Hogar y Oficina", fuenteEmpresa);
        empresa.setAlignment(Element.ALIGN_CENTER);
        documento.add(empresa);

        Paragraph factura = new Paragraph("FACTURA No. " + venta.getIdVenta(), fuenteFactura);
        factura.setAlignment(Element.ALIGN_CENTER);
        factura.setSpacingBefore(6);
        documento.add(factura);

        Paragraph fecha = new Paragraph("Fecha: " + formatearFecha(venta.getFecha()), fuenteNormal);
        fecha.setAlignment(Element.ALIGN_CENTER);
        fecha.setSpacingAfter(18);
        documento.add(fecha);
    }

    private void agregarDatosCliente(Document documento, Venta venta) throws DocumentException {
        Font fuenteEtiqueta = new Font(Font.HELVETICA, 10, Font.BOLD);
        Font fuenteValor = new Font(Font.HELVETICA, 10, Font.NORMAL);

        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1.2f, 4f});
        tabla.setSpacingAfter(16);

        String atendidoPor = venta.getNombreUsuario() + " (" + venta.getIdUsuario() + ")";

        agregarFilaDato(tabla, "Cliente:", venta.getNombreCliente(), fuenteEtiqueta, fuenteValor);
        agregarFilaDato(tabla, "NIT:", venta.getNitCliente(), fuenteEtiqueta, fuenteValor);
        agregarFilaDato(tabla, "Atendido por:", atendidoPor, fuenteEtiqueta, fuenteValor);

        documento.add(tabla);
    }

    private void agregarFilaDato(PdfPTable tabla, String etiqueta, String valor,
                                 Font fuenteEtiqueta, Font fuenteValor) {
        PdfPCell celdaEtiqueta = new PdfPCell(new Phrase(etiqueta, fuenteEtiqueta));
        celdaEtiqueta.setBorder(Rectangle.NO_BORDER);
        celdaEtiqueta.setPaddingBottom(4);
        tabla.addCell(celdaEtiqueta);

        PdfPCell celdaValor = new PdfPCell(new Phrase(valor, fuenteValor));
        celdaValor.setBorder(Rectangle.NO_BORDER);
        celdaValor.setPaddingBottom(4);
        tabla.addCell(celdaValor);
    }

    private void agregarTablaProductos(Document documento, Venta venta) throws DocumentException {
        Font fuenteEncabezado = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
        Font fuenteCelda = new Font(Font.HELVETICA, 10, Font.NORMAL);

        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1.2f, 4f, 1.3f, 1.8f, 1.8f});

        agregarCeldaEncabezado(tabla, "Código", fuenteEncabezado);
        agregarCeldaEncabezado(tabla, "Producto", fuenteEncabezado);
        agregarCeldaEncabezado(tabla, "Cantidad", fuenteEncabezado);
        agregarCeldaEncabezado(tabla, "Precio Unit.", fuenteEncabezado);
        agregarCeldaEncabezado(tabla, "Importe", fuenteEncabezado);

        List<DetalleVenta> detalles = venta.getDetalles();

        for (int i = 0; i < detalles.size(); i++) {
            DetalleVenta detalle = detalles.get(i);
            double importe = detalle.getCantidad() * detalle.getPrecioUnitario();

            agregarCelda(tabla, detalle.getIdProducto(), fuenteCelda, Element.ALIGN_LEFT);
            agregarCelda(tabla, detalle.getNombreProducto(), fuenteCelda, Element.ALIGN_LEFT);
            agregarCelda(tabla, formatearCantidad(detalle.getCantidad()), fuenteCelda, Element.ALIGN_RIGHT);
            agregarCelda(tabla, formatearMoneda(detalle.getPrecioUnitario()), fuenteCelda, Element.ALIGN_RIGHT);
            agregarCelda(tabla, formatearMoneda(importe), fuenteCelda, Element.ALIGN_RIGHT);
        }

        documento.add(tabla);
    }

    private void agregarCeldaEncabezado(PdfPTable tabla, String texto, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBackgroundColor(COLOR_PRINCIPAL);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(6);
        tabla.addCell(celda);
    }

    private void agregarCelda(PdfPTable tabla, String texto, Font fuente, int alineacion) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setHorizontalAlignment(alineacion);
        celda.setPadding(5);
        tabla.addCell(celda);
    }

    private void agregarTotales(Document documento, Venta venta) throws DocumentException {
        Font fuenteNormal = new Font(Font.HELVETICA, 10, Font.NORMAL);
        Font fuenteTotal = new Font(Font.HELVETICA, 11, Font.BOLD);

        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(40);
        tabla.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tabla.setSpacingBefore(10);

        agregarFilaTotal(tabla, "Subtotal:", formatearMoneda(venta.getSubtotal()), fuenteNormal, false);
        agregarFilaTotal(tabla, "IVA (12%):", formatearMoneda(venta.getIva()), fuenteNormal, false);
        agregarFilaTotal(tabla, "TOTAL:", formatearMoneda(venta.getTotal()), fuenteTotal, true);

        documento.add(tabla);
    }

    private void agregarFilaTotal(PdfPTable tabla, String etiqueta, String valor,
                                  Font fuente, boolean resaltar) {
        PdfPCell celdaEtiqueta = new PdfPCell(new Phrase(etiqueta, fuente));
        celdaEtiqueta.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaEtiqueta.setPadding(5);

        PdfPCell celdaValor = new PdfPCell(new Phrase(valor, fuente));
        celdaValor.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaValor.setPadding(5);

        if (resaltar) {
            celdaEtiqueta.setBackgroundColor(COLOR_FONDO_TOTAL);
            celdaValor.setBackgroundColor(COLOR_FONDO_TOTAL);
        }

        tabla.addCell(celdaEtiqueta);
        tabla.addCell(celdaValor);
    }

    private void agregarPie(Document documento) throws DocumentException {
        Font fuentePie = new Font(Font.HELVETICA, 9, Font.ITALIC, Color.GRAY);

        Paragraph pie = new Paragraph("Gracias por su compra", fuentePie);
        pie.setAlignment(Element.ALIGN_CENTER);
        pie.setSpacingBefore(30);
        documento.add(pie);
    }

    private String formatearMoneda(Double valor) {
        if (valor == null) {
            return "Q 0.00";
        }
        return "Q " + String.format(Locale.US, "%,.2f", valor);
    }

    private String formatearCantidad(Double cantidad) {
        if (cantidad == null) {
            return "0";
        }
        if (cantidad == Math.floor(cantidad)) {
            return String.valueOf(cantidad.intValue());
        }
        return String.format(Locale.US, "%.2f", cantidad);
    }

    private String formatearFecha(String fecha) {
        if (fecha == null) {
            return "";
        }
        if (fecha.length() >= 16) {
            return fecha.substring(0, 16);
        }
        return fecha;
    }
}
