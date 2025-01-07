# Generated by Django 2.0 on 2024-12-17 15:13

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('animaldetectapp', '0012_auto_20241217_1418'),
    ]

    operations = [
        migrations.CreateModel(
            name='ForestStation',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=100)),
                ('head', models.CharField(max_length=100)),
                ('email', models.CharField(max_length=100)),
                ('phone', models.CharField(max_length=100)),
                ('lattitude', models.CharField(max_length=100)),
                ('longitude', models.CharField(max_length=100)),
                ('proof', models.CharField(max_length=100)),
                ('division', models.CharField(max_length=100)),
                ('status', models.CharField(max_length=100)),
                ('staff_count', models.CharField(max_length=100)),
            ],
        ),
    ]