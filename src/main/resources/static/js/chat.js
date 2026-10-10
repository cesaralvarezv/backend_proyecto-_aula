// Yireh Store - Chatbot Client JS
document.addEventListener('DOMContentLoaded', () => {
    const chatMessagesEl = document.getElementById('chatMessages');
    const messageInput = document.getElementById('messageInput');
    const chatForm = document.getElementById('chatForm');
    const sendBtn = document.getElementById('sendBtn');
    const clearBtn = document.getElementById('clearBtn');
    const statusText = document.getElementById('statusText');
    const statusDot = document.getElementById('statusDot');
    const quickChips = document.querySelectorAll('.chip-btn');

    // 1. Gestión de Sesión
    let sessionId = localStorage.getItem('yireh_store_chat_session');
    if (!sessionId) {
        sessionId = 'session_' + Math.random().toString(36).substring(2, 11) + '_' + Date.now();
        localStorage.setItem('yireh_store_chat_session', sessionId);
    }

    // 2. Verificar estado de Ollama en el backend
    checkSystemStatus();
    // 3. Cargar historial existente
    loadConversationHistory();

    // Event Listeners
    chatForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const text = messageInput.value.trim();
        if (text) {
            handleSendMessage(text);
        }
    });

    if (clearBtn) {
        clearBtn.addEventListener('click', handleClearSession);
    }

    quickChips.forEach(chip => {
        chip.addEventListener('click', () => {
            const promptText = chip.getAttribute('data-prompt') || chip.innerText.trim();
            messageInput.value = promptText;
            handleSendMessage(promptText);
        });
    });

    // Funciones Principales
    async function checkSystemStatus() {
        try {
            const res = await fetch('/api/chat/status');
            if (res.ok) {
                const data = await res.json();
                if (data.llmAvailable) {
                    statusText.textContent = `Ollama Online (${data.model})`;
                    statusDot.classList.remove('disconnected');
                } else {
                    statusText.textContent = `Ollama no detectado (${data.model})`;
                    statusDot.classList.add('disconnected');
                }
            } else {
                statusText.textContent = 'Backend Conectado';
            }
        } catch (err) {
            console.warn('No se pudo verificar el estado del chatbot:', err);
            statusText.textContent = 'Modo Local';
        }
    }

    async function loadConversationHistory() {
        try {
            const res = await fetch(`/api/chat/history/${encodeURIComponent(sessionId)}`);
            if (res.ok) {
                const history = await res.json();
                if (history && history.length > 0) {
                    chatMessagesEl.innerHTML = '';
                    history.forEach(msg => {
                        appendMessageBubble(msg.role, msg.content, msg.timestamp);
                    });
                    scrollToBottom();
                } else {
                    // Mensaje de bienvenida inicial
                    renderWelcomeMessage();
                }
            } else {
                renderWelcomeMessage();
            }
        } catch (err) {
            console.error('Error al cargar historial:', err);
            renderWelcomeMessage();
        }
    }

    function renderWelcomeMessage() {
        chatMessagesEl.innerHTML = '';
        appendMessageBubble(
            'assistant',
            '¡Hola! 👋 Bienvenido a **Yireh Store**. Soy tu asistente virtual inteligente. ¿En qué te puedo asesorar hoy? Puedes preguntarme sobre nuestro menú de café, repostería, precios, promociones o métodos de pago.'
        );
    }

    async function handleSendMessage(text) {
        // Renderizar mensaje del usuario inmediatamente
        appendMessageBubble('user', text, new Date().toISOString());
        messageInput.value = '';
        setInputDisabled(true);
        scrollToBottom();

        // Mostrar indicador de "escribiendo..."
        const typingEl = showTypingIndicator();
        scrollToBottom();

        try {
            const response = await fetch('/api/chat', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    sessionId: sessionId,
                    message: text
                })
            });

            removeTypingIndicator(typingEl);

            if (response.ok) {
                const data = await response.json();
                appendMessageBubble(data.role || 'assistant', data.content, data.timestamp);
            } else {
                appendMessageBubble(
                    'assistant',
                    '⚠️ Ocurrió un error al contactar al servidor. Por favor verifica que el backend y Ollama estén ejecutándose.'
                );
            }
        } catch (error) {
            removeTypingIndicator(typingEl);
            console.error('Error al enviar mensaje:', error);
            appendMessageBubble(
                'assistant',
                '⚠️ No fue posible conectar con el asistente. Asegúrate de que el backend esté en ejecución.'
            );
        } finally {
            setInputDisabled(false);
            messageInput.focus();
            scrollToBottom();
        }
    }

    async function handleClearSession() {
        if (!confirm('¿Deseas reiniciar la conversación con el asistente?')) return;

        try {
            await fetch(`/api/chat/session/${encodeURIComponent(sessionId)}`, {
                method: 'DELETE'
            });
        } catch (e) {
            console.warn('Error al limpiar sesión en backend:', e);
        }

        // Generar nuevo sessionId
        sessionId = 'session_' + Math.random().toString(36).substring(2, 11) + '_' + Date.now();
        localStorage.setItem('yireh_store_chat_session', sessionId);
        renderWelcomeMessage();
    }

    // Renderizado de Burbujas
    function appendMessageBubble(role, rawContent, timestamp) {
        const isUser = role === 'user';
        const row = document.createElement('div');
        row.className = `message-row ${isUser ? 'user' : 'bot'}`;

        const avatar = document.createElement('div');
        avatar.className = 'message-avatar-mini';
        avatar.textContent = isUser ? '👤' : '🤖';

        const bubble = document.createElement('div');
        bubble.className = 'message-bubble';
        bubble.innerHTML = formatMarkdown(rawContent);

        const time = document.createElement('div');
        time.className = 'message-time';
        time.textContent = formatTime(timestamp);
        bubble.appendChild(time);

        row.appendChild(avatar);
        row.appendChild(bubble);

        chatMessagesEl.appendChild(row);
    }

    function showTypingIndicator() {
        const row = document.createElement('div');
        row.className = 'message-row bot typing-wrapper';

        const avatar = document.createElement('div');
        avatar.className = 'message-avatar-mini';
        avatar.textContent = '🤖';

        const indicator = document.createElement('div');
        indicator.className = 'typing-indicator';
        indicator.innerHTML = '<span class="typing-dot"></span><span class="typing-dot"></span><span class="typing-dot"></span>';

        row.appendChild(avatar);
        row.appendChild(indicator);

        chatMessagesEl.appendChild(row);
        return row;
    }

    function removeTypingIndicator(el) {
        if (el && el.parentNode) {
            el.parentNode.removeChild(el);
        }
    }

    function setInputDisabled(disabled) {
        messageInput.disabled = disabled;
        sendBtn.disabled = disabled;
    }

    function scrollToBottom() {
        setTimeout(() => {
            chatMessagesEl.scrollTop = chatMessagesEl.scrollHeight;
        }, 50);
    }

    function formatTime(isoString) {
        if (!isoString) return new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        try {
            const date = new Date(isoString);
            return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        } catch {
            return '';
        }
    }

    // Formateador simple de Markdown seguro para texto enriquecido
    function formatMarkdown(text) {
        if (!text) return '';
        let escaped = text
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;');

        // Negrita **texto**
        escaped = escaped.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');

        // Líneas que empiezan con - o * convertidas a viñetas
        const lines = escaped.split('\n');
        let inList = false;
        let htmlLines = [];

        for (let line of lines) {
            const trimmed = line.trim();
            if (trimmed.startsWith('- ') || trimmed.startsWith('* ')) {
                if (!inList) {
                    htmlLines.push('<ul>');
                    inList = true;
                }
                htmlLines.push(`<li>${trimmed.substring(2)}</li>`);
            } else {
                if (inList) {
                    htmlLines.push('</ul>');
                    inList = false;
                }
                htmlLines.push(line);
            }
        }
        if (inList) htmlLines.push('</ul>');

        return htmlLines.join('<br>').replace(/<br><\/ul>/g, '</ul>').replace(/<ul><br>/g, '<ul>');
    }
});
