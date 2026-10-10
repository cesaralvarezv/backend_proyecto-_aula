import 'package:flutter/material.dart';
import '../models/chat_message_model.dart';
import '../services/chat_api_service.dart';

class ChatScreen extends StatefulWidget {
  final String? customApiUrl;

  const ChatScreen({Key? key, this.customApiUrl}) : super(key: key);

  @override
  State<ChatScreen> createState() => _ChatScreenState();
}

class _ChatScreenState extends State<ChatScreen> {
  late final ChatApiService _apiService;
  final TextEditingController _textController = TextEditingController();
  final ScrollController _scrollController = ScrollController();

  final List<ChatMessageModel> _messages = [];
  bool _isLoading = false;
  ChatStatusModel? _status;

  final List<String> _quickPrompts = [
    '¿Qué variedades de café tienen?',
    '¿Precios y productos activos?',
    '¿Qué medios de pago aceptan?',
    '¿Tienen repostería disponible?'
  ];

  @override
  void initState() {
    super.initState();
    _apiService = ChatApiService(customBaseUrl: widget.customApiUrl);
    _loadInitialState();
  }

  void _loadInitialState() async {
    final status = await _apiService.checkStatus();
    setState(() => _status = status);

    _messages.add(
      ChatMessageModel(
        role: 'assistant',
        content: '¡Hola! Bienvenido a Yireh Store. Soy YirehBot, tu asistente de la cafetería. ¿En qué te asesoro hoy?',
        timestamp: DateTime.now(),
      ),
    );
  }

  void _sendMessage([String? quickText]) async {
    final text = quickText ?? _textController.text.trim();
    if (text.isEmpty || _isLoading) return;

    setState(() {
      _messages.add(
        ChatMessageModel(
          role: 'user',
          content: text,
          timestamp: DateTime.now(),
        ),
      );
      _isLoading = true;
    });

    if (quickText == null) {
      _textController.clear();
    }
    _scrollToBottom();

    try {
      final response = await _apiService.sendMessage(text);
      setState(() {
        _messages.add(response);
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _messages.add(
          ChatMessageModel(
            role: 'assistant',
            content: '⚠️ Error de conexión con Yireh Store backend u Ollama.',
            timestamp: DateTime.now(),
          ),
        );
        _isLoading = false;
      });
    }
    _scrollToBottom();
  }

  void _scrollToBottom() {
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (_scrollController.hasClients) {
        _scrollController.animateTo(
          _scrollController.position.maxScrollExtent,
          duration: const Duration(milliseconds: 300),
          curve: Curves.easeOut,
        );
      }
    });
  }

  void _clearChat() async {
    await _apiService.clearSession();
    setState(() {
      _messages.clear();
      _messages.add(
        ChatMessageModel(
          role: 'assistant',
          content: '¡Conversación reiniciada! ¿Qué deseas consultar?',
          timestamp: DateTime.now(),
        ),
      );
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF0F172A),
      appBar: AppBar(
        backgroundColor: const Color(0xFF1E293B),
        elevation: 1,
        title: Row(
          children: [
            const CircleAvatar(
              backgroundColor: Color(0xFF6366F1),
              radius: 18,
              child: Text('🤖', style: TextStyle(fontSize: 18)),
            ),
            const SizedBox(width: 10),
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Text(
                  'YirehBot',
                  style: TextStyle(fontSize: 15, fontWeight: FontWeight.bold, color: Colors.white),
                ),
                Text(
                  _status?.llmAvailable == true
                      ? '● Ollama Online (${_status?.model})'
                      : '● Conectando...',
                  style: TextStyle(
                    fontSize: 11,
                    color: _status?.llmAvailable == true ? Colors.greenAccent : Colors.grey,
                  ),
                ),
              ],
            ),
          ],
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh, color: Colors.white70),
            tooltip: 'Reiniciar conversación',
            onPressed: _clearChat,
          )
        ],
      ),
      body: Column(
        children: [
          // Mensajes
          Expanded(
            child: ListView.builder(
              controller: _scrollController,
              padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 12),
              itemCount: _messages.length,
              itemBuilder: (context, index) {
                final msg = _messages[index];
                return _buildMessageBubble(msg);
              },
            ),
          ),

          // Indicador de tipeo
          if (_isLoading)
            Padding(
              padding: const EdgeInsets.only(left: 16, bottom: 8),
              child: Row(
                children: const [
                  Text('🤖', style: TextStyle(fontSize: 16)),
                  SizedBox(width: 8),
                  Text('YirehBot está respondiendo...', style: TextStyle(color: Colors.white60, fontSize: 12)),
                ],
              ),
            ),

          // Chips rápidos
          Container(
            height: 38,
            margin: const EdgeInsets.symmetric(vertical: 4),
            child: ListView.separated(
              scrollDirection: Axis.horizontal,
              padding: const EdgeInsets.symmetric(horizontal: 12),
              itemCount: _quickPrompts.length,
              separatorBuilder: (_, __) => const SizedBox(width: 8),
              itemBuilder: (context, index) {
                final prompt = _quickPrompts[index];
                return ActionChip(
                  label: Text(prompt, style: const TextStyle(fontSize: 11.5, color: Colors.white)),
                  backgroundColor: const Color(0xFF1E293B),
                  side: const BorderSide(color: Color(0xFF6366F1), width: 0.8),
                  onPressed: () => _sendMessage(prompt),
                );
              },
            ),
          ),

          // Barra de entrada
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
            color: const Color(0xFF1E293B),
            child: SafeArea(
              child: Row(
                children: [
                  Expanded(
                    child: TextField(
                      controller: _textController,
                      style: const TextStyle(color: Colors.white, fontSize: 14),
                      decoration: InputDecoration(
                        hintText: 'Pregunta sobre café, stock o precios...',
                        hintStyle: const TextStyle(color: Colors.white38, fontSize: 13),
                        filled: true,
                        fillColor: const Color(0xFF334155),
                        contentPadding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(24),
                          borderSide: BorderSide.none,
                        ),
                      ),
                      onSubmitted: (_) => _sendMessage(),
                    ),
                  ),
                  const SizedBox(width: 8),
                  CircleAvatar(
                    backgroundColor: const Color(0xFF6366F1),
                    radius: 22,
                    child: IconButton(
                      icon: const Icon(Icons.send, color: Colors.white, size: 18),
                      onPressed: () => _sendMessage(),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildMessageBubble(ChatMessageModel msg) {
    return Align(
      alignment: msg.isUser ? Alignment.centerRight : Alignment.centerLeft,
      child: Container(
        margin: const EdgeInsets.symmetric(vertical: 5),
        padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
        constraints: BoxConstraints(
          maxWidth: MediaQuery.of(context).size.width * 0.78,
        ),
        decoration: BoxDecoration(
          color: msg.isUser ? const Color(0xFF4F46E5) : const Color(0xFF1E293B),
          borderRadius: BorderRadius.only(
            topLeft: const Radius.circular(14),
            topRight: const Radius.circular(14),
            bottomLeft: Radius.circular(msg.isUser ? 14 : 2),
            bottomRight: Radius.circular(msg.isUser ? 2 : 14),
          ),
          border: Border.all(
            color: msg.isUser ? Colors.transparent : const Color(0xFF334155),
            width: 0.8,
          ),
        ),
        child: Text(
          msg.content,
          style: const TextStyle(color: Colors.white, fontSize: 13.5, height: 1.4),
        ),
      ),
    );
  }
}
