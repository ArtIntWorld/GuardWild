# Generated by Django 2.0 on 2024-12-14 09:36

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('animaldetectapp', '0008_auto_20241213_2217'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='user',
            name='country',
        ),
    ]