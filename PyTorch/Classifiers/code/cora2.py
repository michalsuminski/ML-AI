import pandas as pd
import torch
from matplotlib import pyplot as plt
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score, confusion_matrix
from sklearn.model_selection import GridSearchCV
from sklearn.neighbors import KNeighborsClassifier
from sklearn.neural_network import MLPClassifier
from sklearn.svm import SVC
import seaborn as sns
from torch_geometric.datasets import Planetoid

def compute_citation_classes(edge_index, y, num_classes):
    # Initialize an empty tensor to store the citation classes
    classes_quantity = torch.zeros((y.shape[0], num_classes), dtype=torch.long)

    # Iterate over each article and compute its citation class
    for i in range(y.shape[0]):
        # Get the indices of the articles that cite the current article
        citing_indices = torch.where(edge_index[1] == i)[0]

        # If no articles cite the current article, assign it to its own class
        if citing_indices.numel() != 0:
            # Compute the classes of the citing articles
            citing_classes = y[edge_index[0][citing_indices]]
            for city_class in citing_classes:
                classes_quantity[i][city_class.item()] += 1

    return classes_quantity


def citations_quantity(y, edge_index):
    citations = torch.zeros((len(y),))
    for i in range(edge_index.shape[1]):
        target_idx = edge_index[1][i]
        citations[target_idx] += 1
    return citations


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

x = data.x.to_dense()
y = data.y
edge_index = data.edge_index

print(f"Dataset: {dataset.name}")
print(
    f"Num. nodes: {num_nodes} (train={train_len}, val={val_len}, test={test_len}, train_val_mask={train_val_mask_len},other={other_len})")
print(f"Num. edges: {num_edges}")
print(f"Num. node features: {dataset.num_node_features}")
print(f"Num. classes: {dataset.num_classes}")
print(f'Is undirected: {data.is_undirected()}')
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
print(
    f"X: Feature matrix (sparse tensor) of shape (N, D), where N is the number of nodes and D is the number of features. {x}")
print(f"Y: Target variable (tensor) of shape (N,).. {y}")
print(f"Edge_index: Edge index (sparse tensor) of shape (2, E), where E is the number of edges.. {edge_index}")

train_mask_n = train_mask.numpy()
val_mask_n = val_mask.numpy()
test_mask_n = test_mask.numpy()

X = dataset.data.x.numpy()
y = dataset.data.y.numpy()

# Convert the masks to indices
train_indices = torch.arange(len(X))[train_mask]
val_indices = torch.arange(len(X))[val_mask]
test_indices = torch.arange(len(X))[test_mask]

train_val_indices = torch.cat([train_indices, val_indices], dim=0)

citation_class = compute_citation_classes(dataset.data.edge_index, dataset.data.y, classes.numel())

# Count how many times each article is cited
num_citations = citations_quantity(dataset.data.y, edge_index)

# Get the number of nodes in the dataset
num_nodes = dataset.data.num_nodes

# Create a feature matrix with the number of citations for each article
X_train_val_num_citations = num_citations[train_val_indices].unsqueeze(1)
X_train_val_citation_class = citation_class[train_val_indices]

X_test_citation_class = citation_class[test_indices]
X_test_num_citations = num_citations[test_indices].unsqueeze(1)

train_val_expanded = X_train_val_num_citations.expand(X_train_val_citation_class.shape[0], X_train_val_num_citations.shape[1])
X_train_val = torch.cat((train_val_expanded, X_train_val_citation_class), dim=1)

test_expanded = X_test_num_citations.expand(X_test_citation_class.shape[0], X_test_num_citations.shape[1])
X_test = torch.cat((test_expanded, X_test_citation_class), dim=1)

y_train_val = dataset.data.y[train_val_indices]
y_test = dataset.data.y[test_indices]


# KNN

param_grid = {
    'n_neighbors': [1, 5, 25, 50, 100],
    'weights': ['uniform', 'distance']
}
# # # Define the KNN classifier
knn = KNeighborsClassifier()
#
grid = GridSearchCV(knn, param_grid, scoring='accuracy', cv=5)
#
# # Train the KNN classifier on the training data
grid.fit(X_train_val, y_train_val)
#
print("Best parameters: ", grid.best_params_)
print("Best accuracy score: ", grid.best_score_)
#
cv_results = pd.DataFrame(grid.cv_results_)
print(cv_results[['params', 'mean_test_score']])

best_knn = grid.best_estimator_
print(best_knn)
test_acc = best_knn.score(X_test, y_test)
print("Test accuracy: {:.4f}".format(test_acc))


# MLP
mlp = MLPClassifier()
#
param_grid = {
    'hidden_layer_sizes': [(5,), (25,), (50,), (5, 5), (5, 5, 5)],
    'activation': ['identity', 'relu', 'tanh', 'logistic'],
    'solver': ['adam', 'sgd', 'lbfgs'],
}
# Use GridSearchCV to find the best hyperparameters
grid = GridSearchCV(mlp, param_grid, scoring='accuracy', cv=5)
# Train the MLP classifier on the training data
grid.fit(X_train_val, y_train_val)
# Evaluate the classifier on the test data
test_acc = grid.score(X_test, y_test)

# Print the best hyperparameters and test accuracy
print('Best parameters:', grid.best_params_)
print('Test accuracy score:', test_acc)

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
grid.fit(X_train_val, y_train_val)

# Print best hyperparameters and accuracy score
print("Best hyperparameters: ", grid.best_params_)
print("Accuracy score: ", grid.best_score_)

test_acc = grid.score(X_test, y_test)

cv_results = pd.DataFrame(grid.cv_results_)
print(cv_results[['params', 'mean_test_score']])

knnPredictions = grid.best_estimator_.predict(X_test)

print("Accuracy: " + str(round(accuracy_score(y_test, knnPredictions), 4)))
print("Precision(weighted): " + str(round(precision_score(y_test, knnPredictions, average='weighted'), 4)))
print("Precision(macro): " + str(round(precision_score(y_test, knnPredictions, average='macro'), 4)))
print("Recall(weighted): " + str(round(recall_score(y_test, knnPredictions, average='weighted'), 4)))
print("Recall(macro): " + str(round(recall_score(y_test, knnPredictions, average='macro'), 4)))
print("F1(weighted): " + str(round(f1_score(y_test, knnPredictions, average='weighted'), 4)))
print("F1(macro): " + str(round(f1_score(y_test, knnPredictions, average='macro'), 4)))

cm = confusion_matrix(y_test, knnPredictions)
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