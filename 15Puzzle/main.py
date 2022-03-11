from operator import attrgetter
import argparse
import file_operations as f
import timeit

desired_mat = [
    [1, 2, 3, 4],
    [5, 6, 7, 8],
    [9, 10, 11, 12],
    [13, 14, 15, 0]
]

example_state = [[1, 2, 0, 3],
                 [5, 6, 7, 4],
                 [9, 10, 11, 8],
                 [13, 14, 15, 12]]

example_state_0_in_first_row = [[5, 0, 1, 9],
                                [6, 10, 15, 3],
                                [2, 7, 4, 11],
                                [12, 8, 14, 13]]

example_state_0_in_last_row = [[5, 4, 1, 9],
                               [6, 10, 15, 3],
                               [2, 7, 14, 11],
                               [12, 8, 0, 13]]

example_state_0_in_first_column = [[5, 4, 1, 9],
                                   [6, 10, 15, 3],
                                   [0, 7, 2, 11],
                                   [12, 8, 14, 13]]

example_state_0_in_last_column = [[1, 2, 3, 4],
                                  [5, 6, 7, 8],
                                  [9, 10, 11, 0],
                                  [13, 14, 15, 12]]

exa = [[1, 2, 3, 4],
       [5, 6, 7, 8],
       [9, 10, 11, 0],
       [13, 14, 15, 12]]

ex = [[1, 2, 3, 4],
      [5, 6, 7, 8],
      [9, 10, 11, 12],
      [0, 13, 14, 15]]

ex9 = [[0, 2, 3, 4],
       [1, 6, 7, 8],
       [5, 10, 11, 12],
       [9, 13, 14, 15]]

ex2 = [[1, 2, 3, 4],
       [5, 6, 7, 8],
       [10, 13, 11, 12],
       [9, 14, 0, 15]]

test = [[1, 2, 3, 4],
        [5, 6, 7, 8],
        [9, 0, 11, 12],
        [13, 10, 14, 15]]


# function to check if matrix is properly solved
def is_solved(matrix):
    properMatrix = [[1, 2, 3, 4],
                    [5, 6, 7, 8],
                    [9, 10, 11, 12],
                    [13, 14, 15, 0]]
    return matrix == properMatrix


def show(matrix):
    for row in matrix:
        print(row)
    print(" ")


def findZero(matrix):
    for row, i in enumerate(matrix):
        try:
            column = i.index(0)
        except ValueError:
            continue
        return row, column
    return -1


def goUp(matrix, row, col):
    matrix[row][col] = matrix[row - 1][col]
    matrix[row - 1][col] = 0
    return matrix


def goDown(matrix, row, col):
    matrix[row][col] = matrix[row + 1][col]
    matrix[row + 1][col] = 0
    return matrix


def goLeft(matrix, row, col):
    matrix[row][col] = matrix[row][col - 1]
    matrix[row][col - 1] = 0
    return matrix


def goRight(matrix, row, col):
    matrix[row][col] = matrix[row][col + 1]
    matrix[row][col + 1] = 0
    return matrix


def findNeighbours(matrix, order):  # function to find neighbours
    rowZero, colZero = findZero(matrix)  # check what is the row and column of zero
    U = None
    D = None
    L = None
    R = None
    if not rowZero == 0:
        tmp = list(map(list, matrix))
        U = goUp(tmp, rowZero, colZero)
    if not rowZero == 3:
        tmp = list(map(list, matrix))
        D = goDown(tmp, rowZero, colZero)
    if not colZero == 0:
        tmp = list(map(list, matrix))
        L = goLeft(tmp, rowZero, colZero)
    if not colZero == 3:
        tmp = list(map(list, matrix))
        R = goRight(tmp, rowZero, colZero)
    if order == "RDUL":
        neighbours = [R, D, U, L]
    elif order == "RDLU":
        neighbours = [R, D, L, U]
    elif order == "DRUL":
        neighbours = [D, R, U, L]
    elif order == "DRLU":
        neighbours = [D, R, L, U]
    elif order == "LUDR":
        neighbours = [L, U, D, R]
    elif order == "LURD":
        neighbours = [L, U, R, D]
    elif order == "ULDR":
        neighbours = [U, L, D, R]
    elif order == "ULRD":
        neighbours = [U, L, R, D]
    else:
        print("Wrong order")
        return -1
    return neighbours


def findPath(matrix):
    path = []
    while matrix.parent != 'Root':
        # show(matrix.matrix)
        path.append(matrix.moves)
        matrix = matrix.parent
    path.reverse()
    return path


def openListCheck(openList, move):
    flag = True
    for elem in openList:
        if elem.matrix == move:
            flag = False
    return flag


class State:
    def __init__(self, matrix):
        self.matrix = matrix  # current board
        self.parent = 'Root'  # what is the actual parent of that state
        self.depth = 0
        self.moves = []
        self.priority = 0


# function to solve matrix with bfs: two arguments: matrix and order of moving
def bfs(matrix, order):
    starttime = timeit.default_timer()
    state = State(matrix)  # the object of State class
    openList = []  # list containing next matrices to check
    exploredList = []  # list containing explored matrices
    index = 0  # used to support neigh list to find out the move
    explored_states = 1 
    processed_states = 1 
    iterator = 0
    while not is_solved(state.matrix):
        if state.matrix not in exploredList:
            exploredList.append(state.matrix)  # if state not in exploredList,we're adding it
        neighbours = findNeighbours(state.matrix, order)  # find out about possible moves
        # if neighbours[index] == 0 not possible to go somewhere
        for move in neighbours:  # for every element in list
            if move is not None and move not in exploredList and openListCheck(openList, move):
                # checking if move not = 0 and not in explored, openlist
                move = State(move)
                move.depth = state.depth + 1
                move.moves.append(order[index])
                move.parent = state
                explored_states += 1
                if is_solved(move.matrix):
                    time = timeit.default_timer() - starttime
                    time = time * 1000
                    time = float("%.3f" % time)
                    return move.matrix, move.depth, processed_states, explored_states, findPath(move), len(
                        findPath(move)), time
                openList.append(move)
            index = index + 1
        if not openList:
            time = timeit.default_timer() - starttime
            time = time * 1000
            time = float("%.3f" % time)
            return state.matrix, state.depth, processed_states, explored_states, findPath(state), -1, time
        state = openList[iterator]
        iterator += 1
        processed_states += 1
        index = 0
    # if not solved
    time = timeit.default_timer() - starttime
    time = time * 1000
    time = float("%.3f" % time)
    return state.matrix, state.depth, processed_states, explored_states, findPath(state), -1, time



def toTuple(matrix):
    tmp = list(map(list, matrix))
    for row in range(len(tmp)):
        tmp[row] = tuple(tmp[row])
    return tuple(tmp)


def dfs(matrix, order, max_deep=25):
    starttime = timeit.default_timer()
    state = State(matrix)  # the object of State class
    openlist = []
    closedlist = set()  # set is used because we only need information if something is in this set or not
    index = 3
    explored_states = 1 
    processed_states = 1 
    tupleMove = None
    max_deep_reached = 0
    # if input matrix is properly solved matrix then end algorithm
    if is_solved(state.matrix):
        time = timeit.default_timer() - starttime
        time = time * 1000
        time = float("%.3f" % time)
        return state.matrix, max_deep_reached, processed_states, explored_states, findPath(state), len(
            findPath(state)), time
    openlist.append(state)
    while openlist:
        state = openlist.pop()
        if state.depth == max_deep:
            tupleMove = toTuple(state.matrix)
            closedlist.add(tupleMove)  # processed state added into closed list
            continue
        # if input matrix isn't properly solved matrix then start algorithm
        # show(state.matrix)
        neighbours = reversed(findNeighbours(state.matrix, order))
        for move in neighbours:
            # if not neighbour == 0 and neighbour not in closedlist:
            if move is not None:
                tupleMove = toTuple(move)
            if move is not None and tupleMove not in closedlist and openListCheck(openlist, move):
                move = State(move)
                move.depth = state.depth + 1
                if move.depth > max_deep_reached:
                    max_deep_reached = move.depth
                move.moves.append(order[index])
                move.parent = state
                explored_states += 1
                if is_solved(move.matrix):  # check if the neighbour is solution of the game
                    time = timeit.default_timer() - starttime
                    time = time * 1000
                    time = float("%.3f" % time)
                    return move.matrix, max_deep_reached, processed_states, explored_states, findPath(move), len(
                        findPath(move)), time
                else:
                    openlist.append(move)
            index -= 1
        tupleMove = toTuple(state.matrix)
        closedlist.add(tupleMove)  # processed state added into closed list
        index = 3
        processed_states += 1
    # if not solved
    time = timeit.default_timer() - starttime
    time = time * 1000
    time = float("%.3f" % time)
    return state.matrix, max_deep_reached, processed_states, explored_states, findPath(state), -1, time


def hamming(matrix):
    suma = 0
    for row in range(4):
        for col in range(4):
            if matrix[row][col] == 0:
                continue
            if matrix[row][col] != desired_mat[row][col]:
                suma += 1
    return suma


def findElement(element):
    for row in range(4):
        for col in range(4):
            if desired_mat[row][col] == element:
                return col, row


def manhatan(matrix):
    suma = 0
    for row in range(4):
        for col in range(4):
            if matrix[row][col] == 0: 
                continue
            elif matrix[row][col] == desired_mat[row][col]:  
                continue
            else:
                desired_row, desired_col = findElement(matrix[row][col])  
                suma += abs(row - desired_row) + abs(col - desired_col)
    return suma


def astr(matrix, function, order="ULDR"):
    # show(matrix)
    starttime = timeit.default_timer()
    if function == 'hamm':
        heuristic = hamming
    elif function == 'manh':
        heuristic = manhatan
    else:
        print("Błędna heurystyka (manh, hamm")
        return -1
    state = State(matrix)  # the object of State class
    openList = []  # list containing next matrices to check
    exploredList = []  # list containing explored matrices
    index = 0  # used to support neigh list to find out the move
    explored_states = 1  
    processed_states = 1  
    while not is_solved(state.matrix):
        if state.matrix not in exploredList:
            exploredList.append(state.matrix)  # if state not in exploredList,we're adding it
        neighbours = findNeighbours(state.matrix, order)  # find out about possible moves
        for move in neighbours:  # for every element in list
            if move is not None and move not in exploredList and openListCheck(openList, move):
                # checking if move = 0 and was explored
                move = State(move)
                move.depth = state.depth + 1
                move.moves.append(order[index])
                move.parent = state
                move.priority = heuristic(move.matrix) + move.depth  
                explored_states += 1
                if is_solved(move.matrix):
                    time = timeit.default_timer() - starttime
                    time = time * 1000
                    time = float("%.3f" % time)
                    return move.matrix, move.depth, processed_states, explored_states, findPath(move), len(
                        findPath(move)), time
                openList.append(move)
            index = index + 1
        if not openList:
            # if not solved
            time = timeit.default_timer() - starttime
            time = time * 1000
            time = float("%.3f" % time)
            return state.matrix, state.depth, processed_states, explored_states, findPath(state), -1, time
        if heuristic == hamming:
            openList.sort(key=attrgetter('priority'))
        if heuristic == manhatan:
            openList.sort(key=attrgetter('priority'))
        state = openList.pop(0)
        index = 0
        processed_states += 1
    # if not solved
    time = timeit.default_timer() - starttime
    time = time * 1000
    time = float("%.3f" % time)
    return state.matrix, state.depth, processed_states, explored_states, findPath(state), -1, time




if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='SISE 2021 Piętnastka.')
    parser.add_argument('algorithm')
    parser.add_argument('order')
    parser.add_argument('source_file')
    parser.add_argument('solution_file')
    parser.add_argument('statistic_file')
    args = parser.parse_args()

    f.read_from_file(args.source_file)

    if args.algorithm == 'bfs':
        print("Algorytm BFS")
        matrix, depth, processed, explored, solution, length, time = bfs(f.matrix, args.order)
        f.write_informations(args.statistic_file, length, explored, processed, depth, time)
        if length == -1:
            f.write_error(args.solution_file)
        else:
            f.write_solution(args.solution_file, length, solution)
    elif args.algorithm == 'dfs':
        print("Algorytm DFS")
        matrix, depth, processed, explored, solution, length, time = dfs(f.matrix, args.order)
        f.write_informations(args.statistic_file, length, explored, processed, depth, time)
        if length == -1:
            f.write_error(args.solution_file)
        else:
            f.write_solution(args.solution_file, length, solution)
    elif args.algorithm == 'astr': 
        print("Algorytm A*")
        matrix, depth, processed, explored, solution, length, time = astr(f.matrix, args.order)
        f.write_informations(args.statistic_file, length, explored, processed, depth, time)
        if length == -1:
            f.write_error(args.solution_file)
        else:
            f.write_solution(args.solution_file, length, solution)
    else:
        print("Podano zły algorytm (dfs, bfs, astr")
