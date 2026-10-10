class ChatMessageModel {
  final String role; // 'user' o 'assistant'
  final String content;
  final DateTime timestamp;

  ChatMessageModel({
    required this.role,
    required this.content,
    required this.timestamp,
  });

  factory ChatMessageModel.fromJson(Map<String, dynamic> json) {
    return ChatMessageModel(
      role: json['role'] ?? 'assistant',
      content: json['content'] ?? '',
      timestamp: json['timestamp'] != null
          ? DateTime.tryParse(json['timestamp']) ?? DateTime.now()
          : DateTime.now(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'role': role,
      'content': content,
      'timestamp': timestamp.toIso8601String(),
    };
  }

  bool get isUser => role == 'user';
}

class ChatStatusModel {
  final bool llmAvailable;
  final String model;
  final int activeSessions;

  ChatStatusModel({
    required this.llmAvailable,
    required this.model,
    required this.activeSessions,
  });

  factory ChatStatusModel.fromJson(Map<String, dynamic> json) {
    return ChatStatusModel(
      llmAvailable: json['llmAvailable'] ?? false,
      model: json['model'] ?? 'Desconocido',
      activeSessions: json['activeSessions'] ?? 0,
    );
  }
}
