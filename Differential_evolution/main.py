import random
import matplotlib.pyplot as plt
import statistics
import pandas as pd




objects = [['Toporek', 32252, 68674],
           ['Moneta z brązu', 225790, 471010],
           ['Korona', 468164, 944620],
           ['Diamentowy posążek', 489494, 962094],
           ['Szmaragdowy pas', 35384, 78344],
           ['Skamieliny', 265590, 579152],
           ['Złota moneta', 497911, 902698],
           ['Hełm', 800493, 1686515],
           ['Tusz', 823576, 1688691],
           ['Szkatułka', 552202, 1056157],
           ['Nóż', 323618, 677562],
           ['Długi miecz', 382846, 833132],
           ['Maska', 44676, 99192],
           ['Naszyjnik', 169738, 376418],
           ['Opalowa zawieszka', 610876, 1253986],
           ['Perły', 854190, 1853562],
           ['Kołczan', 671123, 1320297],
           ['Rubinowy pierścień', 698180, 1301637],
           ['Srebrna bransoletka', 446517, 859835],
           ['Czasomierz', 909620, 1677534],
           ['Mundur', 904818, 1910501],
           ['Trucizna', 730061, 1528646],
           ['Wełniany szal', 931932, 1827477],
           ['Kusza', 952360, 2068204],
           ['Stara księga', 926023, 1746556],
           ['Puchar z cynku', 978724, 2100851]]





def population_generation(size_of_population, size_of_individual):
    population = [] 
    random.seed()
    for i in range(size_of_population):  
        individual = []  
        for j in range(size_of_individual):  
            individual.append(random.randint(0, 1))  
        population.append(individual)  
    return population


def fitness_function(individual, objects_in_backpack, backpack_capacity):
    weight = 0  
    value = 0 
    for i in range(len(individual)):  
        gene = individual[i]  
        if gene == 1:  
            weight += objects_in_backpack[i][1]  
            value += objects_in_backpack[i][2]  
    if weight > backpack_capacity:  
        return 0  
    else:
        return value  


def probability_in_population(population, objects_in_backpack, backpack_capacity):
    fitness_of_population = 0
    for i in range(len(population)):
        fitness_of_population += fitness_function(population[i], objects_in_backpack, backpack_capacity)
    for i in range(len(population)):
        population[i].append(
            fitness_function(population[i], objects_in_backpack, backpack_capacity) / fitness_of_population)


def choose_individual(population, intervals, roulette_value):
    for i in range(len(population)):
        if intervals[i][1] < roulette_value < intervals[i][2]: 
            return population[i]


def roulette_wheel_selection(population, number_of_chosen, objects_in_backpack, backpack_capacity):
    probability_in_population(population, objects_in_backpack,
                              backpack_capacity)  
    intervals = []  
    begin_of_interval = 0
    for i in range(len(population)):
        end_of_interval = begin_of_interval + population[i][-1]  
        intervals.append([i, begin_of_interval, end_of_interval]) 
        begin_of_interval = end_of_interval
    selected_individuals = []  
    for i in range(number_of_chosen):
        roulette_value = random.randrange(1, 100, 1) / 100  
        selected_individuals.append(choose_individual(population, intervals, roulette_value))
    return selected_individuals 
    

def elite_selection(population, number_of_chosen, objects_in_backpack, backpack_capacity):
    for i in range(len(population)):
        population[i].append(fitness_function(population[i], objects_in_backpack, backpack_capacity))
    elite = sorted(population, key=lambda pop: pop[-1], reverse=True)[:number_of_chosen]
    return elite


def one_point_crossover(parent_a, parent_b, crossover_point):
    first_child = parent_a[:crossover_point] + parent_b[crossover_point:]
    second_child = parent_b[:crossover_point] + parent_a[crossover_point:]
    return first_child, second_child


def uniform_crossover(parent_a, parent_b, pattern):
    first_child = []
    second_child = []
    for i in range(len(pattern)):
        if pattern[i] == 1:
            first_child.append(parent_a[i])
            second_child.append(parent_b[i])
        else:
            first_child.append(parent_b[i])
            second_child.append(parent_a[i])
    return first_child, second_child


def mutation(population, mutation_probability, number_of_bits=1):
    number_of_chosen = int(mutation_probability * len(population))
    random.shuffle(population)
    for i in range(number_of_chosen):
        random_number = random.randint(0, len(population[i]) - 2)
        for j in range(len(population[i])):
            if j == random_number:
                if population[i][j] == 0:
                    population[i][j] = 1
                else:
                    population[i][j] = 0
    return population


def genetic_algorithm(size_of_population, crossover_probability, mutation_probability, objects, backpack_capacity,
                      size_of_chromosome, number_of_generations, selection_method, crossover_method, crossover_point,
                      crossover_pattern):
    pop = population_generation(size_of_population, size_of_chromosome)
    sum_of_fitness_in_population = 0 
    tmp = 0  
    for i in range(len(pop)):
        sum_of_fitness_in_population += fitness_function(pop[i], objects, backpack_capacity)

    number_of_chosen = int(crossover_probability * len(pop))  

    for generation in range(number_of_generations):
        parents_crossover = selection_method(pop, number_of_chosen, objects,
                                             backpack_capacity)
        random.shuffle(pop)
        pop = pop[:2]
        for i in range(0, int(len(parents_crossover)), 2):
            if crossover_method == one_point_crossover:
                child1, child2 = one_point_crossover(parents_crossover[i], parents_crossover[i + 1], crossover_point)
            else:
                child1, child2 = uniform_crossover(parents_crossover[i], parents_crossover[i + 1], crossover_pattern)
            pop.append(child1)
            pop.append(child2)
        pop = mutation(pop, mutation_probability)
        tmp = sum_of_fitness_in_population
        sum_of_fitness_in_population = 0
        for i in range(len(pop)):
            pop[i] = pop[i][:-1]
            sum_of_fitness_in_population += fitness_function(pop[i], objects, backpack_capacity)
    for i in range(len(pop)):
        pop[i].append(fitness_function(pop[i], objects, backpack_capacity))
    pop = sorted(pop, key=lambda pop: pop[-1], reverse=True)
    return pop[0]


crossover_probability = ['0.2', '0.4', '0.6', '0.8', '0.9']
results2 = []
results4 = []
results6 = []
results8 = []
results9 = []
for i in range(5):
    results2.append(
        genetic_algorithm(size_of_population=100, crossover_probability=0.2, mutation_probability=0.1, objects=objects,
                          backpack_capacity=6404180,
                          size_of_chromosome=26, number_of_generations=100, selection_method=elite_selection,
                          crossover_method=one_point_crossover, crossover_point=3, crossover_pattern=None)[-1])
    results4.append(
        genetic_algorithm(size_of_population=100, crossover_probability=0.4, mutation_probability=0.1, objects=objects,
                          backpack_capacity=6404180,
                          size_of_chromosome=26, number_of_generations=100, selection_method=elite_selection,
                          crossover_method=one_point_crossover, crossover_point=3, crossover_pattern=None)[-1])
    results6.append(
        genetic_algorithm(size_of_population=100, crossover_probability=0.6, mutation_probability=0.1, objects=objects,
                          backpack_capacity=6404180,
                          size_of_chromosome=26, number_of_generations=100, selection_method=elite_selection,
                          crossover_method=one_point_crossover, crossover_point=3, crossover_pattern=None)[-1])
    results8.append(
        genetic_algorithm(size_of_population=100, crossover_probability=0.8, mutation_probability=0.1, objects=objects,
                          backpack_capacity=6404180,
                          size_of_chromosome=26, number_of_generations=100, selection_method=elite_selection,
                          crossover_method=one_point_crossover, crossover_point=3, crossover_pattern=None)[-1])
    results9.append(
        genetic_algorithm(size_of_population=100, crossover_probability=0.9, mutation_probability=0.1, objects=objects,
                          backpack_capacity=6404180,
                          size_of_chromosome=26, number_of_generations=100, selection_method=elite_selection,
                          crossover_method=one_point_crossover, crossover_point=3, crossover_pattern=None)[-1])

results_mean = [statistics.mean(results2), statistics.mean(results4), statistics.mean(results6),
                statistics.mean(results8),
                statistics.mean(results9)]
results_max = [max(results2), max(results4), max(results6), max(results8), max(results9)]
results_min = [min(results2), min(results4), min(results6), min(results8), min(results9)]

a = {'': {0.2: results_mean[0], 0.4: results_mean[1], 0.6: results_mean[2], 0.8: results_mean[3], 0.9: results_mean[4]}}
print("mean" + str(a))
b = {'': {0.2: results_max[0], 0.4: results_max[1], 0.6: results_max[2], 0.8: results_max[3], 0.9: results_max[4]}}
print("max" + str(b))
c = {'': {0.2: results_min[0], 0.4: results_min[1], 0.6: results_min[2], 0.8: results_min[3], 0.9: results_min[4]}}
print("min" + str(c))


def wykres(values, param, what):
    d = pd.DataFrame(values).T
    f = plt.figure()
    plt.ticklabel_format(style='plain')
    if param == "mean":
        plt.title(what + '(wartości średnie)', color='black')
    elif param == "max":
        plt.title(what + '(wartości max)', color='black')
    elif param == "min":
        plt.title(what + '(wartości min)', color='black')
    d.plot(kind='bar', ax=f.gca())
    plt.ylim([10000000, 14000000])
    plt.show()


wykres(a, 'mean', 'Prawdopodobieństwo krzyżowania')
wykres(b, 'max', 'Prawdopodobieństwo krzyżowania')
wykres(c, 'min', 'Prawdopodobieństwo krzyżowania')
