# Generated by Django 2.0 on 2025-01-02 15:35

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('animaldetectapp', '0028_auto_20250101_2100'),
    ]

    operations = [
        migrations.CreateModel(
            name='Subscribe',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('email', models.CharField(max_length=100)),
            ],
        ),
    ]
