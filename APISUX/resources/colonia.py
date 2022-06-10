from flask import request
from flask_restful import Resource
from flask_jwt_extended import create_access_token
from flask_jwt_extended import get_jwt_identity
from flask_jwt_extended import jwt_required
from flask_jwt_extended import JWTManager
from http import HTTPStatus

from models.colonia import Colonia
from schemas.colonia import ColoniaSchema

colonia_schema = ColoniaSchema()
colonia_list_schema = ColoniaSchema(many=True)


class ColoniaListResource(Resource):

    def get(self):

        colonias = Colonia.get_all()

        return colonia_list_schema.dump(colonias), HTTPStatus.OK

    @jwt_required(optional=True)
    def post(self):
        
        json_data = request.get_json()

        current_user = get_jwt_identity()

        try:
            data = colonia_schema.load(data=json_data)
        except ValidationError as exc:
            return {'message': "Validation errors", 'errors': exc.messages}, HTTPStatus.BAD_REQUEST

        colonia = Colonia(**data)
        colonia.user_id = current_user
        colonia.save()

        return colonia_schema.dump(colonia), HTTPStatus.CREATED


class ColoniaResource(Resource):

    @jwt_required(optional=True)
    def get(self, colonia_id):

        colonia = Colonia.get_by_id(colonia_id=colonia_id)

        if colonia is None:
            return {'message': 'Colonia no encontrada'}, HTTPStatus.NOT_FOUND

        return colonia_schema.dump(colonia), HTTPStatus.OK

    @jwt_required()
    def patch(self, colonia_id):

        json_data = request.get_json()

        try:
            data = colonia_schema.load(data=json_data, partial=('nombre',))
        except ValidationError as exc:
            return {'message': "Validation errors", 'errors': exc.messages}, HTTPStatus.BAD_REQUEST
    
        colonia = Colonia.get_by_id(colonia_id=colonia_id)

        if colonia is None:
            return {'message': 'Colonia no encontrada'}, HTTPStatus.NOT_FOUND

        colonia.nombre = data.get('nombre') or colonia.nombre
        colonia.codigoPostal = data.get('codigoPostal') or colonia.codigoPostal

        colonia.save()

        return colonia_schema.dump(colonia), HTTPStatus.OK
        

    @jwt_required(optional=True)
    def delete(self, colonia_id):
        
        colonia = Colonia.get_by_id(colonia_id=colonia_id)

        if colonia is None:
            return {'message': 'Colonia no encontrada'}, HTTPStatus.NOT_FOUND

        colonia.delete()

        return {}, HTTPStatus.NO_CONTENT


class ColoniaPublishResource(Resource):

    @jwt_required()
    def put(self, colonia_id):

        colonia = Colonia.get_by_id(colonia_id=colonia_id)

        if colonia is None:
            return {'message': 'Colonia no encontrada'}, HTTPStatus.NOT_FOUND

        colonia.save()

        return {}, HTTPStatus.NO_CONTENT
    
    @jwt_required() 
    def delete(self, colonia_id):
        
        colonia = Colonia.get_by_id(colonia_id=colonia_id)

        if colonia is None:
            return {'message': 'Colonia no encontrada'}, HTTPStatus.NOT_FOUND

        colonia.save()

        return {}, HTTPStatus.NO_CONTENT
