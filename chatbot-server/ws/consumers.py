import json
from channels.generic.websocket import WebsocketConsumer
from api.models import User
from asgiref.sync import async_to_sync
from ws.socketAuthentication import socketUser
import urllib.parse

class ChatConsumer(WebsocketConsumer):
    def connect(self):
        params = urllib.parse.parse_qs(self.scope['query_string'])
        
        if not params[b'username'] or not params[b'socket_code'] :
            self.close()

        self.user = params[b'username'][0].decode()
        if not User.objects.filter(username=self.user).exists():
            self.close()
        
        if str(self.user) not in socketUser or str(params[b'socket_code'][0].decode()) != socketUser.get(str(self.user)):
            self.close()
            
        socketUser.pop(str(self.user))

        self.accept()
        self.group_name = f'chat_{self.user}'
        async_to_sync(self.channel_layer.group_add)(
            self.group_name,
            self.channel_name
        )

    def disconnect(self, close_code):
        pass

    def receive(self, text_data):
        json_data = json.loads(text_data)
        sender = self.user
        encrypted_message = json_data['encrypted_message']
        encrypted_aes_key = json_data['encrypted_aes_key']
        recipient = json_data['recipient']
        self.send_message(sender, encrypted_message, encrypted_aes_key, recipient)

    def send_message(self, sender, encrypted_message, encrypted_aes_key, recipient):
        if not User.objects.filter(username=recipient).exists():
            return
        
        group_name = f'chat_{recipient}'
        async_to_sync(self.channel_layer.group_send)(
            group_name,
            {
                'type': 'chat_message',
                'encrypted_message': encrypted_message,
                'encrypted_aes_key': encrypted_aes_key,
                'sender': sender,
            }
        )

    def chat_message(self, event):
        encrypted_message = event['encrypted_message']
        encrypted_aes_key = event['encrypted_aes_key']
        sender = event['sender']
        self.send(text_data=json.dumps({
            'encrypted_message': encrypted_message,
            'encrypted_aes_key': encrypted_aes_key,
            'sender': sender,
        }))

