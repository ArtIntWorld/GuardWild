# Generated by Django 2.0 on 2024-12-12 10:38

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('animaldetectapp', '0002_login_email'),
    ]

    operations = [
        migrations.CreateModel(
            name='Animals',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=100)),
                ('conservation_status', models.CharField(max_length=100)),
                ('risk', models.CharField(max_length=100)),
            ],
        ),
    ]
