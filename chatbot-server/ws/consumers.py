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
        message = json_data['message']
        sender = self.user
        recipient = json_data['recipient']
        self.send_message(message, sender, recipient)

    def send_message(self, message, sender, recipient):
        if not User.objects.filter(username=recipient).exists():
            return
        
        group_name = f'chat_{recipient}'
        async_to_sync(self.channel_layer.group_send)(
            group_name,
            {
                'type': 'chat_message',
                'message': message,
                'sender': sender,
            }
        )

    def chat_message(self, event):
        message = event['message']
        sender = event['sender']
        self.send(text_data=json.dumps({
            'message': message,
            'sender': sender,
        }))

