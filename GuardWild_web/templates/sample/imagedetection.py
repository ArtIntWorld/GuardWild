import os
import tensorflow as tf
from tensorflow.keras.applications import ResNet50
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras import layers, models
from tensorflow.keras.optimizers import Adam

# Dataset directories
train_dir = "dataset/train"
test_dir = "dataset/test"

# Step 1: Check if dataset directories exist
if not os.path.exists(train_dir) or not os.path.exists(test_dir):
    raise FileNotFoundError("Train or test directory not found!")

# Step 2: Data Preprocessing
train_datagen = ImageDataGenerator(
    rescale=1.0 / 255,  # Normalize pixel values to [0, 1]
    rotation_range=20,  # Augment with rotations
    width_shift_range=0.2,
    height_shift_range=0.2,
    shear_range=0.2,
    zoom_range=0.2,
    horizontal_flip=True,
    fill_mode="nearest"
)

test_datagen = ImageDataGenerator(rescale=1.0 / 255)  # Only normalize test data

train_data = train_datagen.flow_from_directory(
    train_dir,
    target_size=(224, 224),  # Resize to ResNet50 input size
    batch_size=32,
    class_mode="categorical"
)

test_data = test_datagen.flow_from_directory(
    test_dir,
    target_size=(224, 224),
    batch_size=32,
    class_mode="categorical"
)

# Step 3: Check the dataset loading
print(f"Found {train_data.samples} images in {len(train_data.class_indices)} classes for training.")
print(f"Found {test_data.samples} images in {len(test_data.class_indices)} classes for testing.")

# If no images are found, raise an error
if train_data.samples == 0 or test_data.samples == 0:
    raise ValueError("No images found. Check dataset structure and image formats.")

# Step 4: Load ResNet50
base_model = ResNet50(weights="imagenet", include_top=False, input_shape=(224, 224, 3))
base_model.trainable = False  # Freeze the base model layers

# Step 5: Build the Model
model = models.Sequential([
    base_model,
    layers.GlobalAveragePooling2D(),
    layers.Dense(128, activation="relu"),
    layers.Dropout(0.5),
    layers.Dense(train_data.num_classes, activation="softmax")  # Adjust for your classes
])

# Step 6: Compile the Model
model.compile(
    optimizer=Adam(learning_rate=0.001),
    loss="categorical_crossentropy",
    metrics=["accuracy"]
)

# Step 7: Train the Model
history = model.fit(
    train_data,
    epochs=10,  # Adjust as needed
    validation_data=test_data
)

# Step 8: Save the Fine-Tuned Model
model.save("fine_tuned_resnet50.h5")

print("Model training completed and saved as 'fine_tuned_resnet50.h5'.")
