# Generated by Django 2.0 on 2025-01-01 15:24

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('animaldetectapp', '0026_auto_20241229_1737'),
    ]

    operations = [
        migrations.RenameField(
            model_name='user',
            old_name='district',
            new_name='division',
        ),
    ]
