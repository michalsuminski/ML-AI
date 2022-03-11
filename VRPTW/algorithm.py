import numpy as np
import random
from ant import Ant
from graph import Graph


class Algorithm:
    def __init__(self, graph, ants_num=10, max_iter=200, alfa=1, beta=2, random_probability=0.1):
        super()
        self.graph = graph
        self.ants_num = ants_num
        self.max_iter = max_iter
        self.max_load = graph.vehicle_capacity
        self.alfa = alfa
        self.beta = beta
        self.random_probability = random_probability
        self.best_path_distance = None
        self.best_path = None
        self.best_vehicle_num = None

    def ant_algorithm(self):
        for i in range(self.max_iter):
            ants = list(Ant(self.graph) for j in range(self.ants_num))
            for k in range(self.ants_num):
                while not len(ants[k].att_to_visit) == 0:
                    next_index = self.select_next_attraction(ants[k])
                    if not ants[k].check_condition(next_index):
                        next_index = self.select_next_attraction(ants[k])
                        if not ants[k].check_condition(next_index):
                            next_index = 0
                    ants[k].visit_attraction(next_index)
                ants[k].visit_attraction(0)
            paths_distance = np.array([ant.total_travel_distance for ant in ants])
            best_index = np.argmin(paths_distance)

            if self.best_path is None or paths_distance[best_index] < self.best_path_distance:
                self.best_path = ants[int(best_index)].travel_path
                self.best_path_distance = paths_distance[best_index]
                self.best_vehicle_num = self.best_path.count(0) - 1

            self.graph.update_pheromones(self.best_path, self.best_path_distance)
            print('Iteration ' + str(i))

        print('Best path distance is ' + str(round(self.best_path_distance, 3)) + ', number of vehicle is ' + str(
            self.best_vehicle_num))

    def select_next_attraction(self, ant):
        current_index = ant.current_attraction
        index_to_visit = ant.att_to_visit

        pheromones_on_trace = np.power(self.graph.pheromone_traces[current_index][index_to_visit], self.alfa)
        heuristic_on_trace = np.power(self.graph.heuristic_info_mat[current_index][index_to_visit], self.beta)
        probability = pheromones_on_trace * heuristic_on_trace

        if random.random() < self.random_probability:
            used_index = np.argmax(probability)
            next_index = index_to_visit[used_index]
        else:
            length = len(index_to_visit)
            sum_of_probability = np.sum(probability)
            normalized_probability = probability / sum_of_probability
            while True:
                index = int(length * random.random())
                if random.random() <= normalized_probability[index]:
                    next_index = index_to_visit[index]
                    break
        return next_index


if __name__ == '__main__':
    graph = Graph(filename='traces/c104.txt', evaporation=0.1)
    algorithm = Algorithm(graph, ants_num=50, max_iter=200, alfa=1, beta=2, random_probability=0.1)
    algorithm.ant_algorithm()
