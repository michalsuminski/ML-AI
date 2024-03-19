import pandas as pandas
import torch
import torchvision
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score
from sklearn.model_selection import GridSearchCV
from sklearn.neural_network import MLPClassifier
from torchvision import transforms
from sklearn.metrics import confusion_matrix

import seaborn as sns

import matplotlib.pyplot as plt

train_dataset = torchvision.datasets.MNIST(root='./data', train=True, transform=transforms.Compose([
    transforms.Resize(224),
    transforms.CenterCrop(224),
    transforms.ToTensor(),
    transforms.Lambda(lambda x: x.repeat(3, 1, 1))
]), download=True)

test_dataset = torchvision.datasets.MNIST(root='./data', train=False, transform=transforms.Compose([
    transforms.Resize(224),
    transforms.CenterCrop(224),
    transforms.ToTensor(),
    transforms.Lambda(lambda x: x.repeat(3, 1, 1))
]), download=True)

model = torchvision.models.squeezenet1_0()

for param in model.parameters():
    param.requires_grad = False

train_features = []
train_labels = []
test_features = []
test_labels = []

model.eval()
with torch.no_grad():
    for images, labels in train_dataset:
        features = model(images.unsqueeze(0))
        train_features.append(features.squeeze().numpy())
        train_labels.append(labels)

    for images, labels in test_dataset:
        features = model(images.unsqueeze(0))
        test_features.append(features.squeeze().numpy())
        test_labels.append(labels)

train_features = torch.tensor(train_features)
train_labels = torch.tensor(train_labels)
test_features = torch.tensor(test_features)
test_labels = torch.tensor(test_labels)


mlp = MLPClassifier(max_iter=1000)

param_grid = {
    'hidden_layer_sizes': [(10,), (50,), (100,)],
    'activation': ['relu', 'logistic', 'tanh'],
    'solver': ['sgd', 'adam'],
}

gridMLP = GridSearchCV(mlp, param_grid, scoring='accuracy', cv=5)

gridMLP.fit(train_features, train_labels)

print("Best parameters: ", gridMLP.best_params_)
print("Best accuracy score: ", gridMLP.best_score_)

cv_results = pandas.DataFrame(gridMLP.cv_results_)
print(cv_results[['params', 'mean_test_score']])


mlpPredictions = gridMLP.best_estimator_.predict(test_features)
3
print("Accuracy: " + str(accuracy_score(test_labels, mlpPredictions)))
print("Precision(weighted): " + str(precision_score(test_labels, mlpPredictions, average='weighted')))
print("Precision(macro): " + str(precision_score(test_labels, mlpPredictions, average='macro')))
print("Recall(weighted): " + str(recall_score(test_labels, mlpPredictions, average='weighted')))
print("Recall(macro): " + str(recall_score(test_labels, mlpPredictions, average='macro')))
print("F1(weighted): " + str(f1_score(test_labels, mlpPredictions, average='weighted')))
print("F1(macro): " + str(f1_score(test_labels, mlpPredictions, average='macro')))

cm = confusion_matrix(test_labels, mlpPredictions)
classes = ['Class 0', 'Class 1', 'Class 2', 'Class 3', 'Class 4', 'Class 5', 'Class 6', 'Class 7', 'Class 8', 'Class 9']
fig, ax = plt.subplots(figsize=(10, 8))
sns.heatmap(cm, annot=True, cmap='Blues', fmt='d', xticklabels=classes, yticklabels=classes, ax=ax)
plt.xlabel('Predicted Labels')
plt.ylabel('True Labels')
plt.title('Confusion Matrix')
plt.show()
