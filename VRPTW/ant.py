class Ant:
    def __init__(self, graph, start_index=0):
        super()
        self.graph = graph
        self.current_attraction = start_index
        self.vehicle_load = 0
        self.vehicle_travel_time = 0
        self.travel_path = [start_index]
        self.arrival_time = [0]
        self.att_to_visit = list(range(graph.nodes_len))
        self.att_to_visit.remove(start_index)
        self.total_travel_distance = 0

    def visit_attraction(self, next_index):
        self.travel_path.append(next_index)
        self.total_travel_distance += self.graph.distance_matrix[self.current_attraction][next_index]

        dist = self.graph.distance_matrix[self.current_attraction][next_index]
        self.arrival_time.append(self.vehicle_travel_time + dist)

        if self.graph.nodes[next_index].is_depot:
            self.vehicle_load = 0
            self.vehicle_travel_time = 0

        else:
            self.vehicle_load += self.graph.nodes[next_index].demand
            self.vehicle_travel_time += dist + max(
                self.graph.nodes[next_index].ready_time - self.vehicle_travel_time - dist, 0) + self.graph.nodes[
                                            next_index].service_time
            self.att_to_visit.remove(next_index)

        self.current_attraction = next_index

    def check_condition(self, next_index) -> bool:
        if self.vehicle_load + self.graph.nodes[next_index].demand > self.graph.vehicle_capacity:
            return False
        dist = self.graph.distance_matrix[self.current_attraction][next_index]
        wait_time = max(self.graph.nodes[next_index].ready_time - self.vehicle_travel_time - dist, 0)
        service_time = self.graph.nodes[next_index].service_time
        if self.vehicle_travel_time + dist + wait_time + service_time + self.graph.distance_matrix[next_index][0] > \
                self.graph.nodes[0].due_time:
            return False

        if self.vehicle_travel_time + dist > self.graph.nodes[next_index].due_time:
            return False
        return True
