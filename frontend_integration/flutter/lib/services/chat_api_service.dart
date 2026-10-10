import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/chat_message_model.dart';

class ChatApiService {
  // Nota: En emulador Android usar 'http://10.0.2.2:8080/api/chat'
  // En Web o Windows usar 'http://localhost:8080/api/chat'
  static const String defaultBaseUrl = 'http://10.0.2.2:8080/api/chat';

  final String baseUrl;
  String? _sessionId;

  ChatApiService({String? customBaseUrl})
      : baseUrl = customBaseUrl ?? defaultBaseUrl {
    _sessionId = 'flutter_session_${DateTime.now().millisecondsSinceEpoch}';
  }

  String get sessionId => _sessionId!;

  /// Envía un mensaje al backend de Yireh Store
  Future<ChatMessageModel> sendMessage(String text) async {
    final uri = Uri.parse(baseUrl);
    final response = await http.post(
      uri,
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'sessionId': _sessionId,
        'message': text.trim(),
      }),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(utf8.decode(response.bodyBytes));
      return ChatMessageModel.fromJson(data);
    } else {
      throw Exception('Error del servidor: ${response.statusCode}');
    }
  }

  /// Consulta el historial de conversación
  Future<List<ChatMessageModel>> fetchHistory() async {
    final uri = Uri.parse('$baseUrl/history/$_sessionId');
    final response = await http.get(uri);

    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(utf8.decode(response.bodyBytes));
      return data.map((item) => ChatMessageModel.fromJson(item)).toList();
    } else {
      return [];
    }
  }

  /// Limpia la sesión actual y genera un nuevo identificador
  Future<void> clearSession() async {
    if (_sessionId != null) {
      final uri = Uri.parse('$baseUrl/session/$_sessionId');
      try {
        await http.delete(uri);
      } catch (_) {}
    }
    _sessionId = 'flutter_session_${DateTime.now().millisecondsSinceEpoch}';
  }

  /// Consulta si Ollama está online
  Future<ChatStatusModel?> checkStatus() async {
    final uri = Uri.parse('$baseUrl/status');
    try {
      final response = await http.get(uri);
      if (response.statusCode == 200) {
        final data = jsonDecode(utf8.decode(response.bodyBytes));
        return ChatStatusModel.fromJson(data);
      }
    } catch (_) {}
    return null;
  }
}
