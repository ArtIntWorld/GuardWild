# Generated by Django 2.0 on 2024-12-28 17:06

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('animaldetectapp', '0024_complaint'),
    ]

    operations = [
        migrations.AlterField(
            model_name='user',
            name='district',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='animaldetectapp.District'),
        ),
    ]
