# Generated by Django 2.0 on 2024-12-20 18:27

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('animaldetectapp', '0014_foreststation_password'),
    ]

    operations = [
        migrations.RenameField(
            model_name='login',
            old_name='username',
            new_name='name',
        ),
    ]
