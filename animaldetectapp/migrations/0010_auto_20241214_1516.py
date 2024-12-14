# Generated by Django 2.0 on 2024-12-14 09:46

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('animaldetectapp', '0009_remove_user_country'),
    ]

    operations = [
        migrations.CreateModel(
            name='Country',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=100)),
            ],
        ),
        migrations.CreateModel(
            name='District',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=100)),
            ],
        ),
        migrations.CreateModel(
            name='State',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=100)),
                ('country', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='animaldetectapp.Country')),
            ],
        ),
        migrations.RemoveField(
            model_name='user',
            name='pcode',
        ),
        migrations.RemoveField(
            model_name='user',
            name='street',
        ),
        migrations.AlterField(
            model_name='forestdivision',
            name='district',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='animaldetectapp.District'),
        ),
        migrations.AlterField(
            model_name='user',
            name='state',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='animaldetectapp.State'),
        ),
        migrations.AddField(
            model_name='district',
            name='state',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='animaldetectapp.State'),
        ),
        migrations.AddField(
            model_name='user',
            name='country',
            field=models.ForeignKey(default=None, on_delete=django.db.models.deletion.CASCADE, to='animaldetectapp.Country'),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='user',
            name='district',
            field=models.ForeignKey(default=None, on_delete=django.db.models.deletion.CASCADE, to='animaldetectapp.District'),
            preserve_default=False,
        ),
    ]
