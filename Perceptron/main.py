import math
import random

import numpy as np


class Neuron:
    def __init__(self, x_input, weights):
        self.x_input = x_input
        self.weights = weights

    def summary(self, x_input, weights):
        res = 0.0
        for i in range(len(x_input)):
            res += x_input[i] * weights[i]
        return res


class Layer:
    def __init__(self, number_of_neurons, x_input, bias=False):
        self.bias = bias
        self.y_output = list(range(number_of_neurons))
        self.x_input = x_input
        if bias is True:
            self.x_input.append(1)

        # ustawienie losowych wag
        self.weights = []
        random.seed()
        pom = len(self.x_input)
        if bias is True:
            pom = len(self.x_input) - 1
        for i in range(number_of_neurons):
            tmp = []
            for j in range(pom):
                tmp.append(random.uniform(0, 1))
            if bias is True:
                tmp.append(random.uniform(0, 1))
            self.weights.append(tmp)

        # błędów jest tyle samo ile wyjść
        self.errors = list(range(number_of_neurons))

        self.neurons = list(range(number_of_neurons))
        for i in range(number_of_neurons):
            # wagi to bedzie lista list wag dlatego dla odpowiedniego neuronu jest odpowiednio ita lista wag
            self.neurons[i] = Neuron(self.x_input, self.weights[i])

    def counting_outputs(self, x_input):
        self.x_input = x_input
        if self.bias is True:
            self.x_input = np.append(x_input, 1)
        for i in range(len(self.neurons)):
            self.y_output[i] = self.activation(self.neurons[i].summary(self.x_input, self.weights[i]))

    def changing_weights(self, alpha=0.3):
        for i in range(len(self.weights)):
            for j in range(len(self.weights[i])):
                self.weights[i][j] -= alpha * self.x_input[j] * self.errors[i]
                # print(self.weights[i])

    def activation(self, z):
        return 1 / (1 + math.e ** (-z))

    def d_f_activation(self, z):
        return self.activation(z) * (1 - self.activation(z))


class Network:
    def __init__(self, len_of_x_input, neurons_in_hidden_layer, neurons_in_last_layer, bias=False):
        self.x_input = list(range(len_of_x_input))
        self.neurons_in_hidden_layer = neurons_in_hidden_layer
        self.neurons_in_last_layer = neurons_in_last_layer
        self.hidden_layer = Layer(self.neurons_in_hidden_layer, self.x_input, bias)
        self.last_layer = Layer(self.neurons_in_last_layer, self.hidden_layer.y_output)

    def propagate(self, x_input):
        self.hidden_layer.counting_outputs(x_input)
        self.last_layer.counting_outputs(self.hidden_layer.y_output)

    def calculate_err(self, final_output):
        for i in range(len(self.last_layer.errors)):
            self.last_layer.errors[i] = self.last_layer.d_f_activation(self.last_layer.y_output[i]) * (
                    self.last_layer.y_output[i] - final_output[i])
        return sum(self.last_layer.errors)

    def backward_pass(self):
        for i in range(len(self.hidden_layer.neurons)):
            for j in range(len(self.last_layer.neurons)):
                self.hidden_layer.errors[i] += self.last_layer.weights[j][i] * self.last_layer.errors[j]
            self.hidden_layer.errors[i] = self.hidden_layer.d_f_activation(self.hidden_layer.y_output[i]) * \
                                          self.hidden_layer.errors[i]

    def update_weights(self):
        self.hidden_layer.changing_weights()
        self.last_layer.changing_weights()


data = np.array([
    [1.0, 0.0, 0.0, 0.0],
    [0.0, 1.0, 0.0, 0.0],
    [0.0, 0.0, 1.0, 0.0],
    [0.0, 0.0, 0.0, 1.0]
], dtype=np.float)

indices = [i for i in range(4)]

mlp = Network(4, 3, 4, True)

for i in range(50000):
    random.shuffle(indices)
    err = 0.0
    for ind in indices:
        mlp.propagate(data[ind])
        err += mlp.calculate_err(data[ind])
        mlp.backward_pass()
        mlp.update_weights()
    # print(f"Epoch {i + 1}: MSE = {err / 4.0}")

for item in data:
    mlp.propagate(item)
    print(f"oczekiwane: {item}")
    print(f"otrzymane: {mlp.last_layer.y_output}")

# data = np.array([
#     [0.0, 0.0],
#     [0.0, 1.0],
#     [1.0, 0.0],
#     [1.0, 1.0]
# ], dtype=np.float)
#
# xor = np.array([
#     [0.0],
#     [1.0],
#     [1.0],
#     [0.0]
# ], dtype=np.float)
#
# indices = [i for i in range(4)]
#
# mlp = Network(2, 3, 1)
#
# for i in range(10000):
#     random.shuffle(indices)
#     err = 0.0
#     for ind in indices:
#         mlp.propagate(data[ind])
#         err += mlp.calculate_err(xor[ind])
#         mlp.backward_pass()
#         mlp.update_weights()
#     # print(f"Epoch {i + 1}: MSE = {err / 4.0}")
# # print(mlp.last_layer.y_output)
# counter = [i for i in range(4)]
# for c in counter:
#     mlp.propagate(data[c])
#     print(f"wejscie: {data[c]}")
#     print(f"oczekiwane: {xor[c]}")
#     print(f"otrzymane: {mlp.last_layer.y_output}")
