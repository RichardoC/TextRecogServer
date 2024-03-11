# TextRecogServer

This android app server will run on all interfaces on port 8080, and perform text recognition.

Example curl command, assuming you have a file called `my-meme.webp` in the current working directory and your phone is connected to you wifi as `192.168.88.160`

```bash
curl -F "image=@my-meme.webp" http://192.168.88.160:8080/
```

Specifically, POST the image as "image" and you'll get the recognised text back.

## Why?

I got inspired by <https://findthatmeme.com/blog/2023/01/08/image-stacks-and-iphone-racks-building-an-internet-scale-meme-search-engine-Qzrz7V6T.html> and wanted to make my own version but for android

## How does it work?

It uses [com.google.mlkit.vision.text.TextRecognition](https://developers.google.com/ml-kit/vision/text-recognition/v2/android) to recognise the text and [NanoHTTPD](https://github.com/NanoHttpd/nanohttpd) for the webserver side.