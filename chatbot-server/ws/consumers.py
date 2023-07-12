import json
from channels.generic.websocket import WebsocketConsumer
from api.models import User
from asgiref.sync import async_to_sync
from ws.socket_authentication import socket_user
from ws.models import Message
import urllib.parse


class ChatConsumer(WebsocketConsumer):
    def connect(self):
        params = urllib.parse.parse_qs(self.scope['query_string'])
        
        if not params[b'username'] or not params[b'socket_code'] :
            self.close()

        self.user = params[b'username'][0].decode()
        if not User.objects.filter(username=self.user).exists():
            self.close()
        
        if str(self.user) not in socket_user or str(params[b'socket_code'][0].decode()) != socket_user.get(str(self.user)):
            self.close()
            
        socket_user.pop(str(self.user)) 

        self.accept()
        self.group_name = f'chat_{self.user}'
        async_to_sync(self.channel_layer.group_add)(
            self.group_name,
            self.channel_name
        )

        messages = Message.objects.filter(recipient=self.user)
        for message in messages:
            self.send_message(message.sender, message.encrypted_message, message.encrypted_aes_key, message.recipient)
            message.delete()

    def disconnect(self, close_code):
        if close_code != 1000:
            return
        
        async_to_sync(self.channel_layer.group_discard)(
            self.group_name,
            self.channel_name
        )

    def receive(self, text_data):
        json_data = json.loads(text_data)
        sender = self.user
        encrypted_message = json_data['encrypted_message']
        encrypted_aes_key = json_data['encrypted_aes_key']
        recipient = json_data['recipient']
        self.send_message(sender, encrypted_message, encrypted_aes_key, recipient)

    def send_message(self, sender, encrypted_message, encrypted_aes_key, recipient):
        recipient = str(recipient)
        sender = str(sender)

        if not User.objects.filter(username=recipient).exists():
            return
        
        if not f'chat_{recipient}' in self.channel_layer.groups:
            try:
                sender = User.objects.get(username=sender)
                recipient = User.objects.get(username=recipient)
            except:
                return
            
            message = Message(sender=sender, recipient=recipient, encrypted_message=encrypted_message, encrypted_aes_key=encrypted_aes_key)
            message.save()
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

