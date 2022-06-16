from cgi import test
from http import HTTPStatus
import http.client as httpClient
import json

def test_PostAdmin():
    connection = httpClient.HTTPConnection('127.0.0.1:9090')
    data = {
        "username" : "admin1234",
        "email" : "admin1@uv.mx",
        "password" : "12345"
    }
    data2 = json.dumps(data)
    headers = {"Content-Type": "application/json"}
    response = connection.request(
        "POST",
        "/users",
        data2,
        headers,
    )
    generalResponse = connection.getresponse()
    statusCode = generalResponse.status
    assert HTTPStatus.CREATED == statusCode
