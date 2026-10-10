import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ChatMessage, ChatRequest, ChatResponse, ChatStatus } from '../models/chat.model';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private readonly baseUrl = 'http://localhost:8080/api/chat';
  private readonly sessionKey = 'yireh_store_chat_session';

  constructor(private http: HttpClient) {}

  /**
   * Obtiene o genera el identificador de sesión persistente en el cliente.
   */
  getSessionId(): string {
    let sessionId = localStorage.getItem(this.sessionKey);
    if (!sessionId) {
      sessionId = 'session_' + Math.random().toString(36).substring(2, 11) + '_' + Date.now();
      localStorage.setItem(this.sessionKey, sessionId);
    }
    return sessionId;
  }

  /**
   * Envía un mensaje al backend de Spring Boot (el cual consulta a Ollama).
   */
  sendMessage(message: string): Observable<ChatResponse> {
    const payload: ChatRequest = {
      sessionId: this.getSessionId(),
      message: message.trim()
    };
    return this.http.post<ChatResponse>(this.baseUrl, payload);
  }

  /**
   * Recupera el historial de mensajes de la sesión actual.
   */
  getHistory(): Observable<ChatMessage[]> {
    const sessionId = this.getSessionId();
    return this.http.get<ChatMessage[]>(`${this.baseUrl}/history/${encodeURIComponent(sessionId)}`);
  }

  /**
   * Reinicia la sesión en el servidor y genera una nueva en el navegador.
   */
  clearSession(): Observable<{ message: string; sessionId: string }> {
    const oldSessionId = this.getSessionId();
    localStorage.removeItem(this.sessionKey);
    // Inicializar nueva sesión de inmediato
    this.getSessionId();
    return this.http.delete<{ message: string; sessionId: string }>(
      `${this.baseUrl}/session/${encodeURIComponent(oldSessionId)}`
    );
  }

  /**
   * Consulta el estado de conectividad con Ollama y el modelo en ejecución.
   */
  getStatus(): Observable<ChatStatus> {
    return this.http.get<ChatStatus>(`${this.baseUrl}/status`);
  }
}
