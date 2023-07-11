from django.db import models
from api.models import User

# Create your models here.
class Message(models.Model):
    sender = models.ForeignKey(User, on_delete=models.CASCADE, to_field="username", related_name="sender")
    recipient = models.ForeignKey(User, on_delete=models.CASCADE, to_field="username", related_name="recipient")
    encrypted_message = models.CharField(max_length=1000)
    encrypted_aes_key = models.CharField(max_length=256)
    timestamp = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.recipient} - {self.encrypted_message}"