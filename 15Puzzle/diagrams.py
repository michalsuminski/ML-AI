import numpy as np
import matplotlib.pyplot as plt
from statistics import mean


def read_to_charts(filename):
    results = []
    with open(filename) as file:
        for line in file.readlines():
            deep, nr_file, algorithm, order, length, visited, processed, max_depth, time = line.strip().split(" ")
            tab = []
            tab.append(int(deep))
            tab.append(int(nr_file))
            tab.append(str(algorithm))
            tab.append(str(order))
            tab.append(int(length))
            tab.append(int(visited))
            tab.append(int(processed))
            tab.append(int(max_depth))
            tab.append(float(time))
            results.append(tab)
    return results


def divide_deep(results):
    res1 = []
    res2 = []
    res3 = []
    res4 = []
    res5 = []
    res6 = []
    res7 = []
    for res in results:
        if res[0] == 1:
            res1.append(res)
        if res[0] == 2:
            res2.append(res)
        if res[0] == 3:
            res3.append(res)
        if res[0] == 4:
            res4.append(res)
        if res[0] == 5:
            res5.append(res)
        if res[0] == 6:
            res6.append(res)
        if res[0] == 7:
            res7.append(res)
    return res1, res2, res3, res4, res5, res6, res7


def divide_order(results):
    resDRLU = []
    resDRUL = []
    resLUDR = []
    resLURD = []
    resRDLU = []
    resRDUL = []
    resULDR = []
    resULRD = []
    for res in results:
        if res[3] == 'drlu':
            resDRLU.append(res)
        if res[3] == 'drul':
            resDRUL.append(res)
        if res[3] == 'ludr':
            resLUDR.append(res)
        if res[3] == 'lurd':
            resLURD.append(res)
        if res[3] == 'rdlu':
            resRDLU.append(res)
        if res[3] == 'rdul':
            resRDUL.append(res)
        if res[3] == 'uldr':
            resULDR.append(res)
        if res[3] == 'ulrd':
            resULRD.append(res)
    return resDRLU, resDRUL, resLUDR, resLURD, resRDLU, resRDUL, resULDR, resULRD

def divide_heuristic(results):
    resHAMM = []
    resMANH = []
    for res in results:
        if res[3] == 'hamm':
            resHAMM.append(res)
        if res[3] == 'manh':
            resMANH.append(res)
    return resHAMM, resMANH


def getMean(results, index):
    summ = 0
    for res in results:
        summ += res[index]
    return summ / len(results)


def chart_bfs(results):
    results = [res for res in results if 'bfs' in res]
    res1, res2, res3, res4, res5, res6, res7 = divide_deep(results)
    resDRLU1, resDRUL1, resLUDR1, resLURD1, resRDLU1, resRDUL1, resULDR1, resULRD1 = divide_order(res1)
    resDRLU2, resDRUL2, resLUDR2, resLURD2, resRDLU2, resRDUL2, resULDR2, resULRD2 = divide_order(res2)
    resDRLU3, resDRUL3, resLUDR3, resLURD3, resRDLU3, resRDUL3, resULDR3, resULRD3 = divide_order(res3)
    resDRLU4, resDRUL4, resLUDR4, resLURD4, resRDLU4, resRDUL4, resULDR4, resULRD4 = divide_order(res4)
    resDRLU5, resDRUL5, resLUDR5, resLURD5, resRDLU5, resRDUL5, resULDR5, resULRD5 = divide_order(res5)
    resDRLU6, resDRUL6, resLUDR6, resLURD6, resRDLU6, resRDUL6, resULDR6, resULRD6 = divide_order(res6)
    resDRLU7, resDRUL7, resLUDR7, resLURD7, resRDLU7, resRDUL7, resULDR7, resULRD7 = divide_order(res7)

    print(getMean(resDRUL6, 4))
    N = 7
    ind = np.arange(N)
    width = 0.1

    xvals = [getMean(resDRLU1, 4), getMean(resDRLU2, 4), getMean(resDRLU3, 4), getMean(resDRLU4, 4),
             getMean(resDRLU5, 4), getMean(resDRLU6, 4), getMean(resDRLU7, 4)]
    bar1 = plt.bar(ind, xvals, width, color='r')

    yvals = [getMean(resDRUL1, 4), getMean(resDRUL2, 4), getMean(resDRUL3, 4), getMean(resDRUL4, 4),
             getMean(resDRUL5, 4), getMean(resDRUL6, 4), getMean(resDRUL7, 4)]
    bar2 = plt.bar(ind + width, yvals, width, color='g')

    zvals = [getMean(resLUDR1, 4), getMean(resLUDR2, 4), getMean(resLUDR3, 4), getMean(resLUDR4, 4),
             getMean(resLUDR5, 4), getMean(resLUDR6, 4), getMean(resLUDR7, 4)]
    bar3 = plt.bar(ind + width * 2, zvals, width, color='b')

    avals = [getMean(resLURD1, 4), getMean(resLURD2, 4), getMean(resLURD3, 4), getMean(resLURD4, 4),
             getMean(resLURD5, 4), getMean(resLURD6, 4), getMean(resLURD7, 4)]
    bar4 = plt.bar(ind + width * 3, avals, width, color='cyan')

    bvals = [getMean(resRDLU1, 4), getMean(resRDLU2, 4), getMean(resRDLU3, 4), getMean(resRDLU4, 4),
             getMean(resRDLU5, 4), getMean(resRDLU6, 4), getMean(resRDLU7, 4)]
    bar5 = plt.bar(ind + width * 4, bvals, width, color='black')

    cvals = [getMean(resRDUL1, 4), getMean(resRDUL2, 4), getMean(resRDUL3, 4), getMean(resRDUL4, 4),
             getMean(resRDUL5, 4), getMean(resRDUL6, 4), getMean(resRDUL7, 4)]
    bar6 = plt.bar(ind + width * 5, cvals, width, color='brown')

    dvals = [getMean(resULDR1, 4), getMean(resULDR2, 4), getMean(resULDR3, 4), getMean(resULDR4, 4),
             getMean(resULDR5, 4), getMean(resULDR6, 4), getMean(resULDR7, 4)]
    bar7 = plt.bar(ind + width * 6, dvals, width, color='yellow')

    evals = [getMean(resULRD1, 4), getMean(resULRD2, 4), getMean(resULRD3, 4), getMean(resULRD4, 4),
             getMean(resULRD5, 4), getMean(resULRD6, 4), getMean(resULRD7, 4)]
    bar8 = plt.bar(ind + width * 7, evals, width, color='orange')

    plt.xlabel("Głębokość")
    plt.ylabel('Długość')
    plt.title("Średnia długość rozwiązania BFS")

    plt.xticks(ind + width, ['1', '2', '3', '4', '5', '6', '7'])
    plt.legend((bar1, bar2, bar3, bar4, bar5, bar6, bar7, bar8), ('DRLU', 'DRUL', 'LUDR', 'LURD', 'RDLU', 'RDUL',
                                                                  'ULDR', 'ULRD'))
    plt.show()


def chart_dfs(results):
    results = [res for res in results if 'dfs' in res]
    res1, res2, res3, res4, res5, res6, res7 = divide_deep(results)
    resDRLU1, resDRUL1, resLUDR1, resLURD1, resRDLU1, resRDUL1, resULDR1, resULRD1 = divide_order(res1)
    resDRLU2, resDRUL2, resLUDR2, resLURD2, resRDLU2, resRDUL2, resULDR2, resULRD2 = divide_order(res2)
    resDRLU3, resDRUL3, resLUDR3, resLURD3, resRDLU3, resRDUL3, resULDR3, resULRD3 = divide_order(res3)
    resDRLU4, resDRUL4, resLUDR4, resLURD4, resRDLU4, resRDUL4, resULDR4, resULRD4 = divide_order(res4)
    resDRLU5, resDRUL5, resLUDR5, resLURD5, resRDLU5, resRDUL5, resULDR5, resULRD5 = divide_order(res5)
    resDRLU6, resDRUL6, resLUDR6, resLURD6, resRDLU6, resRDUL6, resULDR6, resULRD6 = divide_order(res6)
    resDRLU7, resDRUL7, resLUDR7, resLURD7, resRDLU7, resRDUL7, resULDR7, resULRD7 = divide_order(res7)


    N = 7
    ind = np.arange(N)
    width = 0.1

    xvals = [getMean(resDRLU1, 4), getMean(resDRLU2, 4), getMean(resDRLU3, 4), getMean(resDRLU4, 4),
             getMean(resDRLU5, 4), getMean(resDRLU6, 4), getMean(resDRLU7, 4)]
    bar1 = plt.bar(ind, xvals, width, color='r')

    yvals = [getMean(resDRUL1, 4), getMean(resDRUL2, 4), getMean(resDRUL3, 4), getMean(resDRUL4, 4),
             getMean(resDRUL5, 4), getMean(resDRUL6, 4), getMean(resDRUL7, 4)]
    bar2 = plt.bar(ind + width, yvals, width, color='g')

    zvals = [getMean(resLUDR1, 4), getMean(resLUDR2, 4), getMean(resLUDR3, 4), getMean(resLUDR4, 4),
             getMean(resLUDR5, 4), getMean(resLUDR6, 4), getMean(resLUDR7, 4)]
    bar3 = plt.bar(ind + width * 2, zvals, width, color='b')

    avals = [getMean(resLURD1, 4), getMean(resLURD2, 4), getMean(resLURD3, 4), getMean(resLURD4, 4),
             getMean(resLURD5, 4), getMean(resLURD6, 4), getMean(resLURD7, 4)]
    bar4 = plt.bar(ind + width * 3, avals, width, color='cyan')

    bvals = [getMean(resRDLU1, 4), getMean(resRDLU2, 4), getMean(resRDLU3, 4), getMean(resRDLU4, 4),
             getMean(resRDLU5, 4), getMean(resRDLU6, 4), getMean(resRDLU7, 4)]
    bar5 = plt.bar(ind + width * 4, bvals, width, color='black')

    cvals = [getMean(resRDUL1, 4), getMean(resRDUL2, 4), getMean(resRDUL3, 4), getMean(resRDUL4, 4),
             getMean(resRDUL5, 4), getMean(resRDUL6, 4), getMean(resRDUL7, 4)]
    bar6 = plt.bar(ind + width * 5, cvals, width, color='brown')

    dvals = [getMean(resULDR1, 4), getMean(resULDR2, 4), getMean(resULDR3, 4), getMean(resULDR4, 4),
             getMean(resULDR5, 4), getMean(resULDR6, 4), getMean(resULDR7, 4)]
    bar7 = plt.bar(ind + width * 6, dvals, width, color='yellow')

    evals = [getMean(resULRD1, 4), getMean(resULRD2, 4), getMean(resULRD3, 4), getMean(resULRD4, 4),
             getMean(resULRD5, 4), getMean(resULRD6, 4), getMean(resULRD7, 4)]
    bar8 = plt.bar(ind + width * 7, evals, width, color='orange')

    plt.xlabel("Głębokość")
    plt.ylabel('Długość')
    plt.title("Średnia długość rozwiązania DFS")

    plt.xticks(ind + width, ['1', '2', '3', '4', '5', '6', '7'])
    plt.legend((bar1, bar2, bar3, bar4, bar5, bar6, bar7, bar8), ('DRLU', 'DRUL', 'LUDR', 'LURD', 'RDLU', 'RDUL',
                                                                  'ULDR', 'ULRD'))
    plt.show()


def chart_astr(results):
    results = [res for res in results if 'astr' in res]
    res1, res2, res3, res4, res5, res6, res7 = divide_deep(results)
    resHAMM1, resMANH1 = divide_heuristic(res1)
    resHAMM2, resMANH2 = divide_heuristic(res2)
    resHAMM3, resMANH3 = divide_heuristic(res3)
    resHAMM4, resMANH4 = divide_heuristic(res4)
    resHAMM5, resMANH5 = divide_heuristic(res5)
    resHAMM6, resMANH6 = divide_heuristic(res6)
    resHAMM7, resMANH7 = divide_heuristic(res7)

    print(getMean(resHAMM7, 4))
    N = 2
    ind = np.arange(N)
    width = 0.1

    xvals = [getMean(resHAMM1, 4), getMean(resHAMM2, 4), getMean(resHAMM3, 4), getMean(resHAMM4, 4),
             getMean(resHAMM5, 4), getMean(resHAMM6, 4), getMean(resHAMM7, 4)]
    bar1 = plt.bar(ind, xvals, width, color='r')

    yvals = [getMean(resMANH1, 4), getMean(resMANH2, 4), getMean(resMANH3, 4), getMean(resMANH4, 4),
             getMean(resMANH5, 4), getMean(resMANH6, 4), getMean(resMANH7, 4)]
    bar2 = plt.bar(ind + width, yvals, width, color='g')

    plt.xlabel("Głębokość")
    plt.ylabel('Długość')
    plt.title("Średnia długość rozwiązania ASTR")

    plt.xticks(ind + width, ['1', '2', '3', '4', '5', '6', '7'])
    plt.legend((bar1, bar2), ('HAMM', 'MANH'))
    plt.show()
