from cgi import test
from http import HTTPStatus
import http.client as httpClient
import json
import requests

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

def test_Login():
    global token
    connection = httpClient.HTTPConnection('127.0.0.1:9090')
    data = {
        "username" : "admin1234",
        "password" : "12345"
    }
    data2 = json.dumps(data)
    headers = {"Content-Type": "application/json"}
    response = connection.request(
        "POST",
        "/token",
        data2,
        headers,
    )
    generalResponse = connection.getresponse()
    readResponse = generalResponse.read()
    json_format = json.loads(readResponse)
    statusCode = generalResponse.status
    token = json_format["access_token"]
    assert HTTPStatus.OK == statusCode

def test_PostColonia():
    global token
    global bear
    connection = httpClient.HTTPConnection('127.0.0.1:9090')
    data = {
        "nombre" : "Colonia Nueva",
        "codigoPostal" : "290116"
    }
    bear = "Bearer " + token
    data2 = json.dumps(data)
    headers = {"Content-Type": "application/json", "Authorization": bear}
    response = connection.request(
        "POST",
        "/colonias",
        data2,
        headers,
    )
    generalResponse = connection.getresponse()
    statusCode = generalResponse.status
    assert HTTPStatus.CREATED == statusCode

def test_PatchColonia():
    global token
    global bear
    connection = httpClient.HTTPConnection('127.0.0.1:9090')
    data = {
        "nombre" : "Colonia Nueva Editada",
        "codigoPostal" : "290117"
    }
    bear = "Bearer " + token
    data2 = json.dumps(data)
    headers = {"Content-Type": "application/json", "Authorization": bear}
    response = connection.request(
        "PATCH",
        "/colonias/1",
        data2,
        headers,
    )
    generalResponse = connection.getresponse()
    statusCode = generalResponse.status
    assert HTTPStatus.OK == statusCode

def test_DeleteColonia():
    global token
    global bear
    connection = httpClient.HTTPConnection('127.0.0.1:9090')
    data = {
        "nombre" : "Colonia Nueva Editada",
        "codigoPostal" : "290117"
    }
    bear = "Bearer " + token
    data2 = json.dumps(data)
    headers = {"Content-Type": "application/json", "Authorization": bear}
    response = connection.request(
        "DELETE",
        "/colonias/1",
        data2,
        headers,
    )
    generalResponse = connection.getresponse()
    statusCode = generalResponse.status
    assert HTTPStatus.NO_CONTENT == statusCode

def test_GetColonias():
    x = requests.get('http://127.0.0.1:9090/colonias')
    statusCode = x.status_code
    assert HTTPStatus.OK == statusCode
