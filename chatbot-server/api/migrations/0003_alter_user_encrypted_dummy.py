# Generated by Django 4.0.6 on 2023-07-10 03:55

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0002_alter_user_encrypted_dummy_and_more'),
    ]

    operations = [
        migrations.AlterField(
            model_name='user',
            name='encrypted_dummy',
            field=models.BinaryField(max_length=255),
        ),
    ]