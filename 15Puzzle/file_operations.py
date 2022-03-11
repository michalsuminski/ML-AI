row_number = 0
col_number = 0
matrix = []


def read_from_file(filename):
    counter = 0
    with open(filename) as file:
        for line in file.readlines():
            if counter == 0:
                row_number, col_number = line.strip().split(" ")
                counter += 1
                continue
            a, b, c, d = line.strip().split(" ")
            tab = []
            tab.append(int(a))
            tab.append(int(b))
            tab.append(int(c))
            tab.append(int(d))
            matrix.append(tab)


def write_solution(filename, length, solution):
    sol = ''
    for i in solution:
        sol += str(i[0])
    with open(filename, 'w') as file:
        file.writelines([str(length), "\n", str(sol)])


def write_error(filename):
    with open(filename, 'w') as file:
        file.write(str(-1))


def write_informations(filename, length, visited, processed, depth, time):
    with open(filename, 'w') as file:
        file.writelines([str(length), "\n", str(visited), "\n", str(processed), "\n", str(depth), "\n", str(time)])

