import os
from reportlab.lib.pagesizes import letter
from reportlab.lib import colors
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, PageBreak, KeepTogether, HRFlowable, Preformatted, Image
)
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.pdfgen import canvas

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
        self.setFillColor(colors.HexColor("#64748B"))
        
        # Header (páginas > 1)
        if self._pageNumber > 1:
            self.drawString(44, 752, "Yireh Store — Informe de Integración Chatbot con Ollama LLM")
            self.drawRightString(568, 752, "Desarrollo Web Avanzado")
            self.setStrokeColor(colors.HexColor("#CBD5E1"))
            self.setLineWidth(0.5)
            self.line(44, 746, 568, 746)
        
        # Footer en todas las páginas
        footer_text = f"Página {self._pageNumber} de {page_count}"
        self.drawRightString(568, 26, footer_text)
        self.drawString(44, 26, "Documento Técnico Oficial — Arquitectura Hexagonal, Angular/Flutter & Ollama")
        self.setStrokeColor(colors.HexColor("#CBD5E1"))
        self.setLineWidth(0.5)
        self.line(44, 36, 568, 36)
        self.restoreState()

def build_pdf(filename="integracion_chatbot_yireh_store.pdf"):
    doc = SimpleDocTemplate(
        filename,
        pagesize=letter,
        leftMargin=44,
        rightMargin=44,
        topMargin=46,
        bottomMargin=46
    )

    styles = getSampleStyleSheet()
    
    PRIMARY = colors.HexColor("#0F172A")    # Navy oscuro
    SECONDARY = colors.HexColor("#0284C7")  # Azul vibrante
    ACCENT = colors.HexColor("#4F46E5")     # Indigo
    DARK_TEXT = colors.HexColor("#1E293B")  # Slate oscuro
    LIGHT_BG = colors.HexColor("#F8FAFC")
    BORDER_COLOR = colors.HexColor("#CBD5E1")
    
    title_style = ParagraphStyle(
        'DocTitle',
        parent=styles['Heading1'],
        fontName='Helvetica-Bold',
        fontSize=18,
        leading=22,
        textColor=PRIMARY,
        spaceAfter=3
    )
    
    subtitle_style = ParagraphStyle(
        'DocSub',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=10,
        leading=13.5,
        textColor=SECONDARY,
        spaceAfter=10
    )
    
    h1_style = ParagraphStyle(
        'Heading1_Custom',
        parent=styles['Heading1'],
        fontName='Helvetica-Bold',
        fontSize=12,
        leading=15,
        textColor=PRIMARY,
        spaceBefore=10,
        spaceAfter=5,
        keepWithNext=True
    )

    h2_style = ParagraphStyle(
        'Heading2_Custom',
        parent=styles['Heading2'],
        fontName='Helvetica-Bold',
        fontSize=10,
        leading=13,
        textColor=ACCENT,
        spaceBefore=7,
        spaceAfter=3,
        keepWithNext=True
    )

    body_style = ParagraphStyle(
        'Body_Custom',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=8.5,
        leading=11.8,
        textColor=DARK_TEXT,
        spaceAfter=4
    )

    code_style = ParagraphStyle(
        'CodeStyle',
        parent=styles['Normal'],
        fontName='Courier',
        fontSize=7,
        leading=9.2,
        textColor=colors.HexColor("#0F172A"),
        spaceAfter=4
    )

    th_style = ParagraphStyle(
        'TableHeader',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=8,
        leading=10,
        textColor=colors.white,
        alignment=1
    )

    td_style = ParagraphStyle(
        'TableCell',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=7.6,
        leading=9.8,
        textColor=DARK_TEXT
    )

    td_code = ParagraphStyle(
        'TableCellCode',
        parent=styles['Normal'],
        fontName='Courier-Bold',
        fontSize=7.3,
        leading=9.2,
        textColor=colors.HexColor("#1E3A8A")
    )

    story = []

    # ==================== PORTADA / ENCABEZADO ====================
    story.append(Paragraph("INFORME TÉCNICO DE INTEGRACIÓN: CHATBOT / AGENTE INTELIGENTE", title_style))
    story.append(Paragraph("Integración de Ollama LLM en Spring Boot 3 (Hexagonal) y Front-End en Angular / Flutter — <b>Yireh Store</b>", subtitle_style))
    story.append(HRFlowable(width="100%", thickness=1.5, color=ACCENT, spaceAfter=8))

    # Meta Info Card
    meta_data = [
        [
            Paragraph("<b>Materia:</b> Desarrollo Web Avanzado", td_style),
            Paragraph("<b>Proyecto:</b> Yireh Store (Cafetería & E-Commerce)", td_style)
        ],
        [
            Paragraph("<b>Motor LLM:</b> Ollama (qwen2.5-coder:7b local)", td_style),
            Paragraph("<b>Front-End:</b> Angular (Web SPA) / Flutter (Móvil Multiplataforma)", td_style)
        ],
        [
            Paragraph("<b>Backend:</b> Spring Boot 3 + Arquitectura Hexagonal", td_style),
            Paragraph("<b>Túnel Público:</b> Localtunnel (https://nice-tools-nail.loca.lt)", td_style)
        ]
    ]
    t_meta = Table(meta_data, colWidths=[260, 264])
    t_meta.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,-1), colors.HexColor("#F1F5F9")),
        ('BOX', (0,0), (-1,-1), 1, BORDER_COLOR),
        ('INNERGRID', (0,0), (-1,-1), 0.5, BORDER_COLOR),
        ('TOPPADDING', (0,0), (-1,-1), 4),
        ('BOTTOMPADDING', (0,0), (-1,-1), 4),
        ('LEFTPADDING', (0,0), (-1,-1), 8),
        ('RIGHTPADDING', (0,0), (-1,-1), 8),
    ]))
    story.append(t_meta)
    story.append(Spacer(1, 8))

    # ==================== 1. DIAGRAMA DE ARQUITECTURA E IMAGEN ====================
    story.append(Paragraph("1. Diagrama de la Arquitectura de la Aplicación (Spring Boot + Ollama + Frontend)", h1_style))
    story.append(Paragraph(
        "Se adoptó la <b>Arquitectura Hexagonal (Puertos y Adaptadores)</b> para asegurar un desacoplamiento estricto entre el núcleo "
        "de negocio de <b>Yireh Store</b>, el motor de inferencia local <b>Ollama</b> y las aplicaciones cliente (Angular / Flutter).", body_style
    ))

    # Insertar Imagen del Diagrama de Arquitectura
    if os.path.exists("img_diagrama_arquitectura.png"):
        img_arch = Image("img_diagrama_arquitectura.png", width=520, height=312)
        story.append(KeepTogether([img_arch, Spacer(1, 4)]))

    story.append(Paragraph(
        "<b>Flujo de Integración Paso a Paso:</b><br/>"
        "1. <b>Cliente Front-End (Angular / Flutter):</b> El usuario realiza una pregunta sobre el menú de la cafetería o productos.<br/>"
        "2. <b>Controlador Web REST (ChatbotController):</b> Recibe la petición HTTP POST en <code>/api/chat</code> validando el DTO.<br/>"
        "3. <b>Servicio de Dominio (ChatbotService):</b> Consulta <code>ProductRepositoryPort.findByActiveTrue()</code> para extraer "
        "en tiempo real los productos registrados en MySQL, sus precios en COP y stock disponible (mecanismo RAG ligero).<br/>"
        "4. <b>Adaptador de Salida (OllamaAdapter):</b> Se comunica mediante <code>RestClient</code> vía HTTP con el daemon local "
        "de Ollama en <code>http://127.0.0.1:11434/api/chat</code> enviando el historial previo y el prompt contextualizado.<br/>"
        "5. <b>Motor LLM (qwen2.5-coder:7b):</b> Genera una respuesta amigable y certera sobre los productos y la retorna al cliente.", body_style
    ))

    story.append(PageBreak())

    # ==================== 2. PROTOTIPO DE CLASES ====================
    story.append(Paragraph("2. Prototipo de Clases del Chatbot / Agente Inteligente (Backend)", h1_style))
    story.append(Paragraph(
        "La implementación en el backend se organizó en paquetes limpios según su rol de arquitectura hexagonal:", body_style
    ))

    clases_data = [
        [Paragraph("Capa / Paquete", th_style), Paragraph("Clase / Archivo", th_style), Paragraph("Responsabilidad y Rol de Dominio", th_style)],
        [
            Paragraph("<b>domain.model</b>", td_style),
            Paragraph("ChatMessage", td_code),
            Paragraph("Entidad inmutable que representa mensajes (roles: user, assistant, system), texto y timestamp.", td_style)
        ],
        [
            Paragraph("<b>domain.port.in</b>", td_style),
            Paragraph("ChatbotUseCase", td_code),
            Paragraph("Interfaz de entrada: sendMessage(sessionId, msg), getHistory(), clearSession(), getStatus().", td_style)
        ],
        [
            Paragraph("<b>domain.port.out</b>", td_style),
            Paragraph("LlmClientPort", td_code),
            Paragraph("Puerto abstracto que desacopla la lógica de negocio de la tecnología concreta del LLM.", td_style)
        ],
        [
            Paragraph("<b>application.service</b>", td_style),
            Paragraph("ChatbotService", td_code),
            Paragraph("Implementa el caso de uso, recupera productos de BD para alimentar el System Prompt e invoca el puerto LLM.", td_style)
        ],
        [
            Paragraph("<b>infrastructure.adapter.out.ollama</b>", td_style),
            Paragraph("OllamaAdapter", td_code),
            Paragraph("Adaptador con RestClient que consume http://127.0.0.1:11434/api/chat con timeouts y manejo de contingencia.", td_style)
        ],
        [
            Paragraph("<b>infrastructure.adapter.in.web</b>", td_style),
            Paragraph("ChatbotController", td_code),
            Paragraph("Controlador REST que expone los endpoints HTTP (/api/chat, /history, /status) para Angular y Flutter.", td_style)
        ],
        [
            Paragraph("<b>infrastructure.adapter.in.web.dto</b>", td_style),
            Paragraph("ChatRequestDto / ChatResponseDto", td_code),
            Paragraph("Records para transporte de datos con validación @NotBlank de Jakarta.", td_style)
        ]
    ]

    t_clases = Table(clases_data, colWidths=[120, 140, 264])
    t_clases.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), PRIMARY),
        ('BOX', (0,0), (-1,-1), 1, BORDER_COLOR),
        ('INNERGRID', (0,0), (-1,-1), 0.5, BORDER_COLOR),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [colors.white, LIGHT_BG]),
        ('TOPPADDING', (0,0), (-1,-1), 3.5),
        ('BOTTOMPADDING', (0,0), (-1,-1), 3.5),
        ('LEFTPADDING', (0,0), (-1,-1), 6),
        ('RIGHTPADDING', (0,0), (-1,-1), 6),
    ]))
    story.append(t_clases)
    story.append(Spacer(1, 8))

    # ==================== 3. CORRECCIONES TÉCNICAS CLAVE ====================
    story.append(Paragraph("3. Correcciones y Optimizaciones Técnicas Realizadas", h1_style))
    story.append(Paragraph(
        "Durante la fase de integración y pruebas se diagnosticaron y solucionaron aspectos críticos de infraestructura:", body_style
    ))

    story.append(Paragraph(
        "• <b>Resolución IPv4 vs IPv6 en Windows 11:</b> Por defecto, <code>localhost</code> resolvía a <code>[::1]</code> (IPv6), "
        "mientras que el proceso de Ollama escuchaba únicamente en IPv4 (<code>127.0.0.1:11434</code>), provocando caídas por "
        "<i>Connection Refused</i>. Se fijó explícitamente la dirección IPv4 <code>http://127.0.0.1:11434</code> en "
        "<code>OllamaAdapter.java</code>, <code>application.properties</code> y <code>.env</code>.<br/>"
        "• <b>Ampliación de Timeouts a 120 Segundos:</b> La inferencia de modelos grandes (como <code>qwen2.5-coder:7b</code> de 4.7 GB) "
        "requiere tiempo de inicialización en memoria y generación de tokens; se configuró un <code>readTimeout</code> de 120s en "
        "<code>SimpleClientHttpRequestFactory</code> para evitar cortes por timeout.<br/>"
        "• <b>Seguridad Pública para el Chatbot:</b> Se actualizó <code>SecurityConfig.java</code> para otorgar <code>permitAll()</code> "
        "a <code>/api/chat/**</code>, permitiendo a clientes externos y usuarios anónimos consultar al bot sin requerir token JWT previo.<br/>"
        "• <b>Túnel de Acceso Remoto:</b> Se configuró <b>Localtunnel</b> vinculando el puerto 8080 hacia la URL segura "
        "<code>https://nice-tools-nail.loca.lt</code> para pruebas externas y evaluación remota.", body_style
    ))

    story.append(PageBreak())

    # ==================== 4. EVIDENCIA DE PRUEBAS REALES ====================
    story.append(Paragraph("4. Evidencia Gráfica de Pruebas Reales y Funcionamiento", h1_style))
    story.append(Paragraph(
        "A continuación se muestran las capturas de ejecución real sobre el backend activo en el puerto 8080, "
        "comprobando tanto el endpoint de diagnóstico como la inferencia contextual con productos de la cafetería:", body_style
    ))

    # Insertar Imagen de Evidencia de Pruebas
    if os.path.exists("img_evidencia_pruebas.png"):
        img_evid = Image("img_evidencia_pruebas.png", width=520, height=260)
        story.append(KeepTogether([img_evid, Spacer(1, 6)]))

    story.append(Paragraph(
        "<b>Análisis de la Respuesta de IA Generativa:</b><br/>"
        "Al consultar <i>'¿Qué café tienen y qué precio tienen?'</i>, el agente inteligente no inventó datos genéricos; "
        "extrajo directamente de la tabla <code>products</code> de MySQL los productos activos de la categoría cafetería: "
        "<b>Café Expresso ($2500 COP)</b>, <b>Café Americano ($2800 COP)</b>, <b>Café Capuchino ($3200 COP)</b>, etc., "
        "demostrando la efectividad de la inyección de contexto en el System Prompt.", body_style
    ))
    story.append(Spacer(1, 8))

    # ==================== 5. INTEGRACIÓN FRONT-END (ANGULAR & FLUTTER) ====================
    story.append(Paragraph("5. Integración en el Front-End (Angular & Flutter)", h1_style))
    story.append(Paragraph(
        "El componente de chat se diseñó para ser integrado de forma nativa en <b>Angular</b> o en <b>Flutter</b> "
        "(archivos disponibles en la carpeta <code>frontend_integration/</code> del proyecto):", body_style
    ))

    fe_data = [
        [Paragraph("Plataforma", th_style), Paragraph("Archivo / Artefacto", th_style), Paragraph("Tecnología / Rol", th_style), Paragraph("Responsabilidad en la UI", th_style)],
        [
            Paragraph("<b>Angular</b>", td_style),
            Paragraph("chat.service.ts", td_code),
            Paragraph("HttpClient / RxJS", td_style),
            Paragraph("Maneja llamadas a /api/chat, /history y almacenamiento de sesión en localStorage.", td_style)
        ],
        [
            Paragraph("<b>Angular</b>", td_style),
            Paragraph("chat.component.ts/.html/.scss", td_code),
            Paragraph("Standalone Component", td_style),
            Paragraph("Vista con diseño dark/glassmorphism, chips rápidos, auto-scroll y animación typing.", td_style)
        ],
        [
            Paragraph("<b>Flutter</b>", td_style),
            Paragraph("chat_api_service.dart", td_code),
            Paragraph("Dart http package", td_style),
            Paragraph("Cliente HTTP con soporte automático de emulador Android (10.0.2.2) y dispositivos físicos.", td_style)
        ],
        [
            Paragraph("<b>Flutter</b>", td_style),
            Paragraph("chat_screen.dart", td_code),
            Paragraph("StatefulWidget (Material 3)", td_style),
            Paragraph("Pantalla móvil con ListView reactivo, pill de estado Ollama en vivo y burbujas de diálogo.", td_style)
        ]
    ]

    t_fe = Table(fe_data, colWidths=[65, 150, 105, 204])
    t_fe.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), PRIMARY),
        ('BOX', (0,0), (-1,-1), 1, BORDER_COLOR),
        ('INNERGRID', (0,0), (-1,-1), 0.5, BORDER_COLOR),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [colors.white, LIGHT_BG]),
        ('TOPPADDING', (0,0), (-1,-1), 3.5),
        ('BOTTOMPADDING', (0,0), (-1,-1), 3.5),
        ('LEFTPADDING', (0,0), (-1,-1), 6),
        ('RIGHTPADDING', (0,0), (-1,-1), 6),
    ]))
    story.append(t_fe)
    story.append(Spacer(1, 8))

    # ==================== 6. MODELO DE DATOS & SEGURIDAD ====================
    story.append(Paragraph("6. Diagrama del Modelo de Datos (E-R) y Diccionario de Datos", h1_style))

    er_diag = (
        "+-------------------+        +--------------------+        +----------------------+\n"
        "|       USERS       | 1    N |       ORDERS       | 1    N |     ORDER_ITEMS      |\n"
        "|-------------------|------->|--------------------|------->|----------------------|\n"
        "| PK id             |        | PK id              |        | PK id                |\n"
        "|    email, password|        | FK user_id         |        | FK order_id          |\n"
        "|    name, role     |        |    total, status   |        | FK product_id        |\n"
        "+---------+---------+        +---------+----------+        |    quantity, price   |\n"
        "          | 1                          | 1                 +----------------------+\n"
        "          |                            |                              ^\n"
        "          v 1                          v 1                            | N\n"
        "+---------+---------+        +---------+----------+        +----------+-----------+\n"
        "|      WALLETS      |        |      INVOICES      |        |       PRODUCTS       |\n"
        "|-------------------|        |--------------------|        |----------------------|\n"
        "| PK id             |        | PK id              |        | PK id                |\n"
        "| FK user_id        |        | FK order_id        |        | FK category_id       |\n"
        "|    balance        |        |    invoice_number  |        |    name, price, stock|\n"
        "+-------------------+        |    total, tax      |        +----------------------+\n"
        "                             +--------------------+                   ^ N\n"
        "                                                                      | 1\n"
        "                                                           +----------+-----------+\n"
        "                                                           |      CATEGORIES      |\n"
        "                                                           |----------------------|\n"
        "                                                           | PK id, name, location|\n"
        "                                                           +----------------------+"
    )
    story.append(Preformatted(er_diag, code_style))
    story.append(Spacer(1, 6))

    dicc_data = [
        [Paragraph("Entidad", th_style), Paragraph("Campos Clave", th_style), Paragraph("Tipo / Restricción", th_style), Paragraph("Descripción Funcional", th_style)],
        [
            Paragraph("users", td_code),
            Paragraph("id, email, password, role", td_style),
            Paragraph("PK: id, UQ: email", td_style),
            Paragraph("Usuarios del sistema con roles USER, ADMIN, CASHIER.", td_style)
        ],
        [
            Paragraph("categories", td_code),
            Paragraph("id, name, location, active", td_style),
            Paragraph("PK: id", td_style),
            Paragraph("Familias de productos (Cafés de Origen, Repostería, Snacks).", td_style)
        ],
        [
            Paragraph("products", td_code),
            Paragraph("id, name, price, quantity, active", td_style),
            Paragraph("PK: id, FK: category_id", td_style),
            Paragraph("Inventario activo que nutre el contexto de IA del Chatbot.", td_style)
        ],
        [
            Paragraph("orders", td_code),
            Paragraph("id, user_id, total, status", td_style),
            Paragraph("PK: id, FK: user_id", td_style),
            Paragraph("Pedidos generados tanto en web como en caja POS.", td_style)
        ],
        [
            Paragraph("wallets", td_code),
            Paragraph("id, user_id, balance", td_style),
            Paragraph("PK: id, FK: user_id", td_style),
            Paragraph("Billetera virtual con saldo prepagado para compras en cafetería.", td_style)
        ]
    ]

    t_dicc = Table(dicc_data, colWidths=[80, 140, 104, 200])
    t_dicc.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), PRIMARY),
        ('BOX', (0,0), (-1,-1), 1, BORDER_COLOR),
        ('INNERGRID', (0,0), (-1,-1), 0.5, BORDER_COLOR),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [colors.white, LIGHT_BG]),
        ('TOPPADDING', (0,0), (-1,-1), 3.5),
        ('BOTTOMPADDING', (0,0), (-1,-1), 3.5),
        ('LEFTPADDING', (0,0), (-1,-1), 6),
        ('RIGHTPADDING', (0,0), (-1,-1), 6),
    ]))
    story.append(t_dicc)

    doc.build(story, canvasmaker=NumberedCanvas)
    print(f"PDF generado con éxito: {filename}")

if __name__ == '__main__':
    build_pdf()
