import math
import numpy as np
import copy


class Node:
    def __init__(self, id: int, x: float, y: float, demand: float, ready_time: float, due_time: float,
                 service_time: float):
        super()
        self.id = id
        if id == 0:
            self.is_depot = True
        else:
            self.is_depot = False
        self.x = x
        self.y = y
        self.demand = demand
        self.ready_time = ready_time
        self.due_time = due_time
        self.service_time = service_time


def calculate_distance(first_node, second_node):
    horizontal = (first_node.x - second_node.x) * (first_node.x - second_node.x)
    vertical = (first_node.y - second_node.y) * (first_node.y - second_node.y)
    return math.sqrt(horizontal + vertical)


def create_distance_matrix(node_num, nodes):
    matrix = np.zeros((node_num, node_num))
    for i in range(node_num):
        first_node = nodes[i]
        matrix[i][i] = 1e-8
        for j in range(i + 1, node_num):
            second_node = nodes[j]
            matrix[i][j] = calculate_distance(first_node, second_node)
            matrix[j][i] = matrix[i][j]
    return matrix


def read_from_file(filename):
    node_list = []
    with open(filename, 'rt') as f:
        count = 1
        for line in f:
            if count == 3:
                vehicle_value, vehicle_capacity = line.split()
                vehicle_value = int(vehicle_value)
                vehicle_capacity = int(vehicle_capacity)
            elif count >= 6:
                node_list.append(line.split())
            count += 1
    nodes_len = len(node_list)
    nodes = list(Node(int(item[0]), float(item[1]), float(item[2]), float(item[3]), float(item[4]), float(item[5]),
                      float(item[6])) for item in node_list)
    distance_matrix = create_distance_matrix(nodes_len, nodes)
    return nodes_len, nodes, distance_matrix, vehicle_value, vehicle_capacity


class Graph:
    def __init__(self, filename, evaporation=0.1):
        super()
        self.nodes_len, self.nodes, self.distance_matrix, self.vehicle_value, self.vehicle_capacity \
            = read_from_file(filename)
        self.evaporation = evaporation
        self.nnh_path, self.initial_pheromone_value, _ = self.find_path()
        self.initial_pheromone_value = 1 / (self.initial_pheromone_value * self.nodes_len)
        self.pheromone_traces = np.ones((self.nodes_len, self.nodes_len)) * self.initial_pheromone_value
        self.heuristic_info_mat = 1 / self.distance_matrix

    def copy(self, initial_pheromone_value):
        new_graph = copy.deepcopy(self)
        new_graph.initial_pheromone_value = initial_pheromone_value
        new_graph.pheromone_traces = np.ones((new_graph.nodes_len, new_graph.nodes_len)) * initial_pheromone_value
        return new_graph

    def update_pheromones(self, best_path, best_path_distance):
        self.pheromone_traces = (1 - self.evaporation) * self.pheromone_traces
        current_neighbour = best_path[0]
        for next_neighbour in best_path[1:]:
            self.pheromone_traces[current_neighbour][next_neighbour] += self.evaporation / best_path_distance
            current_neighbour = next_neighbour

    def find_path(self, max_vehicle_number=None):
        neighbours_to_visit = list(range(1, self.nodes_len))
        current_neighbour = 0
        current_load = 0
        current_time = 0
        travel_distance = 0
        travel_path = [0]

        if max_vehicle_number is None:
            max_vehicle_number = self.nodes_len

        while len(neighbours_to_visit) > 0 and max_vehicle_number > 0:
            next_nearest_neighbour = self.find_nearest_neighbour(neighbours_to_visit, current_neighbour,
                                                                 current_load,
                                                                 current_time)
            if next_nearest_neighbour is None:
                travel_distance += self.distance_matrix[current_neighbour][0]
                current_load = 0
                current_time = 0
                travel_path.append(0)
                current_neighbour = 0
                max_vehicle_number -= 1
            else:
                current_load += self.nodes[next_nearest_neighbour].demand
                dist = self.distance_matrix[current_neighbour][next_nearest_neighbour]
                wait_time = max(self.nodes[next_nearest_neighbour].ready_time - current_time - dist, 0)
                service_time = self.nodes[next_nearest_neighbour].service_time

                current_time += dist + wait_time + service_time
                neighbours_to_visit.remove(next_nearest_neighbour)

                travel_distance += self.distance_matrix[current_neighbour][next_nearest_neighbour]
                travel_path.append(next_nearest_neighbour)
                current_neighbour = next_nearest_neighbour
        travel_distance += self.distance_matrix[current_neighbour][0]
        travel_path.append(0)

        vehicle_num = travel_path.count(0) - 1
        return travel_path, travel_distance, vehicle_num

    def find_nearest_neighbour(self, neighbour_to_visit, current_neighbour, current_load, current_time):
        nearest_neighbour = None
        nearest_distance = None

        for next_neighbour in neighbour_to_visit:
            if current_load + self.nodes[next_neighbour].demand > self.vehicle_capacity:
                continue
            dist = self.distance_matrix[current_neighbour][next_neighbour]
            wait_time = max(self.nodes[next_neighbour].ready_time - current_time - dist, 0)
            service_time = self.nodes[next_neighbour].service_time
            if current_time + dist + wait_time + service_time + self.distance_matrix[next_neighbour][0] > \
                    self.nodes[0].due_time:
                continue
            if current_time + dist > self.nodes[next_neighbour].due_time:
                continue
            if nearest_distance is None or self.distance_matrix[current_neighbour][next_neighbour] < nearest_distance:
                nearest_distance = self.distance_matrix[current_neighbour][next_neighbour]
                nearest_neighbour = next_neighbour

        return nearest_neighbour
