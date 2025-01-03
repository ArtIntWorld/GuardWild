# Generated by Django 2.0 on 2024-12-25 17:39

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('animaldetectapp', '0019_remove_login_name'),
    ]

    operations = [
        migrations.CreateModel(
            name='StationAnimal',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('population', models.CharField(max_length=100)),
                ('animal', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='animaldetectapp.Animals')),
                ('station', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='animaldetectapp.ForestStation')),
            ],
        ),
    ]
