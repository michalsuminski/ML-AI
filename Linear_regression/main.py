import math
import matplotlib.pyplot as plt
import numpy as np

with open('data1.csv') as f:
    tab_X = list(range(100))
    tab_Y = list(range(100))
    i = 0
    for line in f.readlines():
        X, Y = line.strip().split(",")

        tab_X[i] = float(X)
        tab_X[i] = [tab_X[i]] 
        tab_Y[i] = float(Y)

        i = i + 1

with open('data2.csv') as f:
    tab_X2 = list(range(100))
    tab_Y2 = list(range(100))
    i = 0
    for line in f.readlines():
        X, Y = line.strip().split(",")

        tab_X2[i] = float(X)
        tab_X2[i] = [tab_X2[i]]
        tab_Y2[i] = float(Y)

        i = i + 1

with open('data3.csv') as f:
    tab_X31 = list(range(100))
    tab_X32 = list(range(100))
    tab_Y3 = list(range(100))
    i = 0
    for line in f.readlines():
        X1, X2, Y = line.strip().split(",")

        tab_X31[i] = float(X1)
        tab_X31[i] = [tab_X31[i]]
        tab_X32[i] = float(X2)
        tab_X32[i] = [tab_X32[i]]
        tab_Y3[i] = float(Y)

        i = i + 1

with open('data4.csv') as f:
    tab_X41 = list(range(100))
    tab_X42 = list(range(100))
    tab_Y4 = list(range(100))
    i = 0
    for line in f.readlines():
        X1, X2, Y = line.strip().split(",")

        tab_X41[i] = float(X1)
        tab_X41[i] = [tab_X41[i]]
        tab_X42[i] = float(X2)
        tab_X42[i] = [tab_X42[i]]
        tab_Y4[i] = float(Y)

        i = i + 1

alfa = 0.01

def Q(x, p, y):
    res = 0.0
    for i in range(len(x)):
        res += (glowna_funkcja(x[i], p) - y[i]) ** 2
    return res / (2 * (len(x)))


def glowna_funkcja(x, parametry):
    suma = 0.0
    # range numeruje od 0 tak jak tablica
    for i in range(len(parametry)):
        suma = suma + x[i] * parametry[i]
    return suma


def pochodna(X, Y, p, i):
    res = 0.0
    for j in range(len(X)):
        res += (glowna_funkcja(X[j], p) - Y[j]) * X[j][i]
    return (1 / (len(X))) * res


def funkcja_operacyjna(p, x, y, alfa):
    while Q(x, p, y) > 0.00000001:
        a = Q(x, p, y)
        for i in range(len(p)):
            p[i] = p[i] - alfa * pochodna(x, y, p, i)
        if abs(a - Q(x, p, y)) < 0.00000001:
            break

def odchylenia(x, y, p):
    err = list(range(100))
    for i in range(len(err)):
        err[i] = abs(glowna_funkcja(x[i], p) - y[i])
        # err[i] = y[i] - glowna_funkcja(x[i], p)
    return err

def Var(tab):
    srednia = sum(tab)/len(tab)
    res = 0.0
    for i in range(len(tab)):
        res += (tab[i] - srednia)**2
    return res/len(tab)


def ax(tab_X, tab_Y, alfa):
    p = [0]
    pom = tab_X.copy()
    funkcja_operacyjna(p, pom, tab_Y, alfa)
    Err = odchylenia(pom, tab_Y, p)
    FUV = Var(Err) / Var(tab_Y)

    print("Dla f(x) = ax")
    print("a równa się: " + str(p[0]))
    print("Sredni błąd kwadratowy: " + str(Q(pom, p, tab_Y)))
    print("Największa wartość odchylenia: " + str(max(Err)))
    print(("Współczynnik determinacji(R^2): ") + str(1 - FUV))

    plt.scatter(tab_X, tab_Y, c="red")
    predykcja = list(range(100))
    for i in range(len(predykcja)):
        predykcja[i] = tab_X[i][0] * p[0]
    plt.plot(tab_X, predykcja, c="green")
    plt.xlabel('X')
    plt.ylabel('Y')

    plt.show()

    height = np.array(Err)
    y_pos = np.arange(len(height))

    # Figsize
    plt.figure(figsize=(15, 5))
    # Create bars
    plt.bar(y_pos, height, color='#969696')
    plt.xlabel('Nr elementu', fontsize=12, color='#323232')
    plt.ylabel('Wartosc odchylenia', fontsize=12, color='#323232')
    plt.title('Histogram odchyleń wartości funkcji od danych', fontsize=16, color='#323232')
    plt.show()


def ax_b(tab_X, tab_Y, alfa):
    p = [0, 0]
    pom = tab_X.copy()
    for i in range(len(tab_X)):
        # tab_X[i] = [tab_X[i][0], 1] # równoznaczne z tab_X[i] = [*tab_X[i], 1]
        pom[i] = [tab_X[i][0], 1]

    funkcja_operacyjna(p, pom, tab_Y, alfa)
    Err = odchylenia(pom, tab_Y, p)
    FUV = Var(Err) / Var(tab_Y)

    print("Dla f(x) = ax + b")
    print("a równa się: " + str(p[0]))
    print("b równa się: " + str(p[1]))
    print("Sredni błąd kwadratowy: " + str(Q(pom, p, tab_Y)))
    print("Największa wartość odchylenia: " + str(max(Err)))
    print(("Współczynnik determinacji(R^2): ") + str(1 - FUV))

    plt.scatter(tab_X, tab_Y, c="red")
    predykcja = list(range(100))
    for i in range(len(predykcja)):
        predykcja[i] = p[0] * tab_X[i][0] + p[1]
    plt.plot(tab_X, predykcja, c="green")
    plt.xlabel('X')
    plt.ylabel('Y')
    plt.show()

    height = np.array(Err)
    y_pos = np.arange(len(height))

    # Figsize
    plt.figure(figsize=(15, 5))
    # Create bars
    plt.bar(y_pos, height, color='#969696')
    plt.xlabel('Nr elementu', fontsize=12, color='#323232')
    plt.ylabel('Wartosc odchylenia', fontsize=12, color='#323232')
    plt.title('Histogram odchyleń wartości funkcji od danych', fontsize=16, color='#323232')
    plt.show()


def axkwadrat_bsinx_c(tab_X, tab_Y, alfa):
    p = [0, 0, 0]
    pom = tab_X.copy()
    for i in range(len(tab_X)):
        # zamiana stopni na radiany
        m = math.radians(tab_X[i][0])
        sin = math.sin(m)
        # tab_X[i] = [*tab_X[i]**2, sin, 1]
        pom[i] = [tab_X[i][0] ** 2, sin, 1]

    funkcja_operacyjna(p, pom, tab_Y, alfa)
    Err = odchylenia(pom, tab_Y, p)
    FUV = Var(Err) / Var(tab_Y)

    print("Dla f(x) = ax^2 + bsin(x) + c")
    print("a równa się: " + str(p[0]))
    print("b równa się: " + str(p[1]))
    print("c równa się: " + str(p[2]))
    print("Sredni błąd kwadratowy: " + str(Q(pom, p, tab_Y)))
    print("Największa wartość odchylenia: " + str(max(Err)))
    print(("Współczynnik determinacji(R^2): ") + str(1 - FUV))

    plt.scatter(tab_X, tab_Y, c="red")
    predykcja = list(range(100))
    for i in range(len(predykcja)):
        predykcja[i] = p[0] * tab_X[i][0] ** 2 + p[1] * pom[i][1] + p[2]
    plt.plot(tab_X, predykcja, c="green")
    plt.xlabel('X')
    plt.ylabel('Y')
    plt.show()

    height = np.array(Err)
    y_pos = np.arange(len(height))

    # Figsize
    plt.figure(figsize=(15, 5))
    # Create bars
    plt.bar(y_pos, height, color='#969696')
    plt.xlabel('Nr elementu', fontsize=12, color='#323232')
    plt.ylabel('Wartosc odchylenia', fontsize=12, color='#323232')
    plt.title('Histogram odchyleń wartości funkcji od danych', fontsize=16, color='#323232')
    plt.show()


def trzy_zmienne1(tab_X1, tab_X2, tab_Y, alfa):
    p = [0, 0, 0]
    pom = tab_X1.copy()
    for i in range(len(tab_X1)):
        pom[i] = [tab_X1[i][0], tab_X2[i][0], 1]

    funkcja_operacyjna(p, pom, tab_Y, alfa)
    Err = odchylenia(pom, tab_Y, p)
    FUV = Var(Err) / Var(tab_Y)

    print("Dla f(x1, x2) = ax1 + bx2 + c")
    print("a równa się: " + str(p[0]))
    print("b równa się: " + str(p[1]))
    print("c równa się: " + str(p[2]))
    print("Sredni błąd kwadratowy: " + str(Q(pom, p, tab_Y)))
    print("Największa wartość odchylenia: " + str(max(Err)))
    print(("Współczynnik determinacji(R^2): ") + str(1 - FUV))

    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    X = np.array(tab_X)
    Y = np.array(tab_X2)
    X, Y = np.meshgrid(X, Y)
    Z = X * p[0] + Y * p[1] + p[2]
    ax.plot_surface(X, Y, Z, cmap="Blues_r")
    ax.scatter3D(tab_X1, tab_X2, tab_Y, c='r', marker='o')

    ax.set_xlabel('X Label')
    ax.set_ylabel('Y Label')
    ax.set_zlabel('X2 Label')
    plt.show()

    height = np.array(Err)
    y_pos = np.arange(len(height))

    # Figsize
    plt.figure(figsize=(15, 5))
    # Create bars
    plt.bar(y_pos, height, color='#969696')
    plt.xlabel('Nr elementu', fontsize=12, color='#323232')
    plt.ylabel('Wartosc odchylenia', fontsize=12, color='#323232')
    plt.title('Histogram odchyleń wartości funkcji od danych', fontsize=16, color='#323232')
    plt.show()


def trzy_zmienne2(tab_X1, tab_X2, tab_Y, alfa):
    p = [0, 0, 0, 0, 0, 0]
    pom = tab_X1.copy()
    for i in range(len(tab_X1)):
        pom[i] = [tab_X1[i][0] ** 2, tab_X1[i][0] * tab_X2[i][0], tab_X2[i][0] ** 2, tab_X1[i][0], tab_X2[i][0], 1]

    funkcja_operacyjna(p, pom, tab_Y, alfa)
    Err = odchylenia(pom, tab_Y, p)
    FUV = Var(Err) / Var(tab_Y)

    print("Dla f(x1, x2) = ax1^2 + bx1*x2 + cx2^2 + dx1 + ex2 + f")
    print("a równa się: " + str(p[0]))
    print("b równa się: " + str(p[1]))
    print("c równa się: " + str(p[2]))
    print("d równa się: " + str(p[3]))
    print("e równa się: " + str(p[4]))
    print("f równa się: " + str(p[5]))
    print("Sredni błąd kwadratowy: " + str(Q(pom, p, tab_Y)))
    print("Największa wartość odchylenia: " + str(max(Err)))
    print(("Współczynnik determinacji(R^2): ") + str(1 - FUV))

    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    X = np.array(tab_X1)
    Y = np.array(tab_X2)
    X, Y = np.meshgrid(X, Y)
    Z = p[0] * X**2 + p[1] * X * Y + p[2] * Y**2 + p[3] * X + p[4] * Y + p[5]
    ax.plot_surface(X, Y, Z, cmap="Blues_r")
    ax.scatter3D(tab_X1, tab_X2, tab_Y, c='r', marker='o')

    ax.set_xlabel('X Label')
    ax.set_ylabel('Y Label')
    ax.set_zlabel('X2 Label')
    plt.show()

    height = np.array(Err)
    y_pos = np.arange(len(height))

    # Figsize
    plt.figure(figsize=(15, 5))
    # Create bars
    plt.bar(y_pos, height, color='#969696')
    plt.xlabel('Nr elementu', fontsize=12, color='#323232')
    plt.ylabel('Wartosc odchylenia', fontsize=12, color='#323232')
    plt.title('Histogram odchyleń wartości funkcji od danych', fontsize=16, color='#323232')
    plt.show()


print("WYNIKI DLA DANYCH 'DANE1': ")
ax(tab_X, tab_Y, 0.1)
print("\n")
ax_b(tab_X, tab_Y, 0.1)
print("\n")
axkwadrat_bsinx_c(tab_X, tab_Y, 0.01)
print("\n")

# print("WYNIKI DLA DANYCH 'DANE2': ")
# ax(tab_X2, tab_Y2, 0.01)
# print("\n")
# ax_b(tab_X2, tab_Y2, 0.01)
# print("\n")
# axkwadrat_bsinx_c(tab_X2, tab_Y2, 0.001)
# print("\n")

print("WYNIKI DLA DANYCH 'DANE3': ")
trzy_zmienne1(tab_X31, tab_X32, tab_Y3, 0.1)
# print("\n")
trzy_zmienne2(tab_X31, tab_X32, tab_Y3, 0.01)
print("\n")

# print("WYNIKI DLA DANYCH 'DANE4': ")
# trzy_zmienne1(tab_X41, tab_X42, tab_Y4, 0.1)
# print("\n")
# trzy_zmienne2(tab_X41, tab_X42, tab_Y4, 0.01)
