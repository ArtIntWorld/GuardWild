# Generated by Django 2.0 on 2024-12-13 13:42

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('animaldetectapp', '0004_auto_20241212_1705'),
    ]

    operations = [
        migrations.CreateModel(
            name='ForestDivision',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=100)),
                ('established_year', models.CharField(max_length=100)),
                ('description', models.CharField(max_length=100)),
                ('area_covered', models.CharField(max_length=100)),
                ('district', models.CharField(max_length=100)),
            ],
        ),
        migrations.AddField(
            model_name='animals',
            name='description',
            field=models.CharField(default=None, max_length=100),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='animals',
            name='photo',
            field=models.CharField(default=None, max_length=100),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='animals',
            name='type',
            field=models.CharField(default=None, max_length=100),
            preserve_default=False,
        ),
    ]
