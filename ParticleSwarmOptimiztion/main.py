import math
import random


class Particle:
    def __init__(self, x, y, inertia, cognitive_constant, social_constant):
        self.x = x
        self.y = y
        self.adaptation = math.inf
        self.speed = 0
        self.best_x = x
        self.best_y = y
        self.best_adaptation = math.inf
        self.inertia = inertia
        self.cognitive_constant = cognitive_constant
        self.social_constant = social_constant


    def update_adjustment(self):
        self.adaptation = calculate_adaptation(self.x, self.y)
        if self.adaptation < self.best_adaptation:
            self.best_adaptation = self.adaptation
            self.best_x = self.x
            self.best_y = self.y

    def calculate_inertia(self):
        self.inertia = self.inertia * self.speed


    def count_cognitive(self):
        random_component_level_cognitive = random.uniform(0, 1)
        cognitive_acceleration = self.cognitive_constant * random_component_level_cognitive
        return cognitive_acceleration * count_distance(self.best_x, self.best_y, self.x, self.y)

    def update_position(self):
        self.x += self.speed
        self.y += self.speed


def generate_swarm(swarm_quantity, inertia, cognitive_constant, social_constant):
    particles = []
    for p in range(swarm_quantity):
        particles.append(Particle(random.uniform(-10, 10), random.uniform(-10, 10),
                                  inertia, cognitive_constant, social_constant))
    return particles




def count_social(particle, best_particle):
    random_component_level_social = random.uniform(0, 1)
    social_acceleration = particle.social_constant * random_component_level_social
    return social_acceleration * count_distance(best_particle.best_x, best_particle.best_y, particle.x, particle.y)


def count_speed(particle, best_particle):
    particle.speed = particle.inertia + particle.count_cognitive() + count_social(particle, best_particle)


def count_distance(x1, y1, x2, y2):
    return math.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2)


def calculate_adaptation(x, y):
    return 0.26 * (x * x + y * y) - 0.48 * x * y


def get_best_particle(swarm):
    best_adaptation = math.inf
    best_particle = None
    for particle in swarm:
        particle.update_adjustment()
        if particle.adaptation < best_adaptation:
            best_adaptation = particle.adaptation
            best_particle = particle
    return best_particle


def algorithm(iterations, swarm_quantity, inertia, cognitive_constant, social_constant):
    best_particle = None
    swarm = generate_swarm(swarm_quantity, inertia, cognitive_constant, social_constant)
    for i in range(iterations):
        best_particle = get_best_particle(swarm)
        for p in swarm:
            p.calculate_inertia()
            count_speed(p, best_particle)
            p.update_position()
    best_particle = get_best_particle(swarm)
    return best_particle.x, best_particle.y, best_particle.adaptation


print(algorithm(, 75, 0.2, 0.35, 0.45))
