interface MessageSinkReceivedMessage {
  msg: string;
  receivedAt: /* ISO8601 */ string;
}
export interface MessageSinkMessageBatch {
  sinkName: string;
  /**
   * @note UUID
   */
  sinkId: string;
  messages: MessageSinkReceivedMessage[];
}

export class MessageSinkApi {
  constructor(private readonly apiHttpOrigin: string, readonly sinkName: string) {}

  getMessage(): Promise<MessageSinkMessageBatch> {
    return fetch(this.sinkEndpoint).then((res) => res.json());
  }

  waitMessage(): Promise<MessageSinkMessageBatch> {
    return fetch(this.sinkEndpoint + '/wait').then((res) => res.json());
  }

  postMessage(msg: string): Promise<MessageSinkMessageBatch> {
    return fetch(this.sinkEndpoint, { method: 'POST', body: msg }).then((res) => res.json());
  }

  createSocketURL(): string {
    return this.sinkEndpoint.replace(/^http/i, 'ws') + '/ws';
  }

  private get sinkEndpoint(): string {
    return `${this.apiHttpOrigin}/message-sink/${encodeURIComponent(this.sinkName)}`;
  }
}
