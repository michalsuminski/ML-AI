import numpy as np
import random
import math
import matplotlib
import matplotlib.pyplot as plt

# Reading data from file
with open('heart.csv') as f:
    X = []  # Vector with input data

    Y = []

    i = 0

    for line in f.readlines():
        if i == 0:
            i += 1
            continue
        x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, y = line.strip().split(",")
        X.append([int(x1), int(x2), int(x3), int(x4), int(x5), int(x6), int(x7), int(x8), int(x9), float(x10), int(x11),
                  int(x12), int(x13)])
        Y.append(int(y))

# Dividing input on train and test inputs
print(f'Liczność wektorów danych wejściowych wynosi: {len(X)}')
train_X = X[:120]
train_X.extend(X[165:285]) # The number of 1 outputs and 0 outputs in the train part is equal
test_X = X[120:165]
test_X.extend(X[285:])
train_Y = Y[:120]
train_Y.extend(Y[165:285])
test_Y = Y[120:165]
test_Y.extend(Y[285:])
print(f'Liczność wektorów danych treningowych wynosi: {len(train_X)}')
print(f'Liczność wektorów danych testowych wynosi: {len(test_X)}')
p = [0 for i in range(len(train_X[0]))]
print(f'Parametry wynoszą: {p}')


def sigmoid(x):
    return 1 / (1 + math.e ** -x)


def g(x, p):
    res = 0
    for i in range(len(x)):
        res += x[i] * p[i]
    return res


# function that calculus the derivative part
def derivative(x, y, j, p):
    m = len(x)
    res = 0
    for i in range(m):
        res += (sigmoid(g(x[i], p)) - y[i]) * x[i][j]

    return (1 / m) * res


def gradient_descent(iterations, alfa=0.1):
    # Number of iterations in gradient descent algorithm
    for i in range(iterations):
        copy_p = np.copy(p)
        for j in range(len(p)):
            p[j] = p[j] - alfa * derivative(train_X, train_Y, j, copy_p)


def perf_measure(true_y, function_y):
    TP = 0
    FP = 0
    TN = 0
    FN = 0

    for i in range(len(function_y)):
        if true_y[i] == 1 and function_y[i] >= 0.5:
            TP += 1
        if true_y[i] == 0 and function_y[i] >= 0.5:
            FP += 1
        if true_y[i] == 0 and function_y[i] < 0.5:
            TN += 1
        if true_y[i] == 1 and function_y[i] < 0.5:
            FN += 1

    return TP, FP, TN, FN


gradient_descent(10000, 0.01)
print(f'Parametry wynoszą: {p}')

test_result = [sigmoid(g(test_X[i], p)) for i in range(len(test_X))]
TP, FP, TN, FN = perf_measure(test_Y, test_result)

X_labels = ['POSITIVE', 'NEGATIVE']
Y_labels = ['POSITIVE', 'NEGATIVE']

table = np.array([[TP, FP],
                  [TN, FN]])

fig, ax = plt.subplots()
im = ax.imshow(table)

# We want to show all ticks...
ax.set_xticks(np.arange(2))
ax.set_yticks(np.arange(2))
# ... and label them with the respective list entries
ax.set_xticklabels(X_labels)
ax.set_yticklabels(Y_labels)
# Rotate the tick labels and set their alignment.
plt.setp(ax.get_xticklabels(), rotation=45, ha="right",
         rotation_mode="anchor")
# Loop over data dimensions and create text annotations.
for i in range(len(X_labels)):
    for j in range(len(Y_labels)):
        text = ax.text(j, i, table[i, j],
                       ha="center", va="center", color="w")

ax.set_title("Heart attack classificator")
fig.tight_layout()
plt.show()
