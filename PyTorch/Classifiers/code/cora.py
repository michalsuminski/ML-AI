import pandas as pd
import seaborn as sns
import torch
from matplotlib import pyplot as plt
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score, confusion_matrix
from sklearn.model_selection import GridSearchCV
from sklearn.neighbors import KNeighborsClassifier
from sklearn.neural_network import MLPClassifier
from sklearn.svm import SVC
from torch_geometric.datasets import Planetoid

# Load the Cora dataset
dataset = Planetoid(root='data/Cora', name='Cora')

data = dataset[0]
num_edges = data.num_edges // 2

train_mask = data['train_mask']
val_mask = data['val_mask']
test_mask = data['test_mask']

train_val_mask = torch.cat([dataset.data['train_mask'], dataset.data['val_mask']])
train_val_mask_len = train_val_mask.sum()

num_nodes = data.num_nodes

train_len = train_mask.sum()
val_len = val_mask.sum()
test_len = test_mask.sum()
other_len = num_nodes - train_len - val_len - test_len

edge_index = data.edge_index

x = data.x.to_dense()
y = data.y

print(f"Dataset: {dataset.name}")
print(
    f"Num. nodes: {num_nodes} (train={train_len}, val={val_len}, test={test_len}, train_val_mask={train_val_mask_len},other={other_len})")
print(f"Num. edges: {num_edges}")
print(f"Num. node features: {dataset.num_node_features}")
print(f"Num. classes: {dataset.num_classes}")
# Get the class labels
classes = torch.unique(y)
print(f"Classes: {classes}")

label_to_topic = {
    0: "Case_Based",
    1: "Genetic_Algorithms",
    2: "Neural_Networks",
    3: "Probabilistic_Methods",
    4: "Reinforcement_Learning",
    5: "Rule_Learning",
    6: "Theory"
}
# Print the topics
print("Topics:")
print(label_to_topic)

print(f"Dataset len.: {dataset.len()}")
print(f"X: Feature matrix (sparse tensor) of shape (N, D), where N is the number of nodes and D is the number of features. {x}")
print(f"Y: Target variable (tensor) of shape (N,).. {y}")
print(f"Edge_index: Edge index (sparse tensor) of shape (2, E), where E is the number of edges.. {edge_index}")

X = dataset.data.x.numpy()
y = dataset.data.y.numpy()

# Convert the masks to indices
train_indices = torch.arange(len(X))[train_mask]
val_indices = torch.arange(len(X))[val_mask]
test_indices = torch.arange(len(X))[test_mask]

train_val_indices = torch.cat([train_indices, val_indices], dim=0)

# KNN
param_grid = {
    'n_neighbors': [1, 5, 25, 50, 100],
    'weights': ['uniform', 'distance']
}

# Define the KNN classifier
knn = KNeighborsClassifier()

grid = GridSearchCV(knn, param_grid, scoring='accuracy', cv=5)

# Train the KNN classifier on the training data
grid.fit(X[train_val_indices], y[train_val_indices])

print("Best parameters: ", grid.best_params_)
print("Best accuracy score: ", grid.best_score_)

cv_results = pd.DataFrame(grid.cv_results_)
print(cv_results[['params', 'mean_test_score']])

# MLP
# Define the MLP classifier
mlp = MLPClassifier()

# Define the hyperparameter grid to search
param_grid = {
    'hidden_layer_sizes': [(5,), (25,), (50,), (5, 5), (5, 5, 5)],
    'activation': ['identity', 'relu', 'tanh', 'logistic'],
    'solver': ['adam', 'sgd', 'lbfgs'],
}

# Use GridSearchCV to find the best hyperparameters
grid = GridSearchCV(mlp, param_grid, scoring='accuracy', cv=5)

# Train the MLP classifier on the training data
grid.fit(X[train_val_indices], y[train_val_indices])

# Evaluate the classifier on the test data
test_acc = grid.score(X[test_indices], y[test_indices])

# Print the best hyperparameters and test accuracy
print('Best hyperparameters:', grid.best_params_)
print('Test accuracy:', test_acc)

cv_results = pd.DataFrame(grid.cv_results_)
print(cv_results[['params', 'mean_test_score']])


# SVM
# Create SVM model
svm_model = SVC()

# Set up parameter grid for grid search
param_grid = {'C': [0.1, 1, 10, 50, 100],
              'kernel': ['linear', 'poly', 'rbf', 'sigmoid'],
              'gamma': ['scale']}

# Perform grid search with 5-fold cross validation
grid = GridSearchCV(svm_model, param_grid, cv=5, scoring='accuracy')
grid.fit(X[train_val_indices], y[train_val_indices])

# Print best hyperparameters and accuracy score
print("Best hyperparameters: ", grid.best_params_)
print("Accuracy score: ", grid.best_score_)

test_acc = grid.score(X[test_indices], y[test_indices])

cv_results = pd.DataFrame(grid.cv_results_)
print(cv_results[['params', 'mean_test_score']])

knnPredictions = grid.best_estimator_.predict(X[test_indices])

print("Accuracy: " + str(accuracy_score(y[test_indices], knnPredictions)))
print("Precision(weighted): " + str(precision_score(y[test_indices], knnPredictions, average='weighted')))
print("Precision(macro): " + str(precision_score(y[test_indices], knnPredictions, average='macro')))
print("Recall(weighted): " + str(recall_score(y[test_indices], knnPredictions, average='weighted')))
print("Recall(macro): " + str(recall_score(y[test_indices], knnPredictions, average='macro')))
print("F1(weighted): " + str(f1_score(y[test_indices], knnPredictions, average='weighted')))
print("F1(macro): " + str(f1_score(y[test_indices], knnPredictions, average='macro')))

cm = confusion_matrix(y[test_indices], knnPredictions)
# Define class labels
classes = ['Class 0', 'Class 1', 'Class 2', 'Class 3', 'Class 4', 'Class 5', 'Class 6']
# Set the size of the figure
fig, ax = plt.subplots(figsize=(7, 8))

# Create heatmap using seaborn
sns.heatmap(cm, annot=True, cmap='Blues', fmt='d', xticklabels=classes, yticklabels=classes, ax=ax)
# Add labels and title to plot
plt.xlabel('Predicted Labels')
plt.ylabel('True Labels')
plt.title('Confusion Matrix')

# Display plot
plt.show()
