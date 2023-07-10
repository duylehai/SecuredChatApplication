from django.db import models

DUMMY = "dummy dummy"

# Create your models here.
class User(models.Model):
    user_id = models.AutoField(primary_key=True)
    username = models.CharField(max_length=255, blank=False, unique=True)
    encrypted_dummy = models.BinaryField(max_length=255, blank=False)
    public_key = models.BinaryField(max_length=255, blank=False)
    encrypted_private_key = models.BinaryField(max_length=255, blank=False)

    def __str__(self):
        return self.username