FROM ubuntu:20.04
RUN apt-get update -y
RUN apt-get -y install python3-pip
COPY . /APISUX
WORKDIR /APISUX
RUN pip install -r /app/requirements.txt
ENTRYPOINT ["python"]
CMD ["app.py"]
