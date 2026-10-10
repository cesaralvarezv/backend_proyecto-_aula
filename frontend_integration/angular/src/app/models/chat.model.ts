export interface ChatMessage {
  sessionId?: string;
  role: 'user' | 'assistant' | 'system';
  content: string;
  timestamp?: string;
}

export interface ChatRequest {
  sessionId?: string;
  message: string;
}

export interface ChatResponse {
  sessionId: string;
  role: string;
  content: string;
  timestamp: string;
}

export interface ChatStatus {
  llmAvailable: boolean;
  model: string;
  activeSessions: number;
  timestamp: string;
}
