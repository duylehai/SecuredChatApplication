from rest_framework import serializers
from api.models import User

class BinaryFieldSerializer(serializers.ReadOnlyField):
    def to_representation(self, value):
        return value.hex()

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = '__all__'