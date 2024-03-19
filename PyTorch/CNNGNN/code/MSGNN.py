import torch
import torch.nn.functional as F
import torchvision
from torch_geometric.data import Data, DataListLoader
from torch_geometric.nn import GCNConv, Linear, global_mean_pool, global_max_pool
from torchvision.transforms import transforms


class GCN(torch.nn.Module):
    def __init__(self, hidden_channels, activationGNN, activationOut):
        super(GCN, self).__init__()
        torch.manual_seed(42)

        self.conv1 = GCNConv(1, hidden_channels)
        self.conv2 = GCNConv(hidden_channels, hidden_channels)
        self.activationGNN = activationGNN
        self.activationOut = activationOut

        self.out = Linear(hidden_channels * 2, 10)

    def forward(self, x, edge_index, batch_index):
        x = self.conv1(x, edge_index)
        x = self.activationGNN(x)
        x = F.dropout(x, p=0.5, training=self.training)

        x = self.conv2(x, edge_index)
        x = self.activationGNN(x)
        x = F.dropout(x, p=0.5, training=self.training)

        x = torch.cat([global_max_pool(x, batch_index), global_mean_pool(x, batch_index)], dim=1)

        if self.activationOut == torch.softmax:
            x = self.activationOut(self.out(x), dim=1)
        else:
            x = self.activationOut(self.out(x))
        return x


def get_index(width, row, col):
    return width * row + col


def get_edge_indexes(image):
    dim, height, width = image.shape
    print("dim: " + str(dim))
    print("height: " + str(height))
    print("width: " + str(width))

    edge_indexes_start = []
    edge_indexes_end = []

    for i in range(height):
        for j in range(width):
            current_index = get_index(width, i, j)
            if i == 0:  # pierwszy wiersz
                if j == 0:
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i + 1, j))  # dół
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i, j + 1))  # prawo
                elif j == 27:
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i + 1, j))  # dół
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i, j - 1))  # lewo
                else:
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i + 1, j))  # dół
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i, j + 1))  # prawo
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i, j - 1))  # lewo
            elif i == 27:  # ostatni wiersz
                if j == 0:
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i - 1, j))  # góra
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i, j + 1))  # prawo
                elif j == 27:
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i - 1, j))  # góra
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i, j - 1))  # lewo
                else:
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i - 1, j))  # góra
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i, j + 1))  # prawo
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i, j - 1))  # lewo
            else:
                if j == 0:  # pierwsza kolumna
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i - 1, j))  # góra
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i, j + 1))  # prawo
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i + 1, j))  # dół
                elif j == 27:  # ostatnia kolumna
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i - 1, j))  # góra
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i, j - 1))  # lewo
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i + 1, j))  # dół
                else:  # wszystkie pozostałe
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i - 1, j))  # góra
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i, j - 1))  # lewo
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i + 1, j))  # dół
                    edge_indexes_start.append(current_index)
                    edge_indexes_end.append(get_index(width, i, j + 1))  # prawo
    edges = torch.tensor([edge_indexes_start, edge_indexes_end])
    return edges


def create_graphs(image, edge_indexes, real_class):
    return Data(x=image, edge_index=edge_indexes, y=torch.tensor([real_class]))


train_dataset = torchvision.datasets.MNIST('./data', train=True, transform=transforms.ToTensor(), download=True)
test_dataset = torchvision.datasets.MNIST('./data', train=False, transform=transforms.ToTensor(), download=True)

train_graphs = []
test_graphs = []

edge_indexes = get_edge_indexes(train_dataset[0][0])

for data, target in train_dataset:
    train_graphs.append(create_graphs(data[0].view(-1, 1), edge_indexes, target))
for data, target in test_dataset:
    test_graphs.append(create_graphs(data[0].view(-1, 1), edge_indexes, target))


def custom_collate_fn(batch):
    return DataListLoader(batch)


loss_fn = torch.nn.CrossEntropyLoss()


def train(dataset):
    for batch in dataset.dataset:
        optimizer.zero_grad()
        pred = model(batch.x, batch.edge_index, batch.batch)
        loss = torch.sqrt(loss_fn(pred, batch.y))
        loss.backward()
        optimizer.step()
    return loss


def test(dataset):
    correct = 0
    model.eval()
    with torch.no_grad():
        for batch in dataset.dataset:
            pred = model(batch.x, batch.edge_index, batch.batch)
            pred = torch.argmax(pred, dim=1)
            correct += pred.eq(batch.y.view_as(pred)).sum().item()
    accuracy = 100. * correct / len(test_loader.dataset)
    print(correct)
    return accuracy


def final_train(epochs):
    losses = []
    for epoch in range(0, epochs):
        loss = train(train_loader)
        print(loss)
        losses.append(loss)
    return losses


def assign_optimizer(model, optimizer_name):
    if optimizer_name == 'Adam':
        return torch.optim.Adam(model.parameters(), lr=0.01)
    elif optimizer_name == 'SGD':
        return torch.optim.SGD(model.parameters(), lr=0.01)
    elif optimizer_name == 'Adagrad':
        return torch.optim.Adagrad(model.parameters(), lr=0.01)
    else:
        return -1


hidden_channels_tab = [16, 32, 64]
activationGNN_tab = [torch.relu, torch.tanh, F.leaky_relu]
activationOut_tab = [torch.softmax, torch.sigmoid]
optimizers_tab = ['SGD', 'Adam', 'Adagrad']

train_loader = torch.utils.data.DataLoader(train_graphs, batch_size=64, shuffle=True, collate_fn=custom_collate_fn)
test_loader = torch.utils.data.DataLoader(test_graphs, batch_size=64, shuffle=True, collate_fn=custom_collate_fn)

accuracies = []
params = []

for h_c in hidden_channels_tab:
    for aGNN in activationGNN_tab:
        for aOut in activationOut_tab:
            for opt in optimizers_tab:
                print("aGNN: ", aGNN, ", aOut: ", aOut, ", opt: ", opt)
                model = GCN(hidden_channels=h_c, activationGNN=aGNN, activationOut=aOut)
                device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")

                model = model.to(device)
                optimizer = assign_optimizer(model, opt)
                loss = final_train(15)
                print(loss)
                accuracies.append(test(test_loader))
                params.append(
                    ['hidden_chanels: ' + str(h_c), 'activationGNN: ' + str(aGNN), 'activationOut: ' + str(aOut),
                     'optimizer: ' + opt, ])
                print(accuracies[-1])

print(accuracies)
for elem in params:
    print(elem)


