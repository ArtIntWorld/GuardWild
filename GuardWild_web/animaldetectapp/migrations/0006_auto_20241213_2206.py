# Generated by Django 2.0 on 2024-12-13 16:36

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('animaldetectapp', '0005_auto_20241213_1912'),
    ]

    operations = [
        migrations.AlterField(
            model_name='animals',
            name='photo',
            field=models.ImageField(upload_to='animals/'),
        ),
    ]