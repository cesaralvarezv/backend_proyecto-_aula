import { Component, OnInit, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService } from '../services/chat.service';
import { ChatMessage, ChatStatus } from '../models/chat.model';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit, AfterViewChecked {
  @ViewChild('scrollContainer') private scrollContainer!: ElementRef;

  messages: ChatMessage[] = [];
  userInput: string = '';
  isLoading: boolean = false;
  status: ChatStatus | null = null;

  quickPrompts: string[] = [
    '¿Qué variedades de café tienen hoy?',
    '¿Cuáles son los productos y sus precios?',
    '¿Qué métodos de pago aceptan?',
    '¿Tienen opciones de repostería?'
  ];

  constructor(private chatService: ChatService) {}

  ngOnInit(): void {
    this.checkStatus();
    this.loadHistory();
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  checkStatus(): void {
    this.chatService.getStatus().subscribe({
      next: (res) => this.status = res,
      error: () => this.status = null
    });
  }

  loadHistory(): void {
    this.chatService.getHistory().subscribe({
      next: (history) => {
        if (history && history.length > 0) {
          this.messages = history;
        } else {
          this.messages = [{
            role: 'assistant',
            content: '¡Hola! Bienvenido a Yireh Store. Soy YirehBot, tu asistente de ventas y asesor de cafetería. ¿En qué puedo ayudarte hoy?',
            timestamp: new Date().toISOString()
          }];
        }
      },
      error: () => {
        this.messages = [{
          role: 'assistant',
          content: '¡Hola! Bienvenido a Yireh Store. ¿En qué te podemos colaborar hoy?',
          timestamp: new Date().toISOString()
        }];
      }
    });
  }

  sendMessage(text?: string): void {
    const messageToSend = text || this.userInput.trim();
    if (!messageToSend || this.isLoading) return;

    // Agregar mensaje del usuario a la vista
    this.messages.push({
      role: 'user',
      content: messageToSend,
      timestamp: new Date().toISOString()
    });

    this.userInput = '';
    this.isLoading = true;

    this.chatService.sendMessage(messageToSend).subscribe({
      next: (res) => {
        this.messages.push({
          role: 'assistant',
          content: res.content,
          timestamp: res.timestamp
        });
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al consultar al chatbot:', err);
        this.messages.push({
          role: 'assistant',
          content: '⚠️ Ocurrió un error al contactar al asistente. Asegúrate de que el servidor Spring Boot y Ollama estén activos.',
          timestamp: new Date().toISOString()
        });
        this.isLoading = false;
      }
    });
  }

  clearSession(): void {
    if (!confirm('¿Deseas reiniciar la conversación con YirehBot?')) return;
    this.chatService.clearSession().subscribe({
      next: () => {
        this.messages = [{
          role: 'assistant',
          content: '¡Conversación reiniciada! ¿En qué te puedo asesorar hoy?',
          timestamp: new Date().toISOString()
        }];
      }
    });
  }

  private scrollToBottom(): void {
    try {
      if (this.scrollContainer) {
        this.scrollContainer.nativeElement.scrollTop = this.scrollContainer.nativeElement.scrollHeight;
      }
    } catch (err) {}
  }
}
