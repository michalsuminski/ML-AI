import cv2

from skimage.data import lbp_frontal_face_cascade_filename
from skimage.feature import Cascade

import torch
import torchvision
from torch import nn
import torchvision.transforms as transforms
from torchvision.models import SqueezeNet1_1_Weights


class CNN(torch.nn.Module):
    def __init__(self):
        super(CNN, self).__init__()
        torch.manual_seed(42)

        self.convolutional = nn.Sequential(
            nn.Conv2d(3, 64, kernel_size=5, stride=4, padding=2),
            nn.ReLU(inplace=True),
            nn.MaxPool2d(kernel_size=3, stride=2),
            nn.Conv2d(64, 256, kernel_size=5, padding=2),
            nn.ReLU(inplace=True),
            nn.MaxPool2d(kernel_size=3, stride=2),
            nn.Conv2d(256, 512, kernel_size=3, padding=1),
            nn.ReLU(inplace=True),
            nn.MaxPool2d(kernel_size=3, stride=2),
        )

        self.linear = nn.Sequential(
            nn.Dropout(),
            nn.Linear(512, 64),
            nn.ReLU(inplace=True),
            nn.Linear(64, 2)
        )

    def forward(self, x: torch.Tensor) -> torch.Tensor:
        x = self.convolutional(x)
        x = torch.flatten(x, 1)
        return self.linear(x)


class SqueezeNetBinary(nn.Module):
    def __init__(self):
        super(SqueezeNetBinary, self).__init__()
        self.features = torchvision.models.squeezenet1_1(weights=SqueezeNet1_1_Weights.DEFAULT).features
        self.classifier = nn.Sequential(
            nn.AdaptiveAvgPool2d((1, 1)),
            nn.Flatten(),
            nn.Linear(512, 2),
            nn.Softmax(dim=1)
        )

    def forward(self, x):
        x = self.features(x)
        x = self.classifier(x)
        return x


def detect(frame, detector):
    detections = detector.detect_multi_scale(img=frame, scale_factor=1.2, step_ratio=1,
                                             min_size=(100, 100), max_size=(200, 200))
    boxes = []
    for detection in detections:
        x = detection['c']
        y = detection['r']
        w = detection['width']
        h = detection['height']
        boxes.append((x, y, w, h))
    return boxes


def draw(frame, boxes):
    for x, y, w, h in boxes:
        frame = cv2.rectangle(frame, (x, y), (x + w, y + h), color=(255, 0, 0), thickness=2)  # zwraca image


def getResultFromModel(frame, boxes):
    for (x, y, w, h) in boxes:
        detected = frame[y:y + h, x:x + w]
        detected = cv2.cvtColor(detected, cv2.COLOR_BGR2RGB)
        frame_resized = cv2.resize(detected, (64, 64))
        transform = transforms.ToTensor()
        frame_tensor = transform(frame_resized)
        frame_tensor = frame_tensor.unsqueeze(0)
        with torch.no_grad():
            output = model(frame_tensor)
            _, pred = torch.max(output, 1)
        return pred


if __name__ == '__main__':
    # file = lbp_frontal_face_cascade_filename()
    file = "./face.xml"
    detector = Cascade(file)

    model = SqueezeNetBinary()
    loaded_model = torch.load('squeezenet_model_eyeglasses_weights.pth', map_location=torch.device('cpu'))
    model.load_state_dict(loaded_model)
    model.eval()

    cap = cv2.VideoCapture(0)
    skip = 5
    i = 0
    boxes = []
    while (True):
        ret, frame = cap.read()
        if i % skip == 0:
            boxes = detect(frame, detector)

        # wynik z modelu
        # male = getResultFromModel(frame, boxes)
        # glasses = 1
        # if male == 1:
        #     cv2.putText(frame, "MALE", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2, cv2.LINE_AA)
        # else:
        #     cv2.putText(frame, "FEMALE", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2, cv2.LINE_AA)
        # # male = 1
        glasses = getResultFromModel(frame, boxes)
        if glasses == 1:
            cv2.putText(frame, "GLASSES", (10, 60), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2, cv2.LINE_AA)
        else:
            cv2.putText(frame, "NO GLASSES", (10, 60), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2, cv2.LINE_AA)

        draw(frame, boxes)
        cv2.imshow('Test', frame)

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
        i += 1
    cap.release()
    cv2.destroyAllWindows()
