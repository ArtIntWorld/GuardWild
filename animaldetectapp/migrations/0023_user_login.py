# Generated by Django 2.0 on 2024-12-27 13:56

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('animaldetectapp', '0022_auto_20241226_2103'),
    ]

    operations = [
        migrations.AddField(
            model_name='user',
            name='login',
            field=models.ForeignKey(default=None, on_delete=django.db.models.deletion.CASCADE, to='animaldetectapp.Login'),
            preserve_default=False,
        ),
    ]
