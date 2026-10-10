import os
from reportlab.lib.pagesizes import letter
from reportlab.lib import colors
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, PageBreak, KeepTogether, HRFlowable
)
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.pdfgen import canvas
import shutil

class NumberedCanvas(canvas.Canvas):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self._saved_page_states = []

    def showPage(self):
        self._saved_page_states.append(dict(self.__dict__))
        self._startPage()

    def save(self):
        num_pages = len(self._saved_page_states)
        for state in self._saved_page_states:
            self.__dict__.update(state)
            self.draw_page_decorations(num_pages)
            super().showPage()
        super().save()

    def draw_page_decorations(self, page_count):
        self.saveState()
        self.setFont("Helvetica", 8)
        self.setFillColor(colors.HexColor("#718096"))
        
        # Header (páginas > 1)
        if self._pageNumber > 1:
            self.drawString(48, 752, "Yireh Store — Modelo en Estrella & Data Warehouse")
            self.setStrokeColor(colors.HexColor("#E2E8F0"))
            self.setLineWidth(0.5)
            self.line(48, 746, 564, 746)
        
        # Footer en todas las páginas
        footer_text = f"Página {self._pageNumber} de {page_count}"
        self.drawRightString(564, 32, footer_text)
        self.drawString(48, 32, "Documento de Arquitectura Analítica de Datos — Proyecto de Aula")
        self.setStrokeColor(colors.HexColor("#E2E8F0"))
        self.setLineWidth(0.5)
        self.line(48, 42, 564, 42)
        self.restoreState()

def build_pdf(filename):
    doc = SimpleDocTemplate(
        filename,
        pagesize=letter,
        leftMargin=48,
        rightMargin=48,
        topMargin=46,
        bottomMargin=46
    )

    styles = getSampleStyleSheet()
    
    PRIMARY = colors.HexColor("#1A365D")   # Deep navy blue
    SECONDARY = colors.HexColor("#2B6CB0") # Medium blue
    DARK_TEXT = colors.HexColor("#2D3748") # Slate body
    BG_LIGHT = colors.HexColor("#F7FAFC")
    LINE_BORDER = colors.HexColor("#CBD5E0")
    
    title_style = ParagraphStyle(
        'DocTitle',
        parent=styles['Heading1'],
        fontName='Helvetica-Bold',
        fontSize=21,
        leading=25,
        textColor=PRIMARY,
        spaceAfter=4
    )
    
    subtitle_style = ParagraphStyle(
        'DocSub',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=11,
        leading=14,
        textColor=SECONDARY,
        spaceAfter=10
    )
    
    h1_style = ParagraphStyle(
        'Heading1_Custom',
        parent=styles['Heading1'],
        fontName='Helvetica-Bold',
        fontSize=13,
        leading=16,
        textColor=PRIMARY,
        spaceBefore=10,
        spaceAfter=6,
        keepWithNext=True
    )

    h2_style = ParagraphStyle(
        'Heading2_Custom',
        parent=styles['Heading2'],
        fontName='Helvetica-Bold',
        fontSize=10.5,
        leading=13,
        textColor=SECONDARY,
        spaceBefore=6,
        spaceAfter=3,
        keepWithNext=True
    )

    body_style = ParagraphStyle(
        'Body_Custom',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=9,
        leading=12.5,
        textColor=DARK_TEXT,
        spaceAfter=4
    )

    cell_style = ParagraphStyle(
        'TableCell',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=8,
        leading=10.5,
        textColor=DARK_TEXT
    )

    table_header_style = ParagraphStyle(
        'TableHeader',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=8,
        leading=10.5,
        textColor=colors.white
    )

    story = []

    # ==================== PÁGINA 1 ====================
    story.append(Paragraph("MODELO EN ESTRELLA (STAR SCHEMA)", title_style))
    story.append(Paragraph("Arquitectura de Data Warehouse y Analítica — Yireh Store Cafetería", subtitle_style))
    story.append(HRFlowable(width="100%", thickness=1.5, color=PRIMARY, spaceBefore=0, spaceAfter=8))

    story.append(Paragraph("1. Introducción y Propósito Analítico", h1_style))
    story.append(Paragraph(
        "El presente informe detalla el diseño del <b>Modelo en Estrella (Star Schema)</b> para el sistema "
        "<b>Yireh Store</b>. El objetivo es transformar el modelo relacional transaccional (OLTP) de la cafetería "
        "en una estructura dimensional optimizada para <b>Business Intelligence (BI)</b> y analítica de alto desempeño. "
        "Este modelo permite evaluar la rentabilidad por producto, el comportamiento de compra estudiantil, la eficiencia "
        "operativa en caja (Z-Reports), la satisfacción del cliente (CSAT) y el ciclo de vida de los activos físicos.",
        body_style
    ))

    story.append(Paragraph("2. Incorporación de las Nuevas Tablas del Negocio", h1_style))
    story.append(Paragraph(
        "A diferencia del esquema inicial de catálogo y carritos, la versión actual de la base de datos incorpora "
        "cuatro componentes operativos fundamentales que se integran en el modelo dimensional:",
        body_style
    ))

    table_data_tables = [
        [Paragraph("Entidad OLTP", table_header_style), Paragraph("Rol Transaccional", table_header_style), Paragraph("Mapeo Dimensional (Data Warehouse)", table_header_style)],
        [
            Paragraph("<b>cash_closures</b>", cell_style),
            Paragraph("Control de turnos, base de apertura, ventas y arqueo físico.", cell_style),
            Paragraph("<b>DIM_CAJERO_TURNO</b>: Permite auditar mermas, descuadres y rendimiento por cajero.", cell_style)
        ],
        [
            Paragraph("<b>satisfaction_surveys</b>", cell_style),
            Paragraph("Calificación 1-5 estrellas, tags rápidos y comentarios CSAT.", cell_style),
            Paragraph("<b>DIM_SATISFACCION</b>: Cruza la percepción del servicio con tiempo de entrega y producto.", cell_style)
        ],
        [
            Paragraph("<b>recharges & balance</b>", cell_style),
            Paragraph("Recargas Nequi/Daviplata/Efectivo y saldo en billetera digital.", cell_style),
            Paragraph("<b>FACT_VENTAS</b> & <b>DIM_CLIENTE</b>: Medición del share de pago digital vs efectivo.", cell_style)
        ],
        [
            Paragraph("<b>physical_assets</b>", cell_style),
            Paragraph("Equipos, cafeteras, fechas de compra y depreciación.", cell_style),
            Paragraph("<b>DIM_ACTIVO_FISICO</b> / <b>FACT_MANTENIMIENTO</b>: Control de desgaste y costos.", cell_style)
        ]
    ]

    t_tables = Table(table_data_tables, colWidths=[110, 196, 210])
    t_tables.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, 0), PRIMARY),
        ('ALIGN', (0, 0), (-1, -1), 'LEFT'),
        ('VALIGN', (0, 0), (-1, -1), 'TOP'),
        ('TOPPADDING', (0, 0), (-1, -1), 4),
        ('BOTTOMPADDING', (0, 0), (-1, -1), 4),
        ('ROWBACKGROUNDS', (0, 1), (-1, -1), [colors.white, BG_LIGHT]),
        ('GRID', (0, 0), (-1, -1), 0.5, LINE_BORDER),
    ]))
    story.append(t_tables)

    story.append(PageBreak())

    # ==================== PÁGINA 2 ====================
    story.append(Paragraph("3. Estructura del Proceso Central: FACT_VENTAS", h1_style))
    story.append(Paragraph(
        "La tabla central del modelo analítico es <b>FACT_VENTAS</b>. Cada registro representa una línea individual "
        "de producto vendido (granularidad a nivel de ítem), lo que permite sumarizar métricas exactas por cualquier dimensión.",
        body_style
    ))

    fact_desc = [
        [Paragraph("Campo en FACT_VENTAS", table_header_style), Paragraph("Tipo", table_header_style), Paragraph("Descripción y Regla de Negocio", table_header_style)],
        [Paragraph("<b>venta_id</b> (PK)", cell_style), Paragraph("BIGINT", cell_style), Paragraph("Identificador único de la línea de hecho vendida.", cell_style)],
        [Paragraph("<b>tiempo_id</b> (FK)", cell_style), Paragraph("INT", cell_style), Paragraph("Enlace a DIM_TIEMPO (fecha exacta, turno, jornada).", cell_style)],
        [Paragraph("<b>producto_id</b> (FK)", cell_style), Paragraph("BIGINT", cell_style), Paragraph("Enlace a DIM_PRODUCTO (catálogo, categoría, barra).", cell_style)],
        [Paragraph("<b>cliente_id</b> (FK)", cell_style), Paragraph("BIGINT", cell_style), Paragraph("Enlace a DIM_CLIENTE (usuario y perfil de compra).", cell_style)],
        [Paragraph("<b>cajero_turno_id</b> (FK)", cell_style), Paragraph("BIGINT", cell_style), Paragraph("Enlace a DIM_CAJERO_TURNO (arqueo y cierre asociado).", cell_style)],
        [Paragraph("<b>canal_pago_id</b> (FK)", cell_style), Paragraph("INT", cell_style), Paragraph("Enlace a DIM_CANAL_PAGO (App Móvil vs POS; Billetera vs Efectivo).", cell_style)],
        [Paragraph("<b>satisfaccion_id</b> (FK)", cell_style), Paragraph("BIGINT", cell_style), Paragraph("Enlace a DIM_SATISFACCION (calificación CSAT recibida).", cell_style)],
        [Paragraph("<b>cantidad_vendida</b> (M)", cell_style), Paragraph("INT", cell_style), Paragraph("Métrica aditiva: Unidades físicas despachadas.", cell_style)],
        [Paragraph("<b>precio_unitario</b>", cell_style), Paragraph("DECIMAL(12,2)", cell_style), Paragraph("Precio unitario vigente al momento de la venta.", cell_style)],
        [Paragraph("<b>total_linea</b> (M)", cell_style), Paragraph("DECIMAL(12,2)", cell_style), Paragraph("Métrica aditiva: Ingreso bruto por línea vendida.", cell_style)],
        [Paragraph("<b>monto_billetera</b> (M)", cell_style), Paragraph("DECIMAL(12,2)", cell_style), Paragraph("Métrica aditiva: Fracción cubierta con billetera digital.", cell_style)],
        [Paragraph("<b>monto_efectivo</b> (M)", cell_style), Paragraph("DECIMAL(12,2)", cell_style), Paragraph("Métrica aditiva: Fracción cubierta en dinero físico.", cell_style)]
    ]
    t_fact = Table(fact_desc, colWidths=[130, 86, 300])
    t_fact.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, 0), SECONDARY),
        ('ALIGN', (0, 0), (-1, -1), 'LEFT'),
        ('VALIGN', (0, 0), (-1, -1), 'TOP'),
        ('TOPPADDING', (0, 0), (-1, -1), 3.5),
        ('BOTTOMPADDING', (0, 0), (-1, -1), 3.5),
        ('ROWBACKGROUNDS', (0, 1), (-1, -1), [colors.white, BG_LIGHT]),
        ('GRID', (0, 0), (-1, -1), 0.5, LINE_BORDER),
    ]))
    story.append(t_fact)
    story.append(Spacer(1, 10))

    story.append(Paragraph("4. Dimensiones Analíticas del Negocio", h1_style))
    dim_info = [
        ("DIM_TIEMPO", "Segmenta ventas en el tiempo (Año > Semestre > Mes > Día > Hora) e incluye 'Jornada Académica' (Mañana, Tarde, Noche)."),
        ("DIM_PRODUCTO", "Catálogo y categoría (bebidas frías, calientes, combos, repostería) y barra física de preparación."),
        ("DIM_CLIENTE", "Comunidad universitaria: frecuencia de compra, saldo histórico y adopción de la billetera."),
        ("DIM_CAJERO_TURNO", "Generada con cash_closures y usuarios CASHIER: apertura de gaveta, ventas teóricas, efectivo declarado y descuadre."),
        ("DIM_CANAL_PAGO", "Canal de venta (Móvil vs Mostrador POS) y medio de pago (Billetera, Efectivo, Tarjeta o Mixto)."),
        ("DIM_SATISFACCION", "Poblada desde satisfaction_surveys: rating de 1 a 5 estrellas, sentimiento y quick tags seleccionados.")
    ]
    for dim_title, dim_text in dim_info:
        story.append(Paragraph(f"• <b>{dim_title}:</b> {dim_text}", body_style))

    story.append(PageBreak())

    # ==================== PÁGINA 3 ====================
    story.append(Paragraph("5. Submodelo Especializado: Gestión de Activos Físicos", h1_style))
    story.append(Paragraph(
        "Para responder al mantenimiento y control de equipos (cafeteras industriales, molinos, congeladores y terminales táctiles), "
        "se define un submodelo satélite orientado a costos operativos y depreciación:",
        body_style
    ))

    asset_table_data = [
        [Paragraph("Entidad Dimensional", table_header_style), Paragraph("Tipo", table_header_style), Paragraph("Métricas / Atributos Clave", table_header_style)],
        [
            Paragraph("<b>DIM_ACTIVO_FISICO</b>", cell_style),
            Paragraph("Dimensión", cell_style),
            Paragraph("asset_code, nombre, categoría, ubicación, fecha_compra, vida_util_meses, estado (Operativo, En Mantenimiento, Baja).", cell_style)
        ],
        [
            Paragraph("<b>FACT_MANTENIMIENTO_DEPRECIACION</b>", cell_style),
            Paragraph("Tabla de Hechos", cell_style),
            Paragraph("costo_mantenimiento, depreciacion_acumulada, valor_en_libros, dias_para_proximo_servicio.", cell_style)
        ]
    ]
    t_asset = Table(asset_table_data, colWidths=[150, 76, 290])
    t_asset.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, 0), PRIMARY),
        ('ALIGN', (0, 0), (-1, -1), 'LEFT'),
        ('VALIGN', (0, 0), (-1, -1), 'TOP'),
        ('TOPPADDING', (0, 0), (-1, -1), 4),
        ('BOTTOMPADDING', (0, 0), (-1, -1), 4),
        ('ROWBACKGROUNDS', (0, 1), (-1, -1), [colors.white, BG_LIGHT]),
        ('GRID', (0, 0), (-1, -1), 0.5, LINE_BORDER),
    ]))
    story.append(t_asset)
    story.append(Spacer(1, 8))

    story.append(Paragraph("6. Consultas Analíticas (KPIs) Resueltas por el Modelo", h1_style))

    kpis = [
        ("Horas Pico de Mayor Demanda", "SELECT t.hora, t.jornada_academica, SUM(v.total_linea) AS ingreso_total FROM FACT_VENTAS v JOIN DIM_TIEMPO t ON v.tiempo_id = t.tiempo_id GROUP BY t.hora, t.jornada_academica ORDER BY ingreso_total DESC;"),
        ("Auditoría de Cuadres y Descuadres de Caja", "SELECT c.nombre_cajero, COUNT(DISTINCT c.cierre_caja_id) AS total_turnos, SUM(c.discrepancia_caja) AS descuadre_acumulado FROM DIM_CAJERO_TURNO c GROUP BY c.nombre_cajero;"),
        ("Correlación de Producto vs Calidad Percibida (CSAT)", "SELECT p.nombre_producto, ROUND(AVG(s.rating_estrellas),2) AS rating_prom, COUNT(v.venta_id) AS items_vendidos FROM FACT_VENTAS v JOIN DIM_PRODUCTO p ON v.producto_id = p.producto_id JOIN DIM_SATISFACCION s ON v.satisfaccion_id = s.satisfaccion_id GROUP BY p.nombre_producto HAVING items_vendidos > 5 ORDER BY rating_prom ASC;"),
        ("Tasa de Adopción de Billetera Digital vs Efectivo", "SELECT cp.metodo_pago, COUNT(v.venta_id) AS total_items, SUM(v.total_linea) AS facturado, ROUND(SUM(v.total_linea)*100.0 / (SELECT SUM(total_linea) FROM FACT_VENTAS), 2) AS pct_share FROM FACT_VENTAS v JOIN DIM_CANAL_PAGO cp ON v.canal_pago_id = cp.canal_pago_id GROUP BY cp.metodo_pago;")
    ]

    for kpi_title, kpi_query in kpis:
        story.append(Paragraph(f"• <b>{kpi_title}</b>", h2_style))
        query_p = Paragraph(f"<code>{kpi_query}</code>", ParagraphStyle('CodeStyle', fontName='Courier', fontSize=7.2, leading=9.5, textColor=colors.HexColor("#2C5282")))
        story.append(query_p)
        story.append(Spacer(1, 2))

    story.append(Spacer(1, 6))
    story.append(Paragraph("7. Conclusión", h1_style))
    story.append(Paragraph(
        "La arquitectura dimensional propuesta transforma los registros relacionales de MySQL en un repositorio óptimo para "
        "la toma de decisiones estratégicas. Al desacoplar la analítica del motor OLTP, se garantiza alta disponibilidad en la app móvil "
        "y terminales de caja POS, potenciando la reportería gerencial en tiempo real.",
        body_style
    ))

    doc.build(story, canvasmaker=NumberedCanvas)
    print(f"PDF generado exitosamente en: {filename}")

if __name__ == "__main__":
    out_ws = r"c:\Users\USUARIO\Desktop\Proyecto de aula\backend_proyecto _aula\modelo_estrella_yireh_store.pdf"
    out_desk = r"c:\Users\USUARIO\Desktop\Proyecto de aula\modelo_estrella_yireh_store.pdf"
    build_pdf(out_ws)
    try:
        shutil.copy(out_ws, out_desk)
        print("Copiado al directorio principal de Proyecto de aula")
    except Exception as e:
        print(f"Nota: {e}")
