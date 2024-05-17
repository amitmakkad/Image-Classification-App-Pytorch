# Image Classification App

This app is used to check the deployment of the VIT model from Hugging Face on mobile, allowing us to classify images by running VIT on mobile resources.

<p align="center">
  <img src="https://github.com/amitmakkad/Image-Classification-App-Pytorch/assets/79632719/617c4f64-bc2d-47f8-b4a4-9b3f7565cb62" alt="Image Classification App">
</p>

Different methods explored are:
* **TensorFlow Lite**:
  - Efficiently runs models on Android with hardware acceleration.
  - Limited to `TfVitForImageClassifier()`.
* **Using ONNX as an Intermediate Converter**:
  - ONNX (Open Neural Network Exchange) is an open standard that defines a common file format to represent deep learning models in a wide variety of frameworks.
  - But it may create problems for some custom layers and operations specifically defined in PyTorch.
* **PyTorch Mobile**:
  - Offers APIs for preprocessing and ML integration in mobile apps.
  - Relatively newer than TensorFlow Lite, so it has less community support.

<p align="center">
  <img src="https://github.com/amitmakkad/Image-Classification-App-Pytorch/assets/79632719/54c8807d-1b65-444d-92d1-ff60f9807343" alt="Methods Explored">
</p>

## Testing App

Implemented an automated testing framework that takes images from the gallery, runs our app, and predicts results. The model works fine on mobile resources. It uses Espresso, an open-source Android testing framework that allows you to create automated tests and provides an API to launch activities with direct access to the dataset.

<p align="center">
  <img src="https://github.com/amitmakkad/Image-Classification-App-Pytorch/assets/79632719/19b30d3a-9ace-477a-909a-9964b658d658" alt="Automated Testing">
</p>
