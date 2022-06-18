from flask import request
from flask_restful import Resource
from flask_jwt_extended import get_jwt_identity
from flask_jwt_extended import jwt_required
from http import HTTPStatus

from models.ruta import Ruta
from schemas.ruta import RutaSchema

ruta_schema = RutaSchema()
ruta_list_schema = RutaSchema(many=True)


class RutaListResource(Resource):

    def get(self):

        rutas = Ruta.get_all()

        return ruta_list_schema.dump(rutas), HTTPStatus.OK

    @jwt_required()
    def post(self):

        json_data = request.get_json()

        current_user = get_jwt_identity()

        try:
            data = ruta_schema.load(data=json_data)
        except ValidationError as exc: # noqa
            return {'message': "Validation errors", 'errors': exc.messages}, HTTPStatus.BAD_REQUEST

        ruta = Ruta(**data)
        ruta.user_id = current_user
        ruta.save()

        return ruta_schema.dump(ruta), HTTPStatus.CREATED


class RutaResource(Resource):

    @jwt_required(optional=True)
    def get(self, ruta_id):

        ruta = Ruta.get_by_id(ruta_id=ruta_id)

        if ruta is None:
            return {'message': 'Ruta no encontrada'}, HTTPStatus.NOT_FOUND

        return ruta_schema.dump(ruta), HTTPStatus.OK

    @jwt_required()
    def patch(self, ruta_id):

        json_data = request.get_json()

        try:
            data = ruta_schema.load(data=json_data, partial=('nombre',))
        except ValidationError as exc: # noqa
            return {'message': "Validation errors", 'errors': exc.messages}, HTTPStatus.BAD_REQUEST

        ruta = Ruta.get_by_id(ruta_id=ruta_id)

        if ruta is None:
            return {'message': 'Ruta no encontrada'}, HTTPStatus.NOT_FOUND

        ruta.nombre = data.get('nombre') or ruta.nombre

        ruta.save()

        return ruta_schema.dump(ruta), HTTPStatus.OK

    @jwt_required()
    def delete(self, ruta_id):

        ruta = Ruta.get_by_id(ruta_id=ruta_id)

        if ruta is None:
            return {'message': 'Ruta no encontrada'}, HTTPStatus.NOT_FOUND

        ruta.delete()

        return {}, HTTPStatus.NO_CONTENT


class RutaPublishResource(Resource):

    @jwt_required()
    def put(self, ruta_id):

        ruta = Ruta.get_by_id(ruta_id=ruta_id)

        if ruta is None:
            return {'message': 'Ruta no encontrada'}, HTTPStatus.NOT_FOUND

        ruta.save()

        return {}, HTTPStatus.NO_CONTENT

    @jwt_required()
    def delete(self, ruta_id):

        ruta = Ruta.get_by_id(ruta_id=ruta_id)

        if ruta is None:
            return {'message': 'Ruta no encontrada'}, HTTPStatus.NOT_FOUND

        ruta.save()

        return {}, HTTPStatus.NO_CONTENT
