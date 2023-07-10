from django.urls import path, include
from .views import (
    Register, Login, GetPublicKey
)

urlpatterns = [
    path("register", Register.as_view()),
    path("login", Login.as_view()),
    path("public-key/<str:username>", GetPublicKey.as_view()),
]