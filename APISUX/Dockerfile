FROM ubuntu:20.04
RUN apt-get update -y
RUN apt-get -y install python3-pip
COPY . /APISUX
WORKDIR /APISUX
RUN pip install -r /APISUX/requirements.txt
ENTRYPOINT ["python"]
CMD [“python3”,,“-m”,”flask”,”run”,”--host=0.0.0.0”]
