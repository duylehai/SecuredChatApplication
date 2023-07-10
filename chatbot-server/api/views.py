from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from api.serializers import UserSerializer
from api.crypto_helper import *
from api.models import DUMMY, User

class Register(APIView):
    def post(self, request, *args, **kwargs):
        key = generate_aes_key(request.data["password"])
        encrypted_dummy = aes_encrypt(key, DUMMY.encode())
        private_key, public_key = generate_rsa_key_pair()
        encrypted_private_key = aes_encrypt(key, private_key)
        
        user = {
            "username": request.data["username"],
            "encrypted_dummy": encrypted_dummy,
            "public_key": public_key,
            "encrypted_private_key": encrypted_private_key
        }

        serializer = UserSerializer(data=user)
        if serializer.is_valid():
            User.objects.create(**user)
            obj = User.objects.get(username=request.data["username"])
            print(obj.encrypted_dummy)
            return Response(status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class Login(APIView):
    def post(self, request, *args, **kwargs):
        user = User.objects.get(username=request.data["username"])
        key = generate_aes_key(request.data["password"])
        decrypted_dummy = aes_decrypt(key, user.encrypted_dummy)
        if decrypted_dummy.decode() == DUMMY:
            resp = {
                "username": user.username,
                "private_key": aes_decrypt(key, user.encrypted_private_key).decode(),
            }
            return Response(resp, status=status.HTTP_200_OK)
        else:
            return Response(status=status.HTTP_401_UNAUTHORIZED)
        
class GetPublicKey(APIView):
    def get(self, request, *args, **kwargs):
        username = kwargs["username"]
        if not username:
            return Response(status=status.HTTP_400_BAD_REQUEST)

        if not User.objects.filter(username=username).exists():
            return Response(status=status.HTTP_404_NOT_FOUND)
        
        user = User.objects.get(username=username)
        resp = {
            "public_key": user.public_key
        }
        return Response(resp, status=status.HTTP_200_OK)