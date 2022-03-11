import math
from random import randint, random, choice, shuffle


def read_from_file(filename):
    matrix = []
    with open(filename) as file:
        for line in file.readlines():
            tmp = []
            index, x, y = line.strip().split(" ")
            tmp.append(int(index))
            tmp.append(int(x))
            tmp.append(int(y))
            matrix.append(tmp)
    return matrix


def length(table):
    tablica = []
    tmp = []
    for i in table:
        for j in table:
            if j == i:
                tmp.append(0)
                continue
            horizontal = (j[1] - i[1]) * (j[1] - i[1])
            vertical = (j[2] - i[2]) * (j[2] - i[2])
            tmp.append(math.sqrt(horizontal + vertical))
        t = tmp.copy()
        tablica.append(t)
        tmp.clear()
    return tablica


class Ant:
    def __init__(self, tab):
        self.visited = []
        self.visited.append(randint(0, len(tab) - 1))
        self.matrix = tab

    def visit_random_attraction(self):
        all_att = [*range(len(self.matrix))]
        att_to_visit = [elem for elem in all_att if elem not in self.visited]
        random_num = choice(att_to_visit)
        self.visited.append(random_num)

    def visit_random_probabilistic(self, pheromone_traces, alfa, beta):
        current_att = self.visited[-1]
        all_att = [*range(len(self.matrix))]  
        att_to_visit = [elem for elem in all_att if elem not in self.visited]
        used_indexes = []
        used_probability = []
        sum_of_probability = 0
        for att in att_to_visit:
            used_indexes.append(att)
            pheromones_on_trace = pow(pheromone_traces[current_att][att], alfa)
            try:
                heuristic_on_trace = pow(1.0 / self.matrix[current_att][att], beta)
            except ZeroDivisionError:
                heuristic_on_trace = 0
            probability = pheromones_on_trace * heuristic_on_trace
            used_probability.append(probability)
            sum_of_probability += probability
        probability_att = []
        for att in used_probability:
            try:
                probability_att.append(att / sum_of_probability)
            except ZeroDivisionError:
                probability_att.append(0)
        return used_indexes, probability_att

    def roulette_wheel_selection(self, used_indexes, probability_att):
        num_att_to_visit = len(self.matrix) - len(self.visited)
        intervals = []  
        begin_intervals = 0
        for i in range(num_att_to_visit):
            end_intervals = begin_intervals + probability_att[i]
            intervals.append([used_indexes[i], begin_intervals, end_intervals])
            begin_intervals = end_intervals
        roulette_value = random()
        chosen = None
        for i in range(num_att_to_visit):
            if intervals[i][1] < roulette_value <= intervals[i][2]: 
                chosen = intervals[i][0]
                continue
        return chosen

    def visit_attraction(self, pheromone_traces, alfa, beta):
        used_indexes, probability_att = self.visit_random_probabilistic(pheromone_traces, alfa, beta)
        self.visited.append(self.roulette_wheel_selection(used_indexes, probability_att))

    def getLength(self):
        total_distance = 0
        for index in range(len(self.visited) - 1):
            try:
                total_distance += self.matrix[self.visited[index]][self.visited[index + 1]]
            except TypeError:
                total_distance += 0.0

        return total_distance


def population_generation(mat, quantity):
    ants = []
    for i in range(quantity):
        ants.append(Ant(mat))
    return ants


def update_pheromones(evaporation, pheromone_traces, num, ants):
    for x in range(num):
        for y in range(num):
            pheromone_traces[x][y] *= evaporation
            if x == y:
                continue
            for ant in ants:
                moves = []
                for i in range(num - 1):
                    moves.append([ant.visited[i], ant.visited[i + 1]])
                for move in moves:
                    if move[0] == x and move[1] == y:
                        pheromone_traces[x][y] += 1 / ant.getLength()
    return pheromone_traces


def getBestAnt(ants, old_best_ant):
    best_ant = old_best_ant
    for ant in ants:
        if best_ant is None:
            best_ant = ant
            continue
        distance = ant.getLength()
        if distance < best_ant.getLength():
            best_ant = ant
    return best_ant


def go_to_next_attraction(ants, probability, pheromone_traces, alfa, beta):
    number_of_ants_for_random_visit = int(len(ants) * probability)
    counter = 0
    shuffle(ants)
    for index, ant in enumerate(ants):
        if counter < number_of_ants_for_random_visit:
            ant.visit_random_attraction()
            counter += 1
        else:
            ant.visit_attraction(pheromone_traces, alfa, beta)


def get_coordinates(ant, mat):
    coordinates = []
    for i in ant.visited:
        coordinates.append(mat[i])
    print(coordinates)
    return coordinates


def write_informations(filename, coordinates,length,visited, quantity_of_ants, alfa, beta, evaporation):
    with open(filename, 'w') as file:
        file.writelines([str(filename), "\n", str(coordinates),
                         "\n", str(length),"\n", str(visited),
                         "\n", str(quantity_of_ants),"\n", str(alfa),
                         "\n", str(beta), "\n", str(evaporation)])


def alg_ant(filename, ant_quantity, alfa, beta, evaporation, iteration, random_probability):
    best_ant = None
    coordinates = read_from_file(filename)
    matrix = length(coordinates)
    pheromone_traces = [[1 for i in range(len(matrix))] for j in range(len(matrix))]
    for i in range(iteration):
        print(i)
        ants = population_generation(matrix, ant_quantity)
        visited = 1
        attractions_to_visit = len(matrix)
        while visited < attractions_to_visit:
            go_to_next_attraction(ants, random_probability, pheromone_traces, alfa, beta)
            visited += 1
        update_pheromones(evaporation, pheromone_traces, attractions_to_visit, ants)
        best_ant = getBestAnt(ants, best_ant)
    write_informations(filename + "_stats.txt", get_coordinates(best_ant, coordinates), best_ant.getLength(),best_ant.visited,ant_quantity, alfa,
                       beta, evaporation)

    return best_ant.visited


# print(alg_ant("A-n32-k5.txt", 10, 1, 2, 0.1, 1000, 0.3))
# print(alg_ant("A-n80-k10.txt", 10, 1, 2, 0.1, 1000, 0.3))
# print(alg_ant("B-n31-k5.txt", 10, 1, 2, 0.1, 1000, 0.3))
# print(alg_ant("B-n78-k10.txt", 10, 1, 2, 0.1, 1000, 0.3))
print(alg_ant("P-n16-k8.txt", 10, 1, 1, 0.1, 1000, 0.3))
# print(alg_ant("P-n76-k5.txt", 10, 1, 2, 0.1, 1000, 0.3))
